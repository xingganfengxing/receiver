package com.letv.cdn.receiver.service;

import com.letv.cdn.receiver.model.DataResponse;

import java.util.Map;

/**
 * @author liufeng1
 * @date 5/5/2015
 */
public interface ILogReportService {
    DataResponse handle(String reqBody);
}
