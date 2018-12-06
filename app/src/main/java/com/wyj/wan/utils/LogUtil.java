package com.wyj.wan.utils;

import com.orhanobut.logger.Logger;

/**
 * Created by wangyujie
 * Date 2017/8/10
 * Time 0:04
 * TODO Log日志工具
 */

public class LogUtil {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int JSON = 6;
    public static final int NOTHING = 7;
    public static final int LEVEL = VERBOSE;

    public static void v(String msg) {
        if (LEVEL <= VERBOSE) {
            Logger.v(msg);
        }
    }

    public static void d(String msg) {
        if (LEVEL <= DEBUG) {
            Logger.d(msg);
        }
    }

    public static void i(String msg) {
        if (LEVEL <= INFO) {
            Logger.i(msg);
        }
    }

    public static void w(String msg) {
        if (LEVEL <= WARN) {
            Logger.w(msg);
        }
    }

    public static void e(String msg) {
        if (LEVEL <= ERROR) {
            Logger.e(msg);
        }
    }

    public static void json(String msg) {
        if (LEVEL <= JSON) {
            Logger.json(msg);
        }
    }
}