package doronda.app.generator;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by doronda on 15.12.2015.
 */
public class HistoryActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    SimpleCursorAdapter scAdapter;
    ListView lv;
    String[] from = new String[] { DBAdapter.COLUMN_TIME, DBAdapter.COLUMN_RANGE , DBAdapter.COLUMN_THREAD_COUNT};
    int[] to = new int[] { R.id.tv_event_date, R.id.tv_range, R.id.tv_thread_count };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        lv = (ListView)findViewById(R.id.lv_history);
        scAdapter = new SimpleCursorAdapter(this, R.layout.item, null, from, to, 0);
        DBAdapter.getInstance(getApplicationContext()).open();
        lv.setAdapter(scAdapter);
        lv.setLongClickable(true);
        //show dialog with primes for this range
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        int value = Integer.parseInt(((TextView) (view.findViewById(R.id.tv_range))).getText().toString());
                        showListDialog(DBAdapter.getInstance(getApplicationContext()).getCache(value));
                    }
                }).start();
            }
        });
        //show line chart
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                int value = Integer.parseInt(((TextView) (view.findViewById(R.id.tv_range))).getText().toString());
                Intent intent = new Intent(HistoryActivity.this, LineChartActivity.class);
                Bundle b = new Bundle();
                b.putInt("RANGE", value);
                intent.putExtras(b);
                startActivity(intent);
                return true;
            }

        });
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("act", "onDestroy");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new MyCursorLoader(this, DBAdapter.getInstance(getApplicationContext()));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void showListDialog(ArrayList<Integer> arr) {
        FragmentManager fm = getFragmentManager();
        ListDialog listDialog = new ListDialog();
        Bundle b = new Bundle();
        b.putIntegerArrayList("ARR", arr);
        listDialog.setArguments(b);
        listDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
        listDialog.show(fm, "2");
    }

    static class MyCursorLoader extends CursorLoader {

        DBAdapter db;

        public MyCursorLoader(Context context, DBAdapter db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getHistory();
            return cursor;
        }
    }
}
