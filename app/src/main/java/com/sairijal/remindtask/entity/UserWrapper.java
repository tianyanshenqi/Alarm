package com.sairijal.remindtask.entity;

import android.support.annotation.NonNull;

/**
 * Created by 11255 on 2017/3/31.
 */

public class UserWrapper implements Comparable<UserWrapper>{
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public UserWrapper(User user){
        this.user = user;
    }

    @Override
    public int compareTo(@NonNull UserWrapper another) {
        if (Integer.valueOf(this.user.getId())>Integer.valueOf(another.user.getId())){
            return 1;
        }else if (Integer.valueOf(this.user.getId())<Integer.valueOf(another.user.getId())){
            return -1;
        }
        return 0;
    }


    public String getId() {
        return user.getId();
    }

    public void setId(String id) {
        user.setId(id);
    }

    public String getName() {
        return user.getName();
    }

    public void setName(String name) {
        user.setName(name);
    }

    public String getSex() {
        return user.getSex();
    }

    public void setSex(String sex) {
        user.setSex(sex);
    }

    public String getPhone() {
        return user.getPhone();
    }

    public void setPhone(String phone) {
        user.setPhone(phone);
    }

    public int getAge() {
        return user.getAge();
    }

    public void setAge(int age) {
        user.setAge(age);
    }

    public void removeFromRealm(){
        user.removeFromRealm();
    }
}
