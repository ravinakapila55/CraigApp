package com.helper.Helper2Go.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CommonMethods {
    public static String getFormatedDateTime(String dateStr, String strReadFormat, String strWriteFormat) {

        String formattedDate = dateStr;

        DateFormat readFormat = new SimpleDateFormat(strReadFormat, Locale.getDefault());
        DateFormat writeFormat = new SimpleDateFormat(strWriteFormat, Locale.getDefault());

        Date date = null;

        try {
            date = readFormat.parse(dateStr);
        } catch (ParseException e) {
        }

        if (date != null) {
            formattedDate = writeFormat.format(date);
        }

        return formattedDate;
    }

    public static String compareDates(String current,String selected)
    {
        String result="";
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            Date dd1=dateFormat.parse(current);
            Date dd2=dateFormat.parse(selected);
            if (dd1.equals(dd2))
            {
                result="equal";
            }
            else if (dd1.before(dd2))
            {
                result="before";
            }
            else if (dd1.after(dd2))
            {
                result="after";
            }
            Log.e("compareDates: Result ",result);
        }
        catch
        (ParseException e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public static  String getCurrentDate()
    {
        int cYear = Calendar.getInstance().get(Calendar.YEAR);
        int cMonth = Calendar.getInstance().get(Calendar.MONTH);
        int cDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        final String[] Days = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String day = "";

        if (cDay < Days.length)
        {
            day = "0" + cDay;
        } else
        {
            day = "" + cDay;
        }

        int currentMonth=cMonth+1;
        final String[] Months = {"0","1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String mm = "";

        if (currentMonth < Months.length)
        {
            mm = "0" + currentMonth;
        } else
        {
            mm = "" + currentMonth;
        }



//    String currentDate= cYear+"-"+currentMonth+"-"+day;
        String currentDate= cYear+"-"+mm+"-"+day;
        Log.e("setDatePicker: Current ",currentDate);

        return currentDate;
    }

    public static long difference2Dates(String selected,String current)
    {
        long elapsedDays = 0;

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date ddSelected=dateFormat.parse(selected);
            Date ddCurrent=dateFormat.parse(current);
            long difference=ddSelected.getTime()-ddCurrent.getTime();
            Log.e("DatesDifference ",difference+"");

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            elapsedDays = difference / daysInMilli;
//      difference = difference % daysInMilli;

            Log.e("DatesDifference ",difference+"");

        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return elapsedDays;
    }
}
