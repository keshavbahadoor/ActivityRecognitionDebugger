package assignment1.keshav.com.activityrecognitiontest;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.DetectedActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class MainActivity extends Activity implements View.OnClickListener
{
    TextView text;
    TextView log;
    LocalBroadcastManager broadcastManager;
    Button button, logs;

    BroadcastReceiver receiver  = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {

            Log.d("KESHAV", "received something.... ");
            String v =  "Activity :" +
                    intent.getStringExtra("act") + " " +
                    "Confidence : " + intent.getExtras().getInt("confidence") + "\n";

            text.append(v);
            //Store.append(v);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Store.context = this.getApplicationContext();

        this.text = (TextView) this.findViewById(R.id.activity_text);
        this.log = (TextView) this.findViewById(R.id.log);
        this.button = (Button) this.findViewById(R.id.button);
        this.logs = (Button) this.findViewById(R.id.btn_logs);
        this.button.setOnClickListener(this);
        this.logs.setOnClickListener(this);
        log.setText("");

        broadcastManager = LocalBroadcastManager.getInstance(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTIVITY_RECOGNITION_DATA");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filter);

        log.append("Service Running? : " + ServiceUtil.isServiceRunning(ActivityRecognitionService.class, this) + "\n");
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.button)
        {
            Log.d("Keshav", "Btn clicked");
            Intent intent = new Intent(this, ActivityRecognitionService.class);
            this.startService(intent);
            log.append("Service Running? : " + ServiceUtil.isServiceRunning(ActivityRecognitionService.class, this) + "\n");


        }
        if (view.getId() == R.id.btn_logs)
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(Store.read());
            text.setText(gson.toJson(je));
        }

    }

    @Override
    protected void onStop()
    {
        // unregisters the receiver
        unregisterReceiver(receiver);
        super.onStop();
    }


}
