package com.jkcq.homebike;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class BMSDataBean {
    private int Capacity;
    private String Health;
    private int Soc;
    private String Status;
    private int Voltage;
    private int addElectric;
    private boolean alarm;
    private String content;
    private int electric;
    private boolean hotBtn;
    private int tem;
    private int times;
    private ArrayList<Integer> vArray;
    private String workTime;
    private String wrokStateDetail;

    public String toString() {
        return "DataBean [Soc=" + this.Soc + ", Voltage=" + this.Voltage + ", electric=" + this.electric + ", Capacity=" + this.Capacity + ", Status=" + this.Status + ", Health=" + this.Health + ", hotBtn=" + this.hotBtn + ", tem=" + this.tem + ", wrokStateDetail=" + this.wrokStateDetail + ", workTime=" + this.workTime + ", vArray=" + this.vArray + ", addElectric=" + this.addElectric + ", times=" + this.times + ", alarm=" + this.alarm + ", content=" + this.content + "]";
    }

    public int getSoc() {
        return this.Soc;
    }

    public void setSoc(int i) {
        this.Soc = i;
    }

    public int getVoltage() {
        return this.Voltage;
    }

    public void setVoltage(int i) {
        this.Voltage = i;
    }

    public int getElectric() {
        return this.electric;
    }

    public void setElectric(int i) {
        this.electric = i;
    }

    public int getCapacity() {
        return this.Capacity;
    }

    public void setCapacity(int i) {
        this.Capacity = i;
    }

    public String getStatus() {
        return this.Status;
    }

    public void setStatus(String str) {
        this.Status = str;
    }

    public String getHealth() {
        return this.Health;
    }

    public void setHealth(String str) {
        this.Health = str;
    }

    public boolean isHotBtn() {
        return this.hotBtn;
    }

    public void setHotBtn(boolean z) {
        this.hotBtn = z;
    }

    public int getTem() {
        return this.tem;
    }

    public void setTem(int i) {
        this.tem = i;
    }

    public String getWrokStateDetail() {
        return this.wrokStateDetail;
    }

    public void setWrokStateDetail(String str) {
        this.wrokStateDetail = str;
    }

    public String getWorkTime() {
        return this.workTime;
    }

    public void setWorkTime(String str) {
        this.workTime = str;
    }

    public ArrayList<Integer> getvArray() {
        return this.vArray;
    }

    public void setvArray(ArrayList<Integer> arrayList) {
        this.vArray = arrayList;
    }

    public int getAddElectric() {
        return this.addElectric;
    }

    public void setAddElectric(int i) {
        this.addElectric = i;
    }

    public int getTimes() {
        return this.times;
    }

    public void setTimes(int i) {
        this.times = i;
    }

    public boolean getAlarm() {
        return this.alarm;
    }

    public void setAlarm(boolean z) {
        this.alarm = z;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String str) {
        this.content = str;
    }
}
