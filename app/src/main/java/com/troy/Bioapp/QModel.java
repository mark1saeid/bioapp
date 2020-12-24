package com.troy.Bioapp;

public class QModel {
    String a ;
    String b;
    String c;
    String d;
    String Q;
    String An;
    String image;
    String id;

    public QModel(String a, String b, String c, String d, String q, String an, String image, String id) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        Q = q;
        An = an;
        this.image = image;
        this.id = id;
    }

    public QModel() {
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getQ() {
        return Q;
    }

    public void setQ(String q) {
        Q = q;
    }

    public String getAn() {
        return An;
    }

    public void setAn(String an) {
        An = an;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}