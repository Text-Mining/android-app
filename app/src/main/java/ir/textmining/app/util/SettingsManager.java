package ir.textmining.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsManager {

    private static SettingsManager instance = null;
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private SettingsManager(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
    }

    public static SettingsManager getInstance(Context context) {

        if (instance == null) {
            instance = new SettingsManager(context);
        }

        return instance;
    }

    public boolean isDarkThemeEnabled() {
        return prefs.getBoolean(Constant.DARK_THEME, false);
    }

    public void setDarkThemeEnabled(boolean state) {
        editor.putBoolean(Constant.DARK_THEME, state).commit();
    }
}
