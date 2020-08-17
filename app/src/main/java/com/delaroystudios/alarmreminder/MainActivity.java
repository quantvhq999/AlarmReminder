package com.delaroystudios.alarmreminder;

import android.animation.Animator;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.icu.util.LocaleData;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delaroystudios.alarmreminder.data.AlarmReminderContract;
import com.delaroystudios.alarmreminder.data.AlarmReminderDbHelper;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FloatingActionButton mAddReminderButton;
    private FloatingActionButton mAlarmButton;



    private Toolbar mToolbar;
    AlarmCursorAdapter mCursorAdapter;
    RelativeLayout layoutAlarm;
    RelativeLayout layoutMain;
    RelativeLayout layoutContent;

    AlarmReminderDbHelper alarmReminderDbHelper = new AlarmReminderDbHelper(this);
    ListView reminderListView;
    ProgressDialog prgDialog;

    TextView textView;
    private static final int VEHICLE_LOADER = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.onAttach(newBase,"en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        textView = (TextView) findViewById(R.id.no_reminder);
        Paper.init(this);
        String language = Paper.book().read("language");
        if (language == null){
            Paper.book().write("language","vi");
        }
        updateView((String)Paper.book().read("language"));
        reminderListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        reminderListView.setEmptyView(emptyView);

        mAlarmButton = (FloatingActionButton) findViewById(R.id.fab2);

        mCursorAdapter = new AlarmCursorAdapter(this, null);
        reminderListView.setAdapter(mCursorAdapter);

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, AddReminderActivity.class);

                Uri currentVehicleUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentVehicleUri);

                startActivity(intent);

            }
        });


        mAddReminderButton = (FloatingActionButton) findViewById(R.id.fab);

        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddReminderActivity.class));
            }
        });
        getLoaderManager().initLoader(VEHICLE_LOADER, null, this);

        //Alarm view
        mAlarmButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AlarmActivity.class));
            }
        });

    }

    //Update view
    private void updateView(String language) {
        Context context = LanguageHelper.setLocate(this,language);
        Resources resources = context.getResources();

        textView.setText(resources.getString(R.string.hello));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_setting,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      if(item.getItemId() == R.id.lang_en){
          Paper.book().read("language","en");
          updateView((String)Paper.book().read("language"));
      }else if(item.getItemId() == R.id.lang_vi){
          Paper.book().read("language","vi");
          updateView((String)Paper.book().read("language"));
      }
      return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE

        };

        return new CursorLoader(this,   // Parent activity context
                AlarmReminderContract.AlarmReminderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }
}
