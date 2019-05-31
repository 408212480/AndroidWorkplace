package qunincey.com.smartcity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import qunincey.com.smartcity.MainActivity;
import qunincey.com.smartcity.R;
import qunincey.com.smartcity.domain.FragmentInfo;
import qunincey.com.smartcity.domain.NewsMenu;
import qunincey.com.smartcity.global.GlobalConstants;
import qunincey.com.smartcity.utils.CacheUtils;
import qunincey.com.smartcity.utils.OkhttpUtils;
import qunincey.com.smartcity.utils.ProcessDataUtils;
import qunincey.com.smartcity.view.TopNewsViewPager;

public class FragmentNews extends Fragment {

    private ImageButton imageButton;
    private View view;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private NewsMenu.NewsMenuData newsMenuDataArrayList;
    private List<FragmentNewsMenuDetail> mFragments;
    private TabLayout tabLayout;
    private TopNewsViewPager viewPager;
    private ArrayList<NewsMenu.NewsTabData> newsTabData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        * 当创建的时候就去找缓存
        * */
        String cache= CacheUtils.getCache(GlobalConstants.CATEGORY_URL, getActivity());
        if (cache!=null){
            /*
            * newsMenu.data.get(0)
            * */
            newsTabData=ProcessDataUtils.processDataByNewsMenu(cache,getActivity()).data.get(0).children;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_content2,container,false);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imageButton = view.findViewById(R.id.btn_menu);
        drawerLayout = getActivity().findViewById(R.id.dl_content);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }


    public void initView(){

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tab_layout);
        mFragments = new ArrayList<>();


        for (int i=0;i<newsTabData.size();i++){
            FragmentNewsMenuDetail fragmentInfo=new FragmentNewsMenuDetail();
            Bundle bundle1 =new Bundle();
            /*
            * 放id
            * */
            bundle1.putInt("id",newsTabData.get(i).getId());
            bundle1.putSerializable(newsTabData.get(i).getId()+"",newsTabData.get(i));
            fragmentInfo.setArguments(bundle1);
            mFragments.add(fragmentInfo);
            getFromServer(newsTabData.get(i).getUrl());
            tabLayout.addTab(tabLayout.newTab().setText(newsTabData.get(i).title));
        }
        PagerAdapter adapter = new
                ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void getFromServer(final String url) {

        OkhttpUtils.sendRequestWithOkhttp(GlobalConstants.SERVER_URL+url, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(), "网络不通畅", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    String responseStr = response.body().string();
                    CacheUtils.setCache(url,responseStr,getActivity());
                    System.out.println("请求获取成功");
                } else {

                }
            }
        });
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return  mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return newsMenuDataArrayList.children.get(position).title;
        }
    }


}
