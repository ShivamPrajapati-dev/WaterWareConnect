package com.shivamprajapati.waterwareconnect;

import java.io.Serializable;

public class pendingOrderList implements Serializable {

    String name,uId;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
}
