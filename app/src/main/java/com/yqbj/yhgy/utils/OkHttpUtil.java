package com.yqbj.yhgy.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.yqbj.yhgy.MyApplication;
import com.yqbj.yhgy.config.Constants;

import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by think on 2016/11/1.
 */

public class OkHttpUtil {

    private static OkHttpClient client = MyApplication.getInstance().getOkHttpClinetInstance();


    private static  String sdPath  = Environment.getExternalStorageDirectory().getAbsolutePath();
    public  static String CACHE_DIR;

    private static File updateFile;
    public static void get(final Activity activity, String url, final MyListener listener){



        final Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        listener.onStart();



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call,final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailure(call,e);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onResponse(call,response);
            }
        });


    }



    public static void post(final Activity activity, String url, RequestBody requestBody, final MyListener listener){



        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        listener.onStart();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFailure(call,e);
                    }
                });


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onResponse(call,response);
            }
        });







    }

    public static void downUpdateFile(final Activity activity, String  url, final ProgressListener progressListener, final Dialog dialog){

        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {

            CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()+"/" + Constants.APP_NAME + "/";
        } else {

            CACHE_DIR = Environment.getRootDirectory().getAbsolutePath()+"/" + Constants.APP_NAME + "/";
        }


        File file = new File(CACHE_DIR);
        if(!file.exists()){
            file.mkdir();
        }


        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        };

       OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .build();


        //封装请求
        Request request = new Request.Builder().url(url).build();
        //发送异步请求
        if (null == dialog && !dialog.isShowing()){
            dialog.show();
        }
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call,final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Toast.makeText(activity,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //将返回结果转化为流，并写入文件
                int len;
                byte[] buf = new byte[2048];
                InputStream inputStream = response.body().byteStream();
                //可以在这里自定义路径
                File file1 = new File(CACHE_DIR+ Constants.APP_NAME + ".apk");
                FileOutputStream fileOutputStream = new FileOutputStream(file1);
                while ((len = inputStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, len);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            }
        });

    }


    public static void updateApk(final Activity activity, final ProgressBar progressBar, String url, final Dialog dialog){
        OkHttpUtil.downUpdateFile(activity, url,  new OkHttpUtil.ProgressListener() {
            @Override
            public void update(final long bytesRead, final long contentLength,final boolean done) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress((int) (bytesRead*100/contentLength));
                        File file = new File(OkHttpUtil.CACHE_DIR+ Constants.APP_NAME + ".apk");
                        if(done) {
                            dialog.dismiss();
                            File apkFile = new File(OkHttpUtil.CACHE_DIR+ Constants.APP_NAME + ".apk");
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            //放在此处
                            //由于没有在Activity环境下启动Activity,所以设置下面的标签
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            Uri apkUri = null;
                            //判断版本是否是 7.0 及 7.0 以上
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                apkUri = FileProvider.getUriForFile(activity, "com.yqbj.yhgy.fileProvider", apkFile);
                                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } else {
                                apkUri = Uri.fromFile(apkFile);
                            }

                            intent.setDataAndType(apkUri,
                                    "application/vnd.android.package-archive");
                            activity.startActivity(intent);
                        }
                    }
                });
            }
        },dialog);
    }

    public static void post(String url, RequestBody requestBody, final MyListener listener){
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        listener.onStart();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {



            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onResponse(call,response);
            }
        });


    }







    public static void post( String url, JSONObject json,final MyListener listener){

        RequestBody body = new FormBody.Builder().add("data",json.toString()).build();
        listener.onStart();
        Request request = new Request.Builder().post(body).url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onResponse(call,response);
            }
        });

    }

    public static void downFile(final Activity activity, String url,String fileName, final ProgressListener progressListener){

        File directory=Environment.getExternalStorageDirectory();
        final File file = new File(directory,fileName);




        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        };

        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .build();


        //封装请求
        Request request = new Request.Builder().url(url).build();
        //发送异步请求
//        progressDialog.show();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call,final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        progressDialog.dismiss();
                        Toast.makeText(activity,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //将返回结果转化为流，并写入文件
                int len;
                byte[] buf = new byte[2048];
                InputStream inputStream = response.body().byteStream();
                //可以在这里自定义路径

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                while ((len = inputStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, len);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            }
        });

    }

    public static void download(final Context context, String url, final MyDownListener myListener){
        final String imgName = url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("?"));


        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File fileApp =null;
            fileApp = new File(sdPath+"/" +Constants.APP_NAME+ "/");
            if(!fileApp.exists()){

                fileApp.mkdir();
            }
           final File  file = new File(sdPath+"/" +Constants.APP_NAME+ "/"+imgName);
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Request request = new Request.Builder().url(url).build();


            myListener.onStart();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    myListener.onFailure(call,e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {


                    InputStream inputStream =null;
                    byte[] buf= new byte[2048];
                    FileOutputStream fos = null;
                    int len=0;

                    inputStream = response.body().byteStream();

                    fos = new FileOutputStream(file);

                    while((len=inputStream.read(buf))!=-1){
                        fos.write(buf,0,len);

                    }
                    fos.flush();
                    inputStream.close();
                    fos.close();

                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" +file.getAbsolutePath())));



                    myListener.onResponse(call,response,file);



                }
            });



        }else{

            Toast.makeText(context,"内存卡未准备",Toast.LENGTH_SHORT).show();


        }



    }
    /**
     * 添加进度监听的ResponseBody
     */
    private static class ProgressResponseBody extends ResponseBody {
        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override public MediaType contentType() {
            return responseBody.contentType();
        }


        @Override public long contentLength() {
            return responseBody.contentLength();
        }

        @Override public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(final Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;

                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }


    public interface ProgressListener {
        /**
         * @param bytesRead     已下载字节数
         * @param contentLength 总字节数
         * @param done          是否下载完成
         */
        void update(long bytesRead, long contentLength, boolean done);
    }






    public interface MyListener {

        public void onFailure(Call call, IOException e);

        public void onResponse(Call call, Response response) throws IOException;
        public void onStart();

    }

    public interface MyLockListener{
        public void onFailure(Call call, IOException e);
        public void onResponse(Call call, Response response, String strBody) throws IOException;
        public void onStart();
    }
    public interface MyDownListener {

        public void onFailure(Call call, IOException e);
        public void onResponse(Call call, Response response, File file) throws IOException;
        public void onStart();

    }


}
