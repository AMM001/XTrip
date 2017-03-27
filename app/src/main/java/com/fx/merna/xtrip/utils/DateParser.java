package com.fx.merna.xtrip.utils;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Merna on 3/21/17.
 */

public class DateParser {

    public static Long parseStringDateToLong(String date) {

        try {
            Date dDate = Constants.simpleDateFormat.parse(date);
            return dDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String[] parseLongDateToStrings(Long lDate) {
        Date date = new Date();
        if(lDate != null) {
            date.setTime(lDate);
        }
        return Constants.simpleDateFormat.format(date).split(" ");
    }
}
