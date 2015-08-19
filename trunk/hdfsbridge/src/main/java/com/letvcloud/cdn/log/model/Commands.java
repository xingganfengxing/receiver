package com.letvcloud.cdn.log.model;

/**
 * 命令列表
 * @author liufeng1
 * @date 22/4/2015
 */
public enum Commands {

    START_LOGRECOVER("startLogRecover"), GET_RTMPACCLOG("getRtmpAccLog");

    private String command;

    Commands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString(){
        return command;
    }
}
