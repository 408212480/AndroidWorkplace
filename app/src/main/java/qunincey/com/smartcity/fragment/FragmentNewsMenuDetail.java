package qunincey.com.smartcity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import qunincey.com.smartcity.MainActivity;
import qunincey.com.smartcity.R;
import qunincey.com.smartcity.base.impl.menu.TabDetailPager;
import qunincey.com.smartcity.domain.NewsMenu;
import qunincey.com.smartcity.domain.NewsTabBean;
import qunincey.com.smartcity.global.GlobalConstants;
import qunincey.com.smartcity.utils.CacheUtils;
import qunincey.com.smartcity.utils.OkhttpUtils;

public class FragmentNewsMenuDetail extends Fragment {

    private ArrayList<ImageView> imageViewsList;

    private Integer[] mImagesIds=new Integer[]{R.drawable.topnews_item_default};



    private View view;
    private String title;
    private TextView textView;

    private ArrayList<NewsMenu.NewsTabData> arrayList;

    private NewsMenu.NewsTabData menu;
    private int bundleInt;
    private String url;
    private Bundle bundle;
    private ViewPager viewPager;
    private NewsTabBean newsTabBean;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu_detail,container,false);
        viewPager = view.findViewById(R.id.view_pager);
        bundle = getArguments();
        initImageData();
        initData();
        getDataFromServer();
        return view;
    }

    private void initData() {
        bundleInt = bundle.getInt("id");
        menu = (NewsMenu.NewsTabData) bundle.get(bundleInt+"");
        url = menu.getUrl();
        viewPager.setAdapter(new imageAdpter());


    }
    /*初始化view*/
    private void initImageData(){
        imageViewsList=new ArrayList<>();
        for (int i=0;i<mImagesIds.length;i++){
            ImageView view=new ImageView(getActivity());
            view.setBackgroundResource(mImagesIds[i]);
            /*维护view*/
            imageViewsList.add(view);
        }

    }


    public void getDataFromServer() {

        /*
         * 主线程不能开网络请求  会阻塞  用异步的
         *
         * */
        OkhttpUtils.sendRequestWithOkhttp(GlobalConstants.SERVER_URL+url, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(), "网络不通畅", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    newsTabBean = processNewsData(responseStr);
                } else {
                    Toast.makeText(getActivity(), "服务器错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private NewsTabBean processNewsData(String responseStr) {

        Gson gson=new Gson();
        NewsTabBean newsTabBean=gson.fromJson(responseStr, NewsTabBean.class);
        return newsTabBean;
    }

    class imageAdpter extends PagerAdapter{

        /*item总的个数*/
        @Override
        public int getCount() {
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView=imageViewsList.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
