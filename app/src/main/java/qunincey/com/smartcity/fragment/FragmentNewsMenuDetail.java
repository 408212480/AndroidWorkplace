package qunincey.com.smartcity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.IOException;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import qunincey.com.smartcity.MainActivity;
import qunincey.com.smartcity.NewsDetailActivity;
import qunincey.com.smartcity.R;
import qunincey.com.smartcity.base.impl.menu.TabDetailPager;
import qunincey.com.smartcity.domain.NewsMenu;
import qunincey.com.smartcity.domain.NewsTabBean;
import qunincey.com.smartcity.global.GlobalConstants;
import qunincey.com.smartcity.utils.CacheUtils;
import qunincey.com.smartcity.utils.MyBitmapUtils;
import qunincey.com.smartcity.utils.OkhttpUtils;
import qunincey.com.smartcity.utils.OnItemTouchListener;
import qunincey.com.smartcity.utils.PrefUtils;
import qunincey.com.smartcity.utils.SpacesItemDecoration;
import qunincey.com.smartcity.utils.onLoadMoreListener;

public class FragmentNewsMenuDetail extends Fragment {

    private ArrayList<ImageView> imageViewsList;

    private Integer[] mImagesIds=new Integer[]{R.drawable.topnews_item_default};



    private View view;

    private NewsMenu.NewsTabData menu;
    private int bundleInt;
    private Bundle bundle;
    private ViewPager viewPager;
    private NewsTabBean newsTabBean;
    private TextView textView;
    private CirclePageIndicator circleIndicator;
    private RecyclerView recyclerView;
    private MyBitmapUtils myBitmapUtils;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private Handler handler;
    private ArrayList<NewsTabBean.NewsData> arrayNewsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        bundleInt = bundle.getInt("id");
        menu = (NewsMenu.NewsTabData) bundle.getSerializable(bundleInt+"");
        if (menu !=null){
            String cache= CacheUtils.getCache(menu.getUrl(),getActivity());
            if (cache!=null){
                newsTabBean=processNewsData(cache);
            }
        }else {
            System.out.println("空了");
        }
        getDataFromServer();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu_detail,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        handler = new Handler();
        myBitmapUtils = new MyBitmapUtils();

        recyclerView = view.findViewById(R.id.rv_list);
        /*
         * 指定布局方式  recycview有多种布局方式
         * */
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        NewsApdatar newsApdatar=new NewsApdatar(newsTabBean.getData().news);
        recyclerView.setAdapter(newsApdatar);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);

        arrayNewsList = newsTabBean.data.news;


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                System.out.println("耗时操作");
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView.addOnScrollListener(new onLoadMoreListener(){

            @Override
            protected void onLoading(int countItem, int lastItem) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("i", "run: ");
                    }
                },3000);
            }
        });

        recyclerView.addOnItemTouchListener(new OnItemTouchListener(getContext(),recyclerView,new OnItemTouchListener.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {

                System.out.println("短按"+position);
                NewsTabBean.NewsData newsData=arrayNewsList.get(position-1);

                String readIds = PrefUtils.getString(getActivity(),"read_ids","");
                if (!readIds.contains(newsData.id+"")){
                    readIds = readIds+newsData.id + ",";
                    PrefUtils.setString(getActivity(),"read_ids",readIds);
                    TextView textView=view.findViewById(R.id.tv_title);
                    textView.setTextColor(Color.GRAY);
                }

                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                startActivity(intent);


            }

            @Override
            public void onLongItemClick(View view, int position) {
                System.out.println("长按"+position);
            }
        }));





    }

    @Override
    public void onStart() {
        super.onStart();
        initImageData();
    }

    /*初始化view*/
    private void initImageData(){


        imageViewsList=new ArrayList<>();
        for (int i=0;i<newsTabBean.data.topnews.size();i++){
            ImageView view=new ImageView(getActivity());
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            /*维护view*/
            myBitmapUtils.display(view,newsTabBean.getData().getTopnews().get(i).topimage);
            imageViewsList.add(view);
        }

    }



    public void getDataFromServer() {

        /*
         * 主线程不能开网络请求  会阻塞  用异步的
         *
         * */
        OkhttpUtils.sendRequestWithOkhttp(GlobalConstants.SERVER_URL+menu.getUrl(), new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getActivity(), "网络不通畅", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    String responseStr = response.body().string();
                    newsTabBean = processNewsData(responseStr);
                    CacheUtils.setCache(menu.getUrl(),responseStr,getActivity());
                    System.out.println(GlobalConstants.SERVER_URL+menu.getUrl()+"缓存成功");
                } else {
                    System.out.println(GlobalConstants.SERVER_URL+menu.getUrl());
                }
            }
        });

    }

    private NewsTabBean processNewsData(String responseStr) {

        Gson gson=new Gson();
        return gson.fromJson(responseStr, NewsTabBean.class);
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

    public  class NewsApdatar extends RecyclerView.Adapter {


        public static final int TYPE_HEADER = 0;  //说明是带有Header的
        public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
        public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
        private int mHeaderCount=1;//头部View个数
        private int mBottomCount=1;//底部View个数

        public View headerView;
        public View footView;

        ArrayList<NewsTabBean.NewsData> dataArrayList;

        public NewsApdatar(ArrayList<NewsTabBean.NewsData> newsData) {
            this.dataArrayList=newsData;
        }




        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            if (i==TYPE_HEADER){
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_header,viewGroup,false);
                return new HeadHolder(view);
            }else if (i==TYPE_FOOTER){
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_footer,viewGroup,false);
                return new FoodHolder(view);
            }else {
                View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_news,viewGroup,false);
                NewsHolder viewHolder=new NewsHolder(view);
                return viewHolder;
            }


        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            if (i==0){
                viewPager.setAdapter(new imageAdpter());
                circleIndicator.setViewPager(viewPager);
                circleIndicator.setSnap(true);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float v, int i1) {

                    }

                    @Override
                    public void onPageSelected(int i) {
                        textView.setText(newsTabBean.getData().topnews.get(i).title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {

                    }
                });

                textView.setText(newsTabBean.getData().topnews.get(0).title);

            }else if(i == getItemCount()-1){

            }else {
                NewsTabBean.NewsData newsData=dataArrayList.get(i);
                NewsHolder newsHolder = (NewsHolder)viewHolder;
                myBitmapUtils.display(newsHolder.imageView,newsData.listimage);
                newsHolder.textView.setText(newsData.title);

                String readIds = PrefUtils.getString(view.getContext(),"read_ids","");
                if (readIds.contains(newsData.id+"")){
                    newsHolder.textView.setTextColor(Color.GRAY);
                }else{
                    newsHolder.textView.setTextColor(Color.BLACK);
                }


                newsHolder.tv_data.setText(newsData.pubdate);
            }

            linearLayoutManager.getChildCount();
            linearLayoutManager.getItemCount();
            linearLayoutManager.findLastVisibleItemPosition();




        }


        @Override
        public int getItemCount() {
            return dataArrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (headerView!=null && footView!=null){
                return TYPE_NORMAL;
            }
            if (position==0){
                return TYPE_HEADER;
            }
            if (position==getItemCount()-1){
                return TYPE_FOOTER;
            }
            return TYPE_NORMAL;
        }

        class HeadHolder extends RecyclerView.ViewHolder{

            public HeadHolder(@NonNull View itemView) {
                super(itemView);
                viewPager = itemView.findViewById(R.id.view_pager);
                textView = itemView.findViewById(R.id.tv_topnews);
                circleIndicator = itemView.findViewById(R.id.indicator);
                recyclerView = itemView.findViewById(R.id.rv_list);
            }
        }

        class FoodHolder extends RecyclerView.ViewHolder{

            private final ContentLoadingProgressBar contentLoadingProgressBar;

            public FoodHolder(@NonNull View itemView) {
                super(itemView);
                contentLoadingProgressBar = itemView.findViewById(R.id.pb_progress);
            }
        }

        class NewsHolder  extends RecyclerView.ViewHolder{
            private  ImageView imageView;
            private final TextView textView;
            private final TextView tv_data;

            public NewsHolder (@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.iv_icon);
                textView = itemView.findViewById(R.id.tv_title);
                tv_data = itemView.findViewById(R.id.tv_date);
            }
        }


    }







}
