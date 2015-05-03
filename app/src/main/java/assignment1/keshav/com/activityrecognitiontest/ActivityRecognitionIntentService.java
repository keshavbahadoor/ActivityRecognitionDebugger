package assignment1.keshav.com.activityrecognitiontest;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;


/**
 * Created by Keshav on 4/6/2015.
 */
public class ActivityRecognitionIntentService extends IntentService
{
    private static final String TAG ="KESHAV";
    private ActivityRecognitionResult result;
    private Intent i;
    private DetectedActivity detectedActivity;

    public ActivityRecognitionIntentService()
    {
        super("ActivityRecognitionService");
        i = new Intent("ACTIVITY_RECOGNITION_DATA");
    }


    public void onCreate() {
        super.onCreate();
        Log.d("Server", ">>>onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, startId, startId);
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        return START_STICKY;
    }

    /**
     * Google Play Services calls this once it has analysed the sensor data
     */
    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (ActivityRecognitionResult.hasResult(intent))
        {
            result = ActivityRecognitionResult.extractResult(intent);
            detectedActivity = result.getMostProbableActivity();

            int confidence = detectedActivity.getConfidence();
            String mostProbableName = ActivityUtil.getActivityName(detectedActivity.getType());

            i.putExtra("act", mostProbableName);
            i.putExtra("confidence", confidence);

            Log.d(TAG, "ActivityRecognitionResult: "+ mostProbableName);
            Log.d(TAG, result.toString());

            // Send broadcast
            //this.sendBroadcast(i);
            //Store.append("Activity: " + mostProbableName + " [" + confidence + "]\n");
            Store.addActivityRecord(detectedActivity.getType(), detectedActivity.getConfidence());
        }

    }


}
