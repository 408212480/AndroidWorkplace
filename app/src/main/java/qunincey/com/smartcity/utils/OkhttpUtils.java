package qunincey.com.smartcity.utils;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import qunincey.com.smartcity.MainActivity;
import qunincey.com.smartcity.domain.NewsMenu;
import qunincey.com.smartcity.global.GlobalConstants;

public class OkhttpUtils {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    public static void sendRequestWithOkhttp(String address,okhttp3.Callback callback){

        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    /*
    * 第一个异步缓存,获取菜单
    * */
    public static void getNewsMenu(){
        /*
         * 主线程不能开网络请求  会阻塞  用异步的
         *
         * */
        OkhttpUtils.sendRequestWithOkhttp(GlobalConstants.SERVER_URL+"/categories.json", new Callback() {



            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();

                } else {
                }
            }
        });
    }


}
