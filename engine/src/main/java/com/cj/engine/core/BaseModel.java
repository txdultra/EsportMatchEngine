package com.cj.engine.core;

/**
 * Created by tang on 2016/3/31.
 */
public class BaseModel extends PropertyEntity {
    private boolean isModified = false;

    public boolean isModified(){return this.isModified;}

    protected void modify() {this.isModified = true;}
}
