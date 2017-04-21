package com.sairijal.alarm.mqtt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.sairijal.alarm.sharedpreferences.LoginInfoSharePreference;
import com.sairijal.alarm.utils.TimeUtil;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MqttPublishService{
	private static MqttPublishService instance;
	private static final int MSG_CONNECT_FAILED = 1;
	private static final int MSG_CONNECT_SUCCESS = 2;
	private final Context context;
	private String broker         = "tcp://120.25.153.22:61613";
	private String clientId       = "";
	private String subscribeTopic = "";

	private MemoryPersistence persistence ;
	private MqttClient sampleClient;
	private MqttConnectOptions connOpts;
	private ScheduledExecutorService scheduler;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case MSG_CONNECT_FAILED:
					startReconnect();
					break;
				case MSG_CONNECT_SUCCESS:
					subscribe();
					break;
			}
		}
	};

	public static MqttPublishService getInstance(Context context){
		if (instance == null){
			synchronized (context){
				if (instance == null){
					instance = new MqttPublishService(context);
				}
			}
		}
		return instance;
	}
	/*
	private static class SingleHolder{
		private static final MqttPublishService instance = new MqttPublishService();
	}*/

	private MqttPublishService(Context context){
		this.context = context;
		initParams();
		initConnection();
	}

	private void initConnection() {
		try {
			persistence = new MemoryPersistence();
			sampleClient = new MqttClient(broker, clientId, persistence);
            connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false);
            connOpts.setUserName("admin");
            connOpts.setPassword("password".toCharArray());
            connOpts.setConnectionTimeout(10);
			connOpts.setKeepAliveInterval(200);
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void connect() {
		new Thread(){
			@Override
			public void run() {
				try {
					sampleClient.connect(connOpts);
					Message msg = new Message();
					msg.what = MSG_CONNECT_SUCCESS;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = MSG_CONNECT_FAILED;
					handler.sendMessage(msg);
				}
			}
		}.start();
	}

	private void startReconnect() {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if(!sampleClient.isConnected()){
					connect();
				}
			}
		}, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
	}

	public void publish (String topic ,String mqttMessage,int qos) {
		Log.d("publish", "publish: ");
		try {
			MqttMessage message = new MqttMessage(mqttMessage.getBytes());
			message.setQos(qos);
			sampleClient.publish(topic, message);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

    public void subscribe(){
		if (TextUtils.isEmpty(subscribeTopic)){
			return;
		}
		try {
			sampleClient.setCallback(new MqttCallback() {
				@Override
				public void connectionLost(Throwable throwable) {
					startReconnect();
				}

				@Override
				public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
					sendBroadcast(mqttMessage.toString());
					Log.d("messageArrived", mqttMessage.toString());
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
					/*String msg = "断开连接";
					sendBroadcast(msg);*/
				}
			});
			sampleClient.subscribe(subscribeTopic, 2);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	private void sendBroadcast(String msg) {
		Intent intent = new Intent();
		intent.setAction("intent.action.MQTT_RECEIVER");
		intent.putExtra("msg",msg);
		context.sendBroadcast(intent);
	}

	private void initParams() {
		clientId       = LoginInfoSharePreference.getInstancce(context).getData("clientId");
		subscribeTopic = LoginInfoSharePreference.getInstancce(context).getData("subscribeTopic");
	}
}