package com.dev.counterthread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    int counter = 0;
    TextView counterTextView;
    Button incrementBtn, decrementBtn, stopBtn, resetBtn;

    IncrementAsyncTask incrementAsyncTask;
    DecrementAsyncTask decrementAsyncTask;

    boolean isNotStoped = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setListener();
    }

    private void initViews() {
        counterTextView = findViewById(R.id.counter);
        counterTextView.setText(Integer.toString(counter));
        incrementBtn = findViewById(R.id.increment);
        decrementBtn = findViewById(R.id.decrement);
        resetBtn = findViewById(R.id.reset);
        stopBtn = findViewById(R.id.stop);
    }

    private void setListener() {
        incrementBtn.setOnClickListener(this);
        decrementBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.increment:
                isNotStoped = true;
                incrementAsyncTask = new IncrementAsyncTask();
                incrementAsyncTask.execute();
                break;
            case R.id.decrement:
                isNotStoped = true;
                decrementAsyncTask = new DecrementAsyncTask();
                decrementAsyncTask.execute();
                break;
            case R.id.stop:
                stopAsyncTasks();
                break;
            case R.id.reset:
                stopAsyncTasks();
                counter = 0;
                counterTextView.setText(Integer.toString(counter));
                break;
            default: {
            }
        }
    }

    private void stopAsyncTasks() {
        if (incrementAsyncTask != null && incrementAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            incrementAsyncTask.cancel(true);
            isNotStoped = false;
        }
        if (decrementAsyncTask != null && decrementAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            decrementAsyncTask.cancel(true);
            isNotStoped = false;
        }
    }

    class IncrementAsyncTask extends AsyncTask<Integer, Integer, String> {

        int initalCounter;

        @Override
        protected void onPreExecute() {
            initalCounter = counter;
            Log.e(TAG, "Préparation de l'asynctask ...");
            counterTextView.setText("Préparation de l'asynctask...");
        }

        @Override
        protected String doInBackground(Integer... integers) {
            Log.e(TAG, "Thread : " + Thread.currentThread().getName());

            while (isNotStoped) {
                SystemClock.sleep(1000);
                counter++;
                if (counter == initalCounter + 60)
                    break;
                Log.e("TAG", counter + " ...");
                publishProgress(counter);
            }

            return "Increment Thread achevé !";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            counterTextView.setText(Integer.toString(values[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            counterTextView.setText(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.e("TAG", "Increment Thread arreté !!");
        }
    }

    class DecrementAsyncTask extends AsyncTask<Integer, Integer, String> {

        int initalCounter;

        @Override
        protected void onPreExecute() {
            initalCounter = counter;
            Log.e(TAG, "Préparation de l'asynctask ...");
            counterTextView.setText("Préparation de l'asynctask...");
        }

        @Override
        protected String doInBackground(Integer... integers) {
            Log.e(TAG, "Thread : " + Thread.currentThread().getName());

            while (isNotStoped) {
                SystemClock.sleep(1000);
                counter--;
                if (counter == initalCounter - 60)
                    break;
                Log.e("TAG", counter + " ...");
                publishProgress(counter);
            }

            return "Decrement Thread achevé !";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            counterTextView.setText(Integer.toString(values[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            counterTextView.setText(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.e("TAG", "Decrement Thread arreté !!");
        }
    }
}
