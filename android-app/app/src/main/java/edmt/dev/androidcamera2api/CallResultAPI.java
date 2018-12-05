package edmt.dev.androidcamera2api;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 */



public class CallResultAPI {

    ResultInterface resultInterface;
    private static final OkHttpClient client = new OkHttpClient();
    private static final String URL = "http://ec2-54-208-102-107.compute-1.amazonaws.com:8090/";

    public static void getResult(final File sourceImageFile, final Handler handler) {

        final MediaType MEDIA_TYPE = MediaType.parse("image/jpeg");


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "temp.jpg", RequestBody.create(MEDIA_TYPE, sourceImageFile))
                .build();

        Request request = new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();

        /*RequestBody formBody = null;
        try {
            formBody = new FormBody.Builder()
                    .add("image", new String(bytes, "UTF-8"))
                    .build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        };
        Request request = new Request.Builder()
                .url(URL)
                .post(formBody)
                .build();*/

        final Bundle bundle = new Bundle();
        final Message message = new Message();
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                bundle.putSerializable("result", null);
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                bundle.putSerializable("result", body);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        });
    }
}
