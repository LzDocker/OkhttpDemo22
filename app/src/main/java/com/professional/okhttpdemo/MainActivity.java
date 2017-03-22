package com.professional.okhttpdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    ImageView iv_star;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_star = (ImageView) findViewById(R.id.iv_star);
    }


    public void get(View view){

        OkHttpClient mOkHttpClient=new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder().url("http://www.baidu.com");
        //可以省略，默认是GET请求
        requestBuilder.method("GET",null);
        Request request = requestBuilder.build();
        Call mcall= mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str;
                if (null != response.cacheResponse()) {
                    str = response.cacheResponse().toString();
                    Log.i("wangshu", "cache---" + str);
                } else {
                    response.body().string();
                     str = response.networkResponse().toString();
                    Log.i("wangshu", "network---" + str);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "请求成功"+str, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }




        public void postAsynHttp(View view) {
            OkHttpClient mOkHttpClient=new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("uploadFile", "100064")
                    .add("userid","10064")
                    .build();
            Request request = new Request.Builder()
                    .url("http://server.jeasonlzy.com/OkHttpUtils/")
                    .post(formBody)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String str = response.body().string();
                    Log.i("wangshu", str);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "请求成功"+str, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            });
        }


    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("image/jpeg");

    public void postAsynFile(View v) throws IOException {


        OkHttpClient mOkHttpClient=new OkHttpClient();
        File file = new File(Environment.getExternalStorageDirectory()+"/crop_file.jpg");
        if(file.exists()){
            Log.i("wangshu","存在");


        }else {
            Log.i("wangshu","不存在");

           return;
        }

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "test.jpg", RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();
        Request request = new Request.Builder()
                .url("http://server.jeasonlzy.com/OkHttpUtils/upload")
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("wangshu",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("wangshu",response.body().string());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    public void downAsynFile(View view) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        String url = "http://server.jeasonlzy.com/OkHttpUtils/image";
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(new File("/sdcard/wangshu.jpg"));
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();
                } catch (IOException e) {
                    Log.i("wangshu", "IOException");
                    e.printStackTrace();
                }

                Log.d("wangshu", "文件下载成功");

                final Bitmap bitmap= BitmapFactory.decodeFile("/sdcard/wangshu.jpg");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_star.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }


}
