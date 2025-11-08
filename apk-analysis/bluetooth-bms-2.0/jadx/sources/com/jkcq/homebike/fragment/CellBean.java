package com.jkcq.homebike.fragment;

/* loaded from: classes.dex */
public class CellBean {
    public String name;
    public String value;

    public String getName() {
        return this.name;
    }

    public CellBean(String str, String str2) {
        this.name = str;
        this.value = str2;
    }

    public CellBean(String str) {
        this.name = str;
    }

    public CellBean() {
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public String toString() {
        return "CellBean{name='" + this.name + "', value='" + this.value + "'}";
    }
}
