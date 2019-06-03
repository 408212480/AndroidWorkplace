package qunincey.com.smartcity.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import qunincey.com.smartcity.R;
import qunincey.com.smartcity.SplashActivity;
import qunincey.com.smartcity.domain.NewsMenu;
import qunincey.com.smartcity.domain.PhotosBean;
import qunincey.com.smartcity.global.GlobalConstants;
import qunincey.com.smartcity.utils.CacheUtils;
import qunincey.com.smartcity.utils.MyBitmapUtils;
import qunincey.com.smartcity.utils.MyPicassoUtils;
import qunincey.com.smartcity.utils.OkhttpUtils;
import qunincey.com.smartcity.view.TopNewsViewPager;

public class FragmentPhotosMenuDeatil extends Fragment {

    private ImageButton imageButton;
    private View view;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private NewsMenu.NewsMenuData newsMenuDataArrayList;
    private List<FragmentNewsMenuDetail> mFragments;
    private TabLayout tabLayout;
    private TopNewsViewPager viewPager;
    private ListView listView;
    private GridView gridView;
    private ArrayList<PhotosBean.PhotoNews> mNewsList;
    private String cache;

    public static final int UPDATE_TEXT = 1;

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_TEXT:
                    System.out.println("按钮点击");
                    initView();
                    break;


            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.pager_photos_menu_detail,container,false);
        getDataFormServer();
        return view;
    }

    private void processData(String result) {
        Gson gson = new Gson();
        PhotosBean photosBean=gson.fromJson(result, PhotosBean.class);

        mNewsList = photosBean.data.news;

    }

    class PhotoAdapter extends BaseAdapter{

        private MyPicassoUtils myPicassoUtils;

        public PhotoAdapter(){
            myPicassoUtils = new MyPicassoUtils();
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null){
                convertView = View.inflate(getActivity(),R.layout.list_item_photos,null);
                holder=  new ViewHolder();
                holder.ivPic = convertView.findViewById(R.id.iv_pic);
                holder.tvTitle = convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            PhotosBean.PhotoNews item= (PhotosBean.PhotoNews) getItem(position);

            holder.tvTitle.setText(item.title);
            myPicassoUtils.load(GlobalConstants.SERVER_URL+item.listimage.substring(25),holder.ivPic);
            return convertView;
        }
    }

    static class ViewHolder{

        public ImageView ivPic;
        public TextView tvTitle;
    }

    public void initView(){
        listView = view.findViewById(R.id.lv_photo);
        gridView = view.findViewById(R.id.gv_photo);

        imageButton = view.findViewById(R.id.btn_menu);
        drawerLayout = getActivity().findViewById(R.id.dl_content);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        listView.setAdapter(new PhotoAdapter());
    }


    public void getDataFormServer(){
        OkhttpUtils.sendRequestWithOkhttp(GlobalConstants.PHOTOS_URL, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(), "网络不通畅", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    String result = response.body().string();
                    CacheUtils.setCache(GlobalConstants.PHOTOS_URL,result,getActivity());
                    processData(result);
                    Message message=new Message();
                    message.what = UPDATE_TEXT;
                    handler.sendMessage(message);
                } else {
//                    Toast.makeText(MainActivity.this,"服务器错误",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}
