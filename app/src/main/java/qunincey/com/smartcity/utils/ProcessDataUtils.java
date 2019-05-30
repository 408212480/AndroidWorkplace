package qunincey.com.smartcity.utils;

import android.content.Context;

import com.google.gson.Gson;

import qunincey.com.smartcity.MainActivity;
import qunincey.com.smartcity.domain.NewsMenu;
import qunincey.com.smartcity.global.GlobalConstants;

public class ProcessDataUtils {


    private static Gson gson = new Gson();;

    public static NewsMenu processDataByNewsMenu(String json, Context context){
        String cache= CacheUtils.getCache(GlobalConstants.CATEGORY_URL,context);
        if (cache!=null){
           json = cache;
        }else {
            CacheUtils.setCache(GlobalConstants.CATEGORY_URL,json,context );
        }
        return gson.fromJson(json,NewsMenu.class);
    }
}
