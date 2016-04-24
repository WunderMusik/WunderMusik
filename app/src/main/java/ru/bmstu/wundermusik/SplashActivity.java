package ru.bmstu.wundermusik;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import static ru.bmstu.wundermusik.utils.UtilSystem.isOnline;

/**
 * Экран загрузки приложения с логотипом и прелоудером (Заставка)
 * Если есть подключение к интернету, то приложение переходит к главному экрану {@link ru.bmstu.wundermusik.MainActivity MainActivity}.
 * Иначе остается на заставке.
 * @author max
 */
public class SplashActivity extends AppCompatActivity {

    Button checkNetBtn;
    ProgressBar preloader;

    /**
     * Элемент экрана для отображения состояния сети
     */
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

        /**
         * Поток с отложенным на две секунды переходом к главному экрану.
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isOnline(context)) {
                    startMainActivity(context);
                }
                else {
                    showCheckButton();
                }
            }
        }, 2000);
    }

    /**
     * Показать пользователю кнопку проверки сети
     */
    public void showCheckButton() {
        preloader.setVisibility(View.GONE);
        checkNetBtn.setVisibility(View.VISIBLE);
        netStatus.setVisibility(View.VISIBLE);
        netStatus.setText("");
    }

    /**
     * Инициированная пользователем проверка наличия сети
     * @param view кнопка, вызвавшая событие
     */
    public void checkInternet(View view) {
        if (isOnline(this)) {
            netStatus.setText(R.string.net_success_status);
            startMainActivity(SplashActivity.this);
        }
        else {
            netStatus.setText(R.string.no_net_status);
        }
    }

    /**
     * Переход к главному экрану с закрытием заставки
     * @param ctx активити-иницииатор
     */
    public static void startMainActivity(Context ctx) {
        Intent intent = new Intent(ctx, MainActivity.class);
        ctx.startActivity(intent);
        ((Activity) ctx).finish();
    }
}
