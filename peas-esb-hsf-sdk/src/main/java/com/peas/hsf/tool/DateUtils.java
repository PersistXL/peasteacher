package com.peas.hsf.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by duanyihui on 2017/5/25.
 */
public class DateUtils {


    public static String formatDate(Date date, String formatter) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        return sdf.format(date);
    }

}
