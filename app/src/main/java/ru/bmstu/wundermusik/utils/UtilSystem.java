package ru.bmstu.wundermusik.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Системные утилиты android
 * @author max
 */
public class UtilSystem {

    /**
     * Проверка наличия интернет-соединения
     * @param context активити, запросившая проверку
     * @return true, если есть сеть
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Показ сообщения пользователю
     * @param messageParentView элемент экрана, где показываем
     * @param message текст сообщения
     */
    public static void displayMessage(View messageParentView, String message) {
        Snackbar snackbar = Snackbar.make(messageParentView, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * Форматирование продолжительности в милисекундах в формат mm:ss
     * @param duration - продолжительность трека в милисекундах
     */
    public static String getDuration (long duration) {
        int total = (int) (duration / 1000);
        int minutes = (total % 3600) / 60;
        int seconds = total % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
