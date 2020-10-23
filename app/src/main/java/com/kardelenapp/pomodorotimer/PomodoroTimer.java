package com.kardelenapp.pomodorotimer;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;

import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


import br.com.goncalves.pugnotification.interfaces.PendingIntentNotification;
import br.com.goncalves.pugnotification.notification.PugNotification;

public class PomodoroTimer extends AppCompatActivity implements Runnable {

    private PugNotification notification ;

    private AdView mAdView;

    private TextView textView1;
    private TextView textView2;
    private TextView textView4;
    private TextView textView5;
    private TextView textView7;
    private TextView textView8;

    private Switch switch1;
    private Switch switch2;
    private Switch switch3;
    private Switch switch4;
    private Switch switch5;
    private Switch switch6;

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;

    private Button button   ;
    private Button button2   ;

    private boolean mPause = true;

    private long startedTime = System.currentTimeMillis();

    private long totalTime = 10 * 60 * 60; // 10 hours count down

    private long elapsedTime = 25 * 60;

    private long totalTime25min = 25 * 60;

    private boolean started = false;

    private boolean backgroundprocess = false;

    private  boolean showNotification = false;

    MediaPlayer mp;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    ToggleButton toggleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro_timer);



       switch1 = (Switch) findViewById(R.id.switch1);
       switch2 = (Switch) findViewById(R.id.switch2);
       switch3 = (Switch) findViewById(R.id.switch3);
       switch4 = (Switch) findViewById(R.id.switch4);
       switch5 = (Switch) findViewById(R.id.switch5);
       switch6 = (Switch) findViewById(R.id.switch6);

        editText1 = (EditText) findViewById(R.id.editText2);
        editText2 = (EditText) findViewById(R.id.editText4);
        editText3 = (EditText) findViewById(R.id.editText5);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView8 = (TextView) findViewById(R.id.textView8);

        button= (Button) findViewById(R.id.button);
        button2= (Button) findViewById(R.id.button2);
        button2.setEnabled(false);

        notification= PugNotification.with(getApplicationContext());

        textView1.setText("0");
        textView2.setText("0");
        textView4.setText("2");
        textView5.setText("5");
        textView7.setText("0");
        textView8.setText("0");


        toggleButton= (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setChecked(true);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleButton.isChecked()){
                    pause();
                    button.setEnabled(true);
                }
                else{
                    button.setEnabled(false);
                    resume();

                    switch1.setEnabled(false);
                    switch2.setEnabled(false);
                    switch3.setEnabled(false);
                    switch4.setEnabled(false);

                    editText1.setEnabled(false);
                    editText2.setEnabled(false);
                    editText3.setEnabled(false);

                    if (editText1.length() == 0)
                    {
                        editText1.setText("00");
                    }

                    if (editText2.length() == 0)
                    {
                        editText2.setText("00");
                    }

                    if (editText3.length() == 0)
                    {
                        editText3.setText("00");
                    }


                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(elapsedTime<=0 && started == false){
                    mp.stop();
                    notification.cancel(111);

                    button2.setEnabled(false);
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // pause();
                started = false;
                toggleButton.setChecked(true);

                switch1.setEnabled(true);
                switch2.setEnabled(true);
                switch3.setEnabled(true);
                switch4.setEnabled(true);

                editText1.setEnabled(true);
                editText2.setEnabled(true);
                editText3.setEnabled(true);

                if (switch1.isChecked()){

                    switch2.setChecked(false);
                    switch3.setChecked(false);
                    switch4.setChecked(false);

                    editText1.setEnabled(false);
                    editText2.setEnabled(false);
                    editText3.setEnabled(false);

                    textView1.setText("0");
                    textView2.setText("0");
                    textView4.setText("2");
                    textView5.setText("5");
                    textView7.setText("0");
                    textView8.setText("0");

                }
                else  if (switch2.isChecked()){
                    switch1.setChecked(false);
                    switch3.setChecked(false);
                    switch4.setChecked(false);

                    textView1.setText("0");
                    textView2.setText("0");
                    textView4.setText("1");
                    textView5.setText("0");
                    textView7.setText("0");
                    textView8.setText("0");

                    editText1.setEnabled(false);
                    editText2.setEnabled(false);
                    editText3.setEnabled(false);
                }
                else  if (switch3.isChecked()){
                    switch1.setChecked(false);
                    switch2.setChecked(false);
                    switch4.setChecked(false);

                    textView1.setText("0");
                    textView2.setText("0");
                    textView4.setText("0");
                    textView5.setText("5");
                    textView7.setText("0");
                    textView8.setText("0");

                    editText1.setEnabled(false);
                    editText2.setEnabled(false);
                    editText3.setEnabled(false);
                }
                else  if (switch4.isChecked()){
                    switch1.setChecked(false);
                    switch2.setChecked(false);
                    switch3.setChecked(false);


                    editText1.setEnabled(true);
                    editText2.setEnabled(true);
                    editText3.setEnabled(true);

                    digitSetter();
                }

                elapsedTime = 25 * 60;

            }
        });

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch1.isChecked()){
                    switch2.setChecked(false);
                    switch3.setChecked(false);
                    switch4.setChecked(false);

                    editText1.setEnabled(false);
                    editText2.setEnabled(false);
                    editText3.setEnabled(false);

                    textView1.setText("0");
                    textView2.setText("0");
                    textView4.setText("2");
                    textView5.setText("5");
                    textView7.setText("0");
                    textView8.setText("0");
                }
                else
                {
                    if(!(switch2.isChecked() || switch3.isChecked() || switch4.isChecked()))
                        switch1.setChecked(true);
                    return;
                }
            }
        });



        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch2.isChecked()){
                    switch1.setChecked(false);
                    switch3.setChecked(false);
                    switch4.setChecked(false);

                    editText1.setEnabled(false);
                    editText2.setEnabled(false);
                    editText3.setEnabled(false);

                    textView1.setText("0");
                    textView2.setText("0");
                    textView4.setText("1");
                    textView5.setText("0");
                    textView7.setText("0");
                    textView8.setText("0");
                }
                else
                {
                    if(!(switch1.isChecked() || switch3.isChecked() || switch4.isChecked()))
                    switch2.setChecked(true);
                    return;
                }
            }


        });

        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch3.isChecked()){
                    switch1.setChecked(false);
                    switch2.setChecked(false);
                    switch4.setChecked(false);

                    editText1.setEnabled(false);
                    editText2.setEnabled(false);
                    editText3.setEnabled(false);

                    textView1.setText("0");
                    textView2.setText("0");
                    textView4.setText("0");
                    textView5.setText("5");
                    textView7.setText("0");
                    textView8.setText("0");
                }
                else
                {
                    if(!(switch1.isChecked() || switch2.isChecked() || switch4.isChecked()))
                        switch3.setChecked(true);
                    return;
                }
            }
        });

        switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch4.isChecked()){
                    switch1.setChecked(false);
                    switch2.setChecked(false);
                    switch3.setChecked(false);

                    editText1.setEnabled(true);
                    editText2.setEnabled(true);
                    editText3.setEnabled(true);

                    digitSetter();

                }
                else
                {
                    if(!(switch1.isChecked() || switch3.isChecked() || switch3.isChecked()))
                        switch4.setChecked(true);
                    return;
                }
            }
        });
        switch5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switch5.isChecked()){
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
                else{
                    getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                }


            }
        });
        switch6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switch6.isChecked()){
                    showNotification = true;

                    Intent notificationIntent = new Intent(getApplicationContext(), PomodoroTimer.class);

                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
                            notificationIntent, 0);


                    notification.load()
                            .identifier(111)
                            .title("Set Alarm")
                            .bigTextStyle("Press to open.")
                            .smallIcon(R.drawable.icon)
                            // .largeIcon(R.drawable.icon)
                            .click(intent)
                            //.dismiss(intent)
                            .simple()
                            .build();


                }
                else{
                    showNotification = false;
                    notification.cancel(111);
                }



            }
        });

        editText1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    int val = Integer.parseInt(s.toString());
                    if(val > 99) {
                        editText1.setText("99");
                    }
                } catch (NumberFormatException ex) {
                    // Do something
                }
                digitSetter();

            }
        });

        editText2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    int val = Integer.parseInt(s.toString());
                    if ( val > 60 ) {
                        editText2.setText("60");
                    }
                } catch (NumberFormatException ex) {
                    // Do something
                }
                digitSetter();

            }
        });

        editText3.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    int val = Integer.parseInt(s.toString());
                    if(val > 60) {
                        editText3.setText("60");
                    }
                } catch (NumberFormatException ex) {
                    // Do something
                }

                digitSetter();
            }
        });

        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-3312738864772003/8784874329");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
                Log.i("Ads", "onAdClosed");
            }
        });



    }

    public void digitSetter(){
        String s1 = "0" ,s2 = "0" ,s3 = "0" ,s4 = "0" ,s5 = "0" ,s6 = "0" ,s7 = "0" ,s8 = "0" ;


        if (editText1.getText().length()==0){
            s1 = String.valueOf("0");
            s2 = String.valueOf("0");
        }
        else if (editText1.getText().length()==1){
            s1 = String.valueOf("0") ;
            s2 = editText1.getText().toString() ;
        }
        else if (editText1.getText().length()==2){
            s1 = editText1.getText().subSequence(0,1).toString() ;
            s2 = editText1.getText().subSequence(1,2).toString() ;
        }

        if (editText2.getText().length()==0){
            s4 = String.valueOf("0");
            s5 = String.valueOf("0");
        }
        else if (editText2.getText().length()==1){
            s4 = String.valueOf("0") ;
            s5 = editText2.getText().toString() ;
        }
        else if (editText2.getText().length()==2){
            s4 = editText2.getText().subSequence(0,1).toString() ;
            s5 = editText2.getText().subSequence(1,2).toString() ;
        }


        if (editText3.getText().length()==0){
            s7 = String.valueOf("0");
            s8 = String.valueOf("0");
        }
        else if (editText3.getText().length()==1){
            s7 = String.valueOf("0") ;
            s8 = editText3.getText().toString() ;
        }
        else if (editText3.getText().length()==2){
            s7 = editText3.getText().subSequence(0,1).toString() ;
            s8 = editText3.getText().subSequence(1,2).toString() ;
        }

        textView1.setText(s1);
        textView2.setText(s2);
        textView4.setText(s4);
        textView5.setText(s5);
        textView7.setText(s7);
        textView8.setText(s8);

    }

    public void pause() {
        mPause = true;
    }

    public void resume() {

        mPause = false;

        if (started == false){
            setCounter();
            elapsedTime = totalTime;
            started = true;
        }

        run();
    }

    public void setCounter(){
        if (switch1.isChecked() == true){
            totalTime = 25 * 60;
        }

        if (switch2.isChecked() == true){
            totalTime = 10 * 60;
        }

        if (switch3.isChecked() == true){
            totalTime = 5 * 60;
        }

        if (switch4.isChecked() == true){

            int hh = 0;
            int mm = 0;
            int ss = 0;

            try{
                 hh = Integer.parseInt(String.valueOf(editText1.getText()));
            }catch (Exception e){
                hh = 0;
            }

            try{
                mm = Integer.parseInt(String.valueOf(editText2.getText()));
            }catch (Exception e){
                mm = 0;
            }

            try{
                ss = Integer.parseInt(String.valueOf(editText3.getText()));
            }catch (Exception e){
                ss = 0;
            }

            totalTime = (hh * 60 * 60) + (mm * 60) + ss;
        }
    }

    @Override
    public void run() {


            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);


        if(mPause){
            return;
        }



            if (elapsedTime % 10 == 0) {
                String s = (String) textView7.getText();
                int i = Integer.parseInt(s);
                if(i == 0){
                    i = 5;
                }
                else{
                    i -=1 ;
                }

                s = String.valueOf(i);
                textView7.setText(s);
            }
            if (elapsedTime % 60 == 0) {
                String s = (String) textView5.getText();
                int i = Integer.parseInt(s);
                if(i == 0){
                    i = 9;
                }
                else{
                    i -=1 ;
                }

                s = String.valueOf(i);
                textView5.setText(s);
            }
            if (elapsedTime % 600 == 0) {
                String s = (String) textView4.getText();
                int i = Integer.parseInt(s);
                if(i == 0){
                    i = 5;
                }
                else{
                    i -=1 ;
                }

                s = String.valueOf(i);
                textView4.setText(s);
            }
            if (elapsedTime % 3600 == 0) {
                String s = (String) textView2.getText();
                int i = Integer.parseInt(s);
                if(i == 0){
                    i = 9;
                }
                else{
                    i -=1 ;
                }

                s = String.valueOf(i);
                textView2.setText(s);
            }
            if (elapsedTime % 36000 == 0) {
                String s = (String) textView1.getText();
                int i = Integer.parseInt(s);
                if(i == 0){
                    i = 9;
                }
                else{
                    i -=1 ;
                }

                s = String.valueOf(i);
                textView1.setText(s);
            }

        elapsedTime -= 1;
        textView8.setText(String.valueOf(elapsedTime % 10));

        Intent notificationIntent = new Intent(getApplicationContext(), PomodoroTimer.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, 0);

        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        ;

        if (showNotification == true){
            notification.load()
                    .identifier(111)
                    .title("Remaining Time: " +
                            textView1.getText() + textView2.getText() + ":" +
                            textView4.getText() + textView5.getText() + ":" +
                            textView7.getText()+textView8.getText())
                    .bigTextStyle("Press to open.")
                    .smallIcon(R.drawable.icon)
                    // .largeIcon(R.drawable.icon)
                    .click(intent)
                    //.dismiss(intent)
                    .simple()
                    .build();
        }



        Log.v("elapsedTime", String.valueOf(elapsedTime));
        Log.v("(elapsedTime%10)", String.valueOf((elapsedTime%10)));
        Log.v("(elapsedTime%1", String.valueOf((elapsedTime%60)));

        if (elapsedTime > 0){
            ViewCompat.postOnAnimationDelayed(textView1, this, 1000);
        }
        else
        {
            button2.setEnabled(true);



            mp = MediaPlayer.create(this, R.raw.alarm);
            mp.setLooping(false);

            notification.load()
                    .identifier(111)
                    .title("Time is up.")
                    .bigTextStyle("Press to stop alarm.")
                    .smallIcon(R.drawable.icon)
                    // .largeIcon(R.drawable.icon)
                    .click(intent)
                    //.dismiss()
                    //.sound(uri)
                    .simple()
                    .build();


            mp.start();

            ////

            started = false;
            toggleButton.setChecked(true);

            switch1.setEnabled(true);
            switch2.setEnabled(true);
            switch3.setEnabled(true);
            switch4.setEnabled(true);

            editText1.setEnabled(true);
            editText2.setEnabled(true);
            editText3.setEnabled(true);

            if (switch1.isChecked()){

                switch2.setChecked(false);
                switch3.setChecked(false);
                switch4.setChecked(false);

                editText1.setEnabled(false);
                editText2.setEnabled(false);
                editText3.setEnabled(false);

                textView1.setText("0");
                textView2.setText("0");
                textView4.setText("2");
                textView5.setText("5");
                textView7.setText("0");
                textView8.setText("0");

            }
            else  if (switch2.isChecked()){
                switch1.setChecked(false);
                switch3.setChecked(false);
                switch4.setChecked(false);

                textView1.setText("0");
                textView2.setText("0");
                textView4.setText("1");
                textView5.setText("0");
                textView7.setText("0");
                textView8.setText("0");

                editText1.setEnabled(false);
                editText2.setEnabled(false);
                editText3.setEnabled(false);
            }
            else  if (switch3.isChecked()){
                switch1.setChecked(false);
                switch2.setChecked(false);
                switch4.setChecked(false);

                textView1.setText("0");
                textView2.setText("0");
                textView4.setText("0");
                textView5.setText("5");
                textView7.setText("0");
                textView8.setText("0");

                editText1.setEnabled(false);
                editText2.setEnabled(false);
                editText3.setEnabled(false);
            }
            else  if (switch4.isChecked()){
                switch1.setChecked(false);
                switch2.setChecked(false);
                switch3.setChecked(false);


                editText1.setEnabled(true);
                editText2.setEnabled(true);
                editText3.setEnabled(true);

                digitSetter();
            }

        }



    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(elapsedTime<=0 && started == false){
            mp.stop();
            notification.cancel(111);
        }


    }


}
