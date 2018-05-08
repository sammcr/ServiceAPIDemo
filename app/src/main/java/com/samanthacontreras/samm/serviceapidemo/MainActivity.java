package com.samanthacontreras.samm.serviceapidemo;

import android.content.Intent;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public MyResultReceiver mReceiver;
    Intent intent;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);
        mReceiver = new MyResultReceiver(null);

        intent = new Intent(this, APIGetService.class);
        intent.putExtra("receiver", mReceiver);

        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }

    public static class Contacto {

        private String name;
        private String cel;

        public String getNom() {
            return name;
        }

        public String getCel() {
            return cel;
        }

        public Contacto(String nom, String cel) {
            this.name = nom;
            this.cel = cel;
        }

    }

    class UpdateUI implements Runnable{
        String updateString;
        public UpdateUI(String updateString){
            this.updateString = updateString;
        }
        @Override
        public void run() {
            tv.setText(updateString);
        }
    }

    public class MyResultReceiver extends ResultReceiver {


        public MyResultReceiver(Handler handler){
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            runOnUiThread(new UpdateUI(resultData.getString("contacto")));
        }
    }
}