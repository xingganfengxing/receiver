package com.letvcloud.cdn.log.service;

import com.letvcloud.cdn.log.model.Business;
import com.letvcloud.cdn.log.model.LogData;
import com.letvcloud.cdn.log.model.ParseLineResult;
import com.letvcloud.cdn.log.util.CloudTagsUtil;
import com.letvcloud.cdn.log.util.Constants;
import com.letvcloud.cdn.log.util.StrBuilderMapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 保留原始日志
 *
 * @author liufeng1
 * @date 22/4/2015
 */
public class LogSave {

    private static final Logger LOG = LoggerFactory.getLogger(LogSave.class);

    /**
     * 保留云视频,cdn原始日志map组装
     *
     * @param parseLineResult 日志解析结果
     * @param orginLogMap     日志文件名 文件内容Map
     * @param line            某一行日志
     * @return 日志文件名 文件内容Map
     */
    public static Map<String, StringBuilder> buildLogMap(
            ParseLineResult parseLineResult, Map<String, StringBuilder> orginLogMap, String line) {

        LogData logData = parseLineResult.getLogData();
        String platId = logData.getPlatid();

        //云视频,cdn,但不包含m3u8
        if (Business.CDN.toString().equals(platId)
                || Business.CLOUD.toString().equals(platId)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);
                Date ptimeDate = sdf.parse(logData.getPtime());

                String key = buildKey(parseLineResult, ptimeDate);

                //晚到日志加上标识区分
                Calendar cal = Calendar.getInstance();
                Date nowDate = cal.getTime();
                cal.set(Calendar.HOUR_OF_DAY, 4);//凌晨4点
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                Date mergeDate = cal.getTime();
                cal.set(Calendar.HOUR_OF_DAY, 0);//凌晨
                Date newDayDate = cal.getTime();

                //当前时间在凌晨4点后，ptime时间在0点前 算日志晚到
                if (nowDate.after(mergeDate) && ptimeDate.before(newDayDate)) {
                    key = key + ".late";
                }

                //爆米花需要保留原始日志一份，特殊格式一份
                if ("baomihua".equals(logData.getCustomerName())) {
                    StrBuilderMapUtils.appendValue(orginLogMap, key,
                            BmhParse.getBaomihuaLine(logData, line) + "\n");
                    key = key + ".origin";
                }
                StrBuilderMapUtils.appendValue(orginLogMap, key, line + "\n");
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }
        return orginLogMap;
    }

    /**
     * 组装要保留的文件路径
     *
     * @param parseLineResult 解析后的日志
     * @param ptimeDate       日志ptime
     * @return 日志文件路径
     * @throws ParseException
     */
    private static String buildKey(ParseLineResult parseLineResult, Date ptimeDate) throws ParseException {
        String key = "";

        LogData logData = parseLineResult.getLogData();
        String ptimeDay = DateFormatUtils.format(ptimeDate, "yyyyMMdd");
        String ptimeHour = DateFormatUtils.format(ptimeDate, "yyyyMMddHH");

        //云视频
        if (Business.CLOUD.toString().equals(logData.getPlatid())) {
            String sign = logData.getSign();
            String yuntag = sign.contains("bcloud_") ? sign.substring(7) : sign;
            key = ptimeDay + "/cloud_" + yuntag + "_" + ptimeHour;

            List<String> cloudTagsList = CloudTagsUtil.getCloudTags();
            if (!cloudTagsList.contains(yuntag)) {
                key = ptimeDay + "/cloud_unknown_" + ptimeHour;
            }
        }

        //CDN原始日志 map组装
        if (Business.CDN.toString().equals(logData.getPlatid())) {
            key = ptimeDay + "/cdn_" + logData.getCustomerName() + "_" + ptimeHour;
        }

        return key;
    }

    /**
     * 保留云视频和cdn的原始日志
     *
     * @param orginLogMap 日志文件名 文件内容Map
     * @throws IOException
     */
    public static void saveLogMap(Map<String, StringBuilder> orginLogMap) {
        try {
            for (Map.Entry<String, StringBuilder> entry : orginLogMap.entrySet()) {
                String fileName = StringUtils.join("/data/soft/vodcollect/originlogs/", entry.getKey(), ".log");
                FileUtils.writeStringToFile(new File(fileName), entry.getValue().toString(), true);
            }
        } catch (Exception e) {
            LOG.error("saveLogMap error ,caused by {}", e);
        }
    }

    /**
     * 保留发送kafka失败的过滤后的日志
     *
     * @param stringBuilder 日志内容
     * @param fileName      日志文件名
     */
    public static void saveDisasterLog(StringBuilder stringBuilder, String fileName) {
        if (stringBuilder.length() > 0) {  //发送到kafka失败缓存到本地
            String filePath = Constants.LOCAL_FILEPATH + "/" + fileName + ".txt";
            try {
                FileUtils.write(new File(filePath), stringBuilder.toString(), true);
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
    }
}
