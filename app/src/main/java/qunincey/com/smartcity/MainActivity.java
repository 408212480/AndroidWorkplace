package qunincey.com.smartcity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import qunincey.com.smartcity.domain.NewsMenu;
import qunincey.com.smartcity.fragment.FragmentGov;
import qunincey.com.smartcity.fragment.FragmentHome;
import qunincey.com.smartcity.fragment.FragmentNews;
import qunincey.com.smartcity.fragment.FragmentSetting;
import qunincey.com.smartcity.fragment.FragmentSmart;
import qunincey.com.smartcity.global.GlobalConstants;
import qunincey.com.smartcity.utils.CacheUtils;
import qunincey.com.smartcity.utils.OkhttpUtils;
import qunincey.com.smartcity.utils.TitleAdpater;
import qunincey.com.smartcity.view.NoScrollViewPager;

public class MainActivity extends AppCompatActivity {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    private TextView mTextMessage;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabItem tihome;
    private TabItem tinews;
    private TabItem tigov;
    private TabItem tismart;
    private TabItem tisetting;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ListView listView;

    private NewsMenu newsMenu;

    private static List<Fragment> mFragmentHashMap=new ArrayList<>();

    private static final List<String> classifiedNesTitleString= Arrays.asList("首页","新闻","政务","服务","设置");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager =  findViewById(R.id.viewPager);
        navigationView = findViewById(R.id.nav_view);

        tihome = findViewById(R.id.ti_home);
        tinews = findViewById(R.id.ti_news);
        tigov = findViewById(R.id.ti_gov);
        tismart = findViewById(R.id.ti_smart);
        tisetting = findViewById(R.id.ti_setting);
        listView = findViewById(R.id.lv_nv);

        drawerLayout = findViewById(R.id.dl_content);

        initView();
        tabLayout =  findViewById(R.id.tabLayout);

        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount()));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.home_press);
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                       break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.newscenter_press);
                        String cache= CacheUtils.getCache(GlobalConstants.CATEGORY_URL,MainActivity.this);
                        if (cache!=null){
                            newsMenu=processData(cache);
                        }
                        getDataFromServer();
                        listinit(newsMenu);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.smartservice_press);
                        break;
                    case 3:
                        tabLayout.getTabAt(3).setIcon(R.drawable.govaffairs_press);
                        break;
                    case 4:
                        tabLayout.getTabAt(4).setIcon(R.drawable.setting_press);
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        break;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){

                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.home);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.newscenter);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.smartservice);
                        break;
                    case 3:
                        tabLayout.getTabAt(3).setIcon(R.drawable.govaffairs);
                        break;
                    case 4:
                        tabLayout.getTabAt(4).setIcon(R.drawable.setting);
                        break;

                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }

    private void initView() {
        Fragment fragment1=new FragmentHome();
        Fragment fragment2=new FragmentNews();

        /*
        * bundle传参
        *
        * */
        Bundle bundle=new Bundle();
        String cache= CacheUtils.getCache(GlobalConstants.CATEGORY_URL,MainActivity.this);
        if (cache!=null){
            System.out.println("发现缓存啦");
            newsMenu=processData(cache);
            bundle.putSerializable("news_menu",newsMenu.data.get(0));
            fragment2.setArguments(bundle);
        }
        getDataFromServer();
        Fragment fragment3=new FragmentGov();
        Fragment fragment4=new FragmentSmart();
        Fragment fragment5=new FragmentSetting();

        mFragmentHashMap.add(0,fragment1);
        mFragmentHashMap.add(1,fragment2);
        mFragmentHashMap.add(2,fragment3);
        mFragmentHashMap.add(3,fragment4);
        mFragmentHashMap.add(4,fragment5);

        Log.i("i","填充成功");



    }

    private void listinit(NewsMenu newsMenu) {
        TitleAdpater titleAdpater=new TitleAdpater(newsMenu,MainActivity.this);
        listView.setAdapter(titleAdpater);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                switch (position){
                    case 1:
                        Toast.makeText(MainActivity.this,"item1",Toast.LENGTH_SHORT).show();
                        Fragment fragment2=new FragmentHome();
                        mFragmentHashMap.set(1,fragment2);
                        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount()));
                }

            }
        });
    }



    public void getDataFromServer(){

        /*
         * 主线程不能开网络请求  会阻塞  用异步的
         *
         * */
        OkhttpUtils.sendRequestWithOkhttp(GlobalConstants.SERVER_URL+"/categories.json", new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this,"网络不通畅",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    System.out.println(responseStr);
                    newsMenu=processData(responseStr);
                    CacheUtils.setCache(GlobalConstants.CATEGORY_URL,responseStr,MainActivity.this);
                } else {
//                    Toast.makeText(MainActivity.this,"服务器错误",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
    protected NewsMenu processData(String json){

        Gson gson=new Gson();
        NewsMenu newsMenu=gson.fromJson(json,NewsMenu.class);
        return newsMenu;
    }


    public class PageAdapter extends FragmentStatePagerAdapter {

        private int num;


        public PageAdapter(FragmentManager fm,int num) {
            super(fm);
            this.num=num;
        }

        @Override
        public Fragment getItem(int i) {

            return createF(i);
        }

        @Override
        public int getCount() {
            return num;
        }

        private Fragment createF(int pos){
            Fragment fragment=mFragmentHashMap.get(pos);

            if (fragment == null){

                switch (pos){

                    case 0:
                        fragment = new FragmentHome();
                        Log.i("fragment","Fragment1");
                        break;
                    case 1:
                        fragment=new FragmentNews();
                        Log.i("fragment","Fragment2");
                        break;
                    case 2:
                        fragment=new FragmentGov();
                        Log.i("fragment","Fragment3");
                        break;
                    case 3:
                        fragment=new FragmentSmart();
                        Log.i("fragment","Fragment4");
                        break;
                    case 4:
                        fragment=new FragmentSetting();
                        Log.i("fragment","Fragment5");
                        break;
                        default:
                            return null;


                }
                mFragmentHashMap.add(pos,fragment);
            }
            return fragment;
        }
    }
}
