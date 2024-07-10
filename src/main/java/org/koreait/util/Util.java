package org.koreait.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static String getNow() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatedNow = formatter.format(now);
        return formatedNow;
    }
}
