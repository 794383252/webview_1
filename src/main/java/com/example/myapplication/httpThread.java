package com.example.myapplication;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/7/16.
 */
public class httpThread extends Thread {

    private String mUrl;

    public httpThread(String url) {
        this.mUrl = url;
    }

    @Override
    public void run() {
        try {

            Log.i("ln","开始下载");

            URL httpUrl = new URL(mUrl);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
//            conn.setDoOutput(true);
            InputStream in = conn.getInputStream();
            File downLoadFile;
            File sdFile;
            FileOutputStream out = null;
            //判断id卡是否挂载
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) ;
            {
                downLoadFile = Environment.getExternalStorageDirectory();
                sdFile = new File(downLoadFile, "test.apk");
                out = new FileOutputStream(sdFile);
            }

            byte[] b = new byte[6 * 1024];
            int len;
            while ((len = in.read(b)) != -1) {
                if (out != null) {
                    out.write(b, 0, len);
                }
            }

            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }

            Log.i("ln","下载结束");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
