package com.delaroystudios.alarmreminder;

import android.app.Application;
import android.content.Context;

public class LanguageApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageHelper.onAttach(base,"en"));
    }
}
