package com.cj.engine.core;

/**
 * Created by tang on 2016/3/31.
 */
public class ModelBase {
    private boolean isModified = false;
    private boolean isInDb = false;

    protected boolean isModified(){return this.isModified;}
    public boolean isInDb() {return this.isInDb;}

    protected void modify() {this.isModified = true;}
    public void reset() {this.isModified = false;}
    public void inDb() {this.isInDb = true;}
}
