package com.weiqing.noctorrosmssend.util;

import java.util.UUID;

/**
 * @author caoyd
 * @date 2020/6/2 5:56 下午
 */
public class UUIDUtil {

    public static String generate32() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
