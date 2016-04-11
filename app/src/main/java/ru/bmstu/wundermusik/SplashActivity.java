package ru.bmstu.wundermusik;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Button checkNetBtn;
    ProgressBar preloader;
    TextView netStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkNetBtn = (Button) findViewById(R.id.check_btn);
        preloader = (ProgressBar) findViewById(R.id.preloader);
        netStatus = (TextView) findViewById(R.id.net_status);
        checkNetBtn.setVisibility(View.GONE);
        netStatus.setVisibility(View.GONE);

        final Context context = SplashActivity.this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isOnline()) {
                    startMainActivity(context);
                }
                else {
                    showCheckButton();
                }
            }
        }, 2000);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void showCheckButton() {
        preloader.setVisibility(View.GONE);
        checkNetBtn.setVisibility(View.VISIBLE);
        netStatus.setVisibility(View.VISIBLE);
        netStatus.setText("");
    }

    public void checkInternet(View view) {
        if (isOnline()) {
            netStatus.setText(R.string.net_success_status);
            startMainActivity(SplashActivity.this);
        }
        else {
            netStatus.setText(R.string.no_net_status);
        }
    }

    public static void startMainActivity(Context ctx) {
        Intent intent = new Intent(ctx, MainActivity.class);
        ctx.startActivity(intent);
        ((Activity) ctx).finish();
    }
}
