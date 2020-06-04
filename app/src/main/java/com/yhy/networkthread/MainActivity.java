package com.yhy.networkthread;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button showTip;
    private Button BuildThread;
    private Button openWebPage;
    private Thread thread;
    private ProgressBar mProgressBar;
    private ImageView[] mImageViews = new ImageView[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button showTip = findViewById(R.id.buttonShowTip);
        Button BuildThread = findViewById(R.id.buttonStartThread);
        Button openWebPage = findViewById(R.id.buttonWebPage);

        mProgressBar = findViewById(R.id.progressBar);
        mImageViews[0] = findViewById(R.id.imageView1);
        mImageViews[1] = findViewById(R.id.imageView2);
        mImageViews[2] = findViewById(R.id.imageView3);
        mImageViews[3] = findViewById(R.id.imageView4);
        mImageViews[4] = findViewById(R.id.imageView5);
        mImageViews[5] = findViewById(R.id.imageView6);
        mImageViews[6] = findViewById(R.id.imageView7);
        mImageViews[7] = findViewById(R.id.imageView8);
        mImageViews[8] = findViewById(R.id.imageView9);

        showTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "显示界面，程序正常运行", Snackbar.LENGTH_SHORT).show();
            }
        });
        BuildThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("me", "sleep");
                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }

        });

        openWebPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpAsyncTask asyncTask = new HttpAsyncTask(9);
                Log.i("Main","创建异步任务");
                asyncTask.execute(
                        "http://www.tucoo.com/photo/water_02/s/water_05102s.jpg",
                        "http://www.tucoo.com/photo/water_02/s/water_05203s.jpg",
                        "http://www.tucoo.com/photo/water_02/s/water_05304s.jpg",
                        "http://www.tucoo.com/photo/water_02/s/water_05405s.jpg",
                        "http://www.tucoo.com/photo/water_02/s/water_05506s.jpg",
                        "http://www.tucoo.com/photo/water_02/s/water_05607s.jpg",
                        "http://www.tucoo.com/photo/water_02/s/water_05708s.jpg",
                        "http://www.tucoo.com/photo/water_02/s/water_05809s.jpg",
                        "http://www.tucoo.com/photo/water_02/s/water_05910s.jpg"

                );
            }
        });

    }

    class HttpAsyncTask  extends AsyncTask<String,Integer, Bitmap[]> {
        private int taskNum=0;

        public HttpAsyncTask(int taskNum) {
            this.taskNum = taskNum;
        }

        public HttpAsyncTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setMax(taskNum);//给进度条设置最大值
        }

        @Override
        //该方法可以进行耗时操作
        protected Bitmap[] doInBackground(String... strings) {
            Bitmap[] bitmaps = new Bitmap[strings.length];
            for (int i=0; i<strings.length;i++) {
                try {
                    URL urlobj = new URL(strings[i]);
                    HttpURLConnection connection = (HttpURLConnection) urlobj.openConnection();
                    //该步是主要耗时操作
                    connection.connect();//进行连接
                    InputStream is = connection.getInputStream();
                    //读出数据并解码出位图
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    Log.i("task", strings[i]);
                    bitmaps[i] = bitmap;
                    publishProgress(i+1);//通知进度条更新进程
                } catch (IOException e) {
                    Log.i("doInBackground", "连接失败");
                    e.printStackTrace();
                    return null;
                }
            }
            return bitmaps;
        }

        @Override
        //ui线程中执行
        protected void onPostExecute(Bitmap bitmaps[] ) {
            if (bitmaps == null) {
                return;
            }
            for (int i = 0; i < mImageViews.length; i++) {
                mImageViews[i].setImageBitmap(bitmaps[i]);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0].intValue());
        }
    }

}