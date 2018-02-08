package com.cj.engine.core;

/**
 * Created by tang on 2016/3/31.
 */
public class ModelBase {
    private boolean isModified = false;

    protected boolean isModified(){return this.isModified;}

    protected void modify() {this.isModified = true;}
}
