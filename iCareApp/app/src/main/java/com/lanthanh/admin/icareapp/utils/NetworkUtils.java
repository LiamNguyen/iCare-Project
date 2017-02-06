package com.lanthanh.admin.icareapp.utils;

import android.net.Uri;
import android.util.Log;

import com.lanthanh.admin.icareapp.domain.model.DTOAppointmentSchedule;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ADMIN on 06-Jan-17.
 */

public class NetworkUtils {
    public static final String TAG = NetworkUtils.class.getSimpleName();
    public static String convertToUrlData(String key, String value){
        StringBuilder result = new StringBuilder();
        result.append(key).append("=").append(value);
        return result.toString();
    }

    public static String[] getKeys(String... array){
        return array;
    }

    public static String[] getValues(String... array){
        return array;
    }

    public static String convertToUrlData(String[] keys, String[] values){
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < keys.length; i++){
            if (i == 0){
                result.append(keys[i]).append("=").append(values[i]);
                continue;
            }
            result.append("&").append(keys[i]).append("=").append(values[i]);
        }
        return result.toString();
    }

    public static String convertDateForDB(Date date){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        return sdf.format(date);
    }

    public static List<int[]> convertToBookingTimeArray(List<DTOAppointmentSchedule> appointmentSchedulesList){
        List<int[]> result = new ArrayList<>();
        for (DTOAppointmentSchedule schedule : appointmentSchedulesList){
            int[] i = {schedule.getDayId(), schedule.getHourId()};
            result.add(i);
        }
        return result;
    }

    public static URL buildUrl(String base, String[] keys, String[] values){
        Uri.Builder uriBuilder = Uri.parse(base).buildUpon();
        if (keys != null && values != null) {
            for (int i = 0; i < keys.length; i++) {
                uriBuilder.appendQueryParameter(keys[i], values[i]);
            }
        }
        Uri uri = uriBuilder.build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "Malformed URL Exception");
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }
}