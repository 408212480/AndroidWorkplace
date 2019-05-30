package qunincey.com.smartcity.utils;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MyPicassoUtils {


    public void load(String url, ImageView imageView){
        Picasso picasso=Picasso.get();
        /*
        * 开启调试模式
        * */
        picasso.setIndicatorsEnabled(true);
        /*
        * 开启日志
        * */
        picasso.setLoggingEnabled(true);
        picasso.load(url).into(imageView);
    }
}
