package com.example.ricco.entity;

import java.util.List;

/**
 * Created by zydx on 2016/8/2.
 */
public class ObjectModel {

    List<ResourceModel> Resources;
    boolean State;

    public ObjectModel(List<ResourceModel> Resources, boolean State){
        this.Resources=Resources;
        this.State=State;
    }
    public ObjectModel(boolean State){
        this.State=State;
    }

    public void setState(boolean state) {
        State = state;
    }

    public void setResources(List<ResourceModel> resources) {
        Resources = resources;
    }

    public List<ResourceModel> getResources() {
        return Resources;
    }

    public boolean isState() {
        return State;
    }
}
