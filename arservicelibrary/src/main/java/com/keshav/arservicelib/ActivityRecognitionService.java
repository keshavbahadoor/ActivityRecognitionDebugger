package com.keshav.arservicelib;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

/**
 * Created by Keshav on 5/2/2015.
 */
public class ActivityRecognitionService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = "keshav"; //"Constellation-ARS";
    private Looper mServiceLooper;

    private Intent i;
    private PendingIntent activityRecognitionPendingIntent;
    private GoogleApiClient googleApiClient;



    @Override
    public void onCreate()
    {
        Log.i(TAG, "Service Started\n");

        i = new Intent (this, ActivityRecognitionIntentService.class);

//        // Starts thread running the service. A separate thread is created because the service normally
//        // runs in the process's main thread.
//        HandlerThread thread = new HandlerThread("ServiceStartArguements", Process.THREAD_PRIORITY_BACKGROUND);
//        thread.start();
//        mServiceLooper = thread.getLooper();

        //Check Google Play Service Available
        if(isPlayServiceAvailable())
        {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(ActivityRecognition.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            //Connect to gPlay
            googleApiClient.connect();
            Log.i(TAG, "Play services connected\n");
        }else
        {
            Toast.makeText(this, "Google Play Service not Available", Toast.LENGTH_LONG).show();
            Log.i(TAG, "Google Play Service not Available\n");
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "Received start id " + startId + ": " + intent);

        return START_STICKY;
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        i.setAction("ActivityRecognitionIntentService");
        activityRecognitionPendingIntent = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.d(TAG, "connected to ActRecog " + "PI " + activityRecognitionPendingIntent.toString());

        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(googleApiClient, 0, activityRecognitionPendingIntent);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // Binding not provided
        return null;
    }

    @Override
    public void onDestroy()
    {
        Log.d(TAG, "ARS Finsihed");
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.d(TAG, "connection suspended: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Log.d(TAG, "connection failed: " + connectionResult.getErrorCode());
    }

    private boolean isPlayServiceAvailable()
    {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS;
    }
}
