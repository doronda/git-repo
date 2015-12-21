package doronda.app.generator;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class GeneratorActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    static int NUMBER_OF_CORES;
    ListView lv;
    private static int range  = 0;
    private GeneratorFragment fragment;
    private ArrayList arr;
    public static Handler handler;
    public static  ProgressBar pb;
    static EditText edtRange;
    int count = 0;
    long mStartTime;
    SimpleCursorAdapter scAdapter;
    String[] from = new String[] { DBAdapter.COLUMN_CACHE};
    int[] to = new int[] { android.R.id.text1};

    private static SimpleDateFormat timeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        DBAdapter.getInstance(getApplicationContext()).open();
        NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        arr = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list_simple_didgites);
        scAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, 0);
        lv.setAdapter(scAdapter);
        if (savedInstanceState == null) {
            fragment = new GeneratorFragment();
            getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
        }
        else {
            fragment = (GeneratorFragment)getFragmentManager().findFragmentById(android.R.id.content);
            if(savedInstanceState.containsKey("ARR")){
                arr.clear();
                arr.addAll(savedInstanceState.getIntegerArrayList("ARR"));
                set();
            }
        }

        pb = (ProgressBar) findViewById(R.id.pbLoading);
        edtRange  = (EditText) findViewById(R.id.edt_range);
        Button btnStartGeneration = (Button) findViewById(R.id.btn_generate_start);
        btnStartGeneration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startGeneration();

            }
        });

        LinearLayout touchInterceptor = (LinearLayout)findViewById(R.id.lay_generator);
        touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (edtRange.isFocused()) {
                        Rect outRect = new Rect();
                        edtRange.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {

                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            startGeneration();
                        }
                    }
                }
                return false;
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                count++;
                Bundle bundle = msg.getData();
                ArrayList<Integer> arrayList = bundle.getIntegerArrayList("ARR");
                arr.addAll(arrayList);
                Collections.sort(arr);
                if(count == EditTextDialog.threadCount){
                    long mEndTime = SystemClock.elapsedRealtime();
                    DBAdapter.getInstance(getApplicationContext()).insertHistoryItem(timeFormat.format(Calendar.getInstance().getTime()), edtRange.getText().toString(), EditTextDialog.threadCount, ((float)(mEndTime - mStartTime)/1000));
                    if( DBAdapter.getInstance(getApplicationContext()).getRange()<range){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DBAdapter.getInstance(getApplicationContext()).insertCache(arr);
                                DBAdapter.getInstance(getApplicationContext()).updateRange(range);
                            }
                        }).start();

                    }

                    set();
                    if (GeneratorActivity.pb.isShown()) {
                        GeneratorActivity.pb.setVisibility(ProgressBar.INVISIBLE);
                    }
                    GeneratorFragment.isTaskRunning = false;
                }
            }
        };
        getSupportLoaderManager().initLoader(0, null, this);
    }
    // Start primes generation
    public void startGeneration(){
        if(edtRange.getText().toString().length() == 0){
            Toast.makeText(getBaseContext(), "Enter the value", Toast.LENGTH_SHORT).show();
            return;
        } else
        try {
            if (Integer.parseInt(edtRange.getText().toString()) < 0 || Integer.parseInt(edtRange.getText().toString()) > Integer.MAX_VALUE) {
                Toast.makeText(getBaseContext(), "Enter the value from 0 to " + String.valueOf(Integer.MAX_VALUE), Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException ne){
            Toast.makeText(getBaseContext(), "Enter the value from 0 to " + String.valueOf(Integer.MAX_VALUE), Toast.LENGTH_SHORT).show();
            return;
        }
        edtRange.clearFocus();
        hideKeyboard();
        arr.clear();
        count=0;
        range = Integer.valueOf(edtRange.getText().toString());
        if( (DBAdapter.getInstance(getApplicationContext()).getRange() >= range) && DBAdapter.getInstance(getApplicationContext()).getThreads(range).contains(EditTextDialog.threadCount)){
            // if range <= max range in DB (cache) then load primes from DB
            DBAdapter.getInstance(getApplicationContext()).insertHistoryItem(timeFormat.format(Calendar.getInstance().getTime()), edtRange.getText().toString(), EditTextDialog.threadCount, 0);
            Toast.makeText(getBaseContext(), "From chache", Toast.LENGTH_SHORT).show();
new Thread(new Runnable() {
    @Override
    public void run() {
        set();
    }
}).start();

        } else {
            mStartTime = SystemClock.elapsedRealtime();
            fragment.createTreads(EditTextDialog.threadCount, range);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                if (!pb.isShown()) {
                    showEditDialog();
                }
                return true;

            case R.id.item2:
                if (!pb.isShown()) {
                    Intent intent = new Intent(this, HistoryActivity.class);
                    startActivity(intent);
                }

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }

    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        EditTextDialog editNameDialog = new EditTextDialog();
        editNameDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
        editNameDialog.show(fm, "1");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("act", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Log.d("act", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("act", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("act", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("act", "onDestroy");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putIntegerArrayList("ARR", arr);

    }
    //show primes
    public void set(){

        getSupportLoaderManager().getLoader(0).forceLoad();

    }

    public void hideKeyboard() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CacheCursorLoader(this, DBAdapter.getInstance(getApplicationContext()));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        scAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    static class CacheCursorLoader extends CursorLoader {

        DBAdapter db;

        public CacheCursorLoader(Context context, DBAdapter db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getCacheCur(range);
            return cursor;
        }
    }
}


