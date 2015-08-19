package com.letvcloud.cdn.log.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FsShell;

import java.util.List;


/**
 * 执行hdfs命令
 *
 * @author wangzhenzhen
 */
public class HdfsExecCommand {

    public static int excute(final List<String> args) throws Exception {
        if (args.get(0).equals("rmr") || args.get(0).equals("rm")) {
            throw new RuntimeException("not support so dangerous command.");
        }
        FsShell fs = new FsShell(new Configuration());
        return fs.run(args.toArray(new String[]{}));
    }
}
