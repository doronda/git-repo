package doronda.app.generator;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import java.util.ArrayList;


/**
 * Created by doronda on 14.12.2015.
 */
public class GeneratorFragment extends Fragment {

    private boolean isTaskRunning = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d("d", "onCreate");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isTaskRunning) {
            GeneratorActivity.pb.setVisibility(ProgressBar.VISIBLE);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("d", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("d", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("d", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("d", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("d", "onDestroy");
    }

    @Override
    public void onDetach() {
        if (GeneratorActivity.pb.isShown()) {
            GeneratorActivity.pb.setVisibility(ProgressBar.INVISIBLE);
        }
        super.onDetach();
        Log.d("d", "onDetach");
    }

    class Generator implements Runnable {

        int start;
        int finish;
        ArrayList<Integer> arr1 = new ArrayList<Integer>() ;

        Generator(int start, int finish){
            this.start = start;
            this.finish = finish;
        }

        @Override
        public void run() {
            Log.d("d", "run");
            for (int i = start+1; i <= finish; i++) {
                if (isPrimeNum(i)) {
                    arr1.add(i);
                }
            }
            Message msg = GeneratorActivity.handler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putIntegerArrayList("ARR", arr1);
            msg.setData(bundle);
            GeneratorActivity.handler.sendMessage(msg);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (GeneratorActivity.pb.isShown()) {
                        GeneratorActivity.pb.setVisibility(ProgressBar.INVISIBLE);
                    }
                    isTaskRunning = false;
                }
            });

        }
    }

    public static boolean isPrimeNum(int n) {
        boolean prime = true;
        for (long i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i == 0) {
                prime = false;
                break;
            }
        }
        return (n % 2 != 0 && prime && n > 2) || n == 2;
    }

    public void createTreads(final int count, final int range){
        isTaskRunning = true;
        GeneratorActivity.pb.setVisibility(ProgressBar.VISIBLE);
                int inc = range/count;
                for(int y=0; y<count; y++){

                    int start = y*inc;
                    int finish = ((y+1) == count) ? range : (y+1)*inc;
                    Thread t1 = new Thread(new Generator(start, finish ));
                    t1.start();

                }

    }

}
