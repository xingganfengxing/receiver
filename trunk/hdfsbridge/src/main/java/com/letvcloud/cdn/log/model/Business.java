package com.letvcloud.cdn.log.model;

/**
 * 业务线
 * @author liufeng1
 * @date 22/4/2015
 */
public enum Business {

    CLOUD("2"), CDN("102"), LIVE("10");

    private String platId;

    private Business(String platId) {
        this.platId = platId;
    }

    public String getPlatId() {
        return platId;
    }

    public void setPlatId(String platId) {
        this.platId = platId;
    }

    @Override
    public String toString(){
        return platId;
    }

}
