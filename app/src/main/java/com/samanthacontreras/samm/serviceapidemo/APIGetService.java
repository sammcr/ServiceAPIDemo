package com.samanthacontreras.samm.serviceapidemo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class APIGetService extends Service {
    Timer timer = new Timer();
    MyTimerTask timerTask;
    ResultReceiver resultReceiver;

    public APIGetService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        resultReceiver = intent.getParcelableExtra("receiver");
        timerTask = new MyTimerTask();
        timer.scheduleAtFixedRate(timerTask, 0, 5000);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class MyTimerTask extends TimerTask {
        public Gson gson = new Gson();
        SyncHttpClient client = new SyncHttpClient();
        public ArrayList<MainActivity.Contacto> lista = new ArrayList<>();
        Bundle b;
        String contacto;

        public MyTimerTask() {
            b = new Bundle();
            b.putString("start", "Timer started");
            b.putString("contacto", "");
            resultReceiver.send(100, b);
        }

        @Override
        public void run() {
            client.get("https://still-chamber-30627.herokuapp.com/api/contacts", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String txt = new String(responseBody);

                        Type listType = new TypeToken<ArrayList<MainActivity.Contacto>>() {
                        }.getType();
                        lista = gson.fromJson(txt, listType);

                        if (lista != null){
                            contacto = lista.get(lista.size()-1).getNom();
                            b.putString("contacto", contacto);
                            resultReceiver.send(200, b);
                        }

                    } catch(Exception e){
                        System.out.println(e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });

        }
    }
}
