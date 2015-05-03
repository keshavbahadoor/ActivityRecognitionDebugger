package com.keshav.arservicelib;

import com.google.android.gms.location.DetectedActivity;

/**
 * Created by Keshav on 5/2/2015.
 */
public class ActivityUtil
{
    /**
     * Returns the activity name from activity integer
     * @param detected_activity_type
     * @return
     */
    public static String getActivityName(int detected_activity_type)
    {
        switch (detected_activity_type) {
            case DetectedActivity.IN_VEHICLE:
                return "in vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on bike";
            case DetectedActivity.WALKING:
                return "walking";
            case DetectedActivity.ON_FOOT:
                return "on foot";
            case DetectedActivity.RUNNING:
                return "running";
            case DetectedActivity.TILTING:
                return "tilting";
            case DetectedActivity.STILL:
                return "still";
            default:
                return "unknown";
        }
    }
}
