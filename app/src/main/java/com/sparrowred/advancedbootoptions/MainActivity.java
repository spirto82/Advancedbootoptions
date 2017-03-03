package com.sparrowred.advancedbootoptions;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Process;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private boolean isRooted;
    private ImageButton image;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String rooted = "isPhoneRooted";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageButton) findViewById(R.id.imageView2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.tollbar_title);
        setSupportActionBar(toolbar);

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, 0);
        Boolean isPhoneRooted = sharedpreferences.getBoolean(rooted,false);
        if(isPhoneRooted){
            isRooted = checkSu();
            setPreferences();
            image.setImageResource(R.drawable.trans_root_on);
            ((TransitionDrawable)image.getDrawable()).startTransition(3000);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startNewActivity(isRooted);
                }
            }, 3000);
        }
        else {
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image.setOnClickListener(null);
                    isRooted = checkSu();
                    if (isRooted) {
                        //image.setImageResource(R.drawable.root_true);
                        image.setImageResource(R.drawable.trans_root_on);
                        ((TransitionDrawable) image.getDrawable()).startTransition(3000);
                    } else {
                        //image.setImageResource(R.drawable.root_false);
                        image.setImageResource(R.drawable.trans_root_off);
                        ((TransitionDrawable) image.getDrawable()).startTransition(3000);
                    }
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startNewActivity(isRooted);
                        }
                    }, 3000);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        setPreferences();
        finishAffinity();
        Process.killProcess(Process.myPid());
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        setPreferences();
        finishAffinity();
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        setPreferences();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    private boolean checkSu(){
        return isPhoneRooted() || hasSuperuserApkfile() || isTestKeyBuild();
    }

    public boolean isPhoneRooted(){
        java.lang.Process process = null;
        try{
            process = Runtime.getRuntime().exec("su");
            return true;
        } catch (Exception e) {
            return false;
        } finally{
            if(process != null){
                try{
                    process.destroy();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean hasSuperuserApkfile(){
        return new File("/system/app/Superuser.apk").exists();
    }

    private boolean isTestKeyBuild(){
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private void startNewActivity(boolean rooted){
        if(rooted) {
            Intent intent = new Intent(getBaseContext(), Options.class);
            intent.putExtra("isRooted",rooted);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else{
            terminateApp();
        }
    }

    private void setPreferences(){
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, 0);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(rooted, isRooted);
        editor.apply();
    }

    private void terminateApp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(R.string.terminate_title);
        builder.setMessage(R.string.terminate_message);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDestroy();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
