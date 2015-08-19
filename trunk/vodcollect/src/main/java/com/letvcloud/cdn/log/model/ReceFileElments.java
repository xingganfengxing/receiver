package com.letvcloud.cdn.log.model;

import lombok.Data;

/**
 * 接收到的文件相关信息
 * Created by liufeng1 on 2014/12/30.
 */
@Data
public class ReceFileElments {

    private byte[] receBytes;
    private String orginPath;
    //解压前大小
    private double fileSize;

    public ReceFileElments(byte[] receBytes, String orginPath, double fileSize) {
        this.receBytes = receBytes;
        this.orginPath = orginPath;
        this.fileSize = fileSize;
    }
}
