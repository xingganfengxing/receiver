package com.letv.cdn.receiver.servlet;

import com.letv.cdn.receiver.model.DataResponse;
import com.letv.cdn.receiver.service.ILogReportService;
import com.letv.cdn.receiver.service.ServiceFactory;
import com.letv.cdn.receiver.servlet.support.BaseServlet;
import com.letv.cdn.receiver.util.LogReportConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志上报统一接口
 * Created by liufeng1 on 2015/5/5.
 */
public class LogReportServlet extends BaseServlet {

    private static final Logger LOG = LoggerFactory.getLogger(LogReportServlet.class);

    private ILogReportService service;

    @Override
    protected void execute(HttpServletRequest request, HttpServletResponse response) {

        try {
            String type = getType(request);
            String reqBody = getBodyString(request.getInputStream());

            service = ServiceFactory.getServiceInstance(type);
            if (null != service) {
                writeResponse(response, service.handle(reqBody));
            } else {
                DataResponse dataResponse = new DataResponse();
                dataResponse.setResult(LogReportConstants.REP_FALSE);
                dataResponse.setMessage("type missing match");
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    private String getType(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.substring(path.lastIndexOf("/") + 1, path.length());
    }
}
