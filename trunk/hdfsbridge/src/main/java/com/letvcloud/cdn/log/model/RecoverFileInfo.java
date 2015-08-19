package com.letvcloud.cdn.log.model;

import lombok.Data;

import java.io.File;

/**
 * @author liufeng1
 * @date 27/5/2015
 */
@Data
public class RecoverFileInfo {
    private int index;
    private File file;
    private String content;

    public RecoverFileInfo(int index,File file){
        this.index = index;
        this.file = file;
    }
}
