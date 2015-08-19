package com.letvcloud.cdn.log.service;

import com.letvcloud.cdn.log.model.Business;
import com.letvcloud.cdn.log.model.ParseLineResult;
import com.letvcloud.cdn.log.model.RecoverFileInfo;
import com.letvcloud.cdn.log.util.DateUtil;
import com.letvcloud.cdn.log.util.RecoverConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 带宽恢复
 */
public class RecProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecProcess.class);

    public static void process(RecoverFileInfo recoverFileInfo) {

        StringTokenizer stringTokenizer = new StringTokenizer(recoverFileInfo.getContent(), "\n");
        Map<String, StringBuilder> saveMap = new HashMap<>();

        while (stringTokenizer.hasMoreElements()) {
            String line = stringTokenizer.nextToken();
            if (!StringUtils.isEmpty(line)) {
                //解析
                ParseLineResult parseLineResult = LogParse.parseLog(line, false);

                if (parseLineResult.isParseSuccess()) {

                    //过滤 只补CDN和云视频
                    String platId = parseLineResult.getLogData().getPlatid();
                    if (Business.CDN.toString().equals(platId)
                            || Business.CLOUD.toString().equals(platId)) {

                        parseLineResult = LogFilter.filterLog(parseLineResult, line, false);

                        if (parseLineResult.isParseSuccess()) {
                            String join = parseLineResult.getLogData().toLine();
                            String key = DateUtil.toPattern(parseLineResult.getLogData().getPtime(),
                                    RecoverConstants.US_TIME_FORMAT, RecoverConstants.HOUR_FORMAT);
                            if (saveMap.containsKey(key)) {
                                saveMap.get(key).append(join + "\n");
                            } else {
                                StringBuilder stringBuilder = new StringBuilder(join + "\n");
                                saveMap.put(key, stringBuilder);
                            }
                        }
                    }
                }
            }
        }

        //写文件
        try {
            for (Map.Entry<String, StringBuilder> entry : saveMap.entrySet()) {
                String filePath = StringUtils.join(RecoverConstants.OUT_PATH, entry.getKey());
                FileUtils.writeStringToFile(new File(filePath), entry.getValue().toString(), true);
            }
        } catch (Exception e) {
            LOGGER.error("saveLogMap error ,caused by {}", e);
        }
    }
}
