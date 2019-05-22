package qunincey.com.smartcity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import qunincey.com.smartcity.utils.PrefUtils;

public class GuideActivity extends AppCompatActivity {
    ViewPager viewPager;

    CircleIndicator circleIndicator;

    Button btnStart;

    private Integer[] mImagesIds=new Integer[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};

    private ArrayList<ImageView> imageViewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        viewPager=findViewById(R.id.vp_guide);
        circleIndicator=findViewById(R.id.indicator);
        btnStart = findViewById(R.id.btn_start);
        initData();
        /*设置适配器*/
        viewPager.setAdapter(new GuideAdater());
        circleIndicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i == imageViewsList.size()-1 ){
                    btnStart.setVisibility(View.VISIBLE);
                }else{
                    btnStart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.setBoolean(getApplicationContext(),"is_First_Enter",false);
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }

    /*初始化view*/
    private void initData(){
        imageViewsList=new ArrayList<>();
        for (int i=0;i<mImagesIds.length;i++){
            ImageView view=new ImageView(this);
            view.setBackgroundResource(mImagesIds[i]);
            /*维护view*/
            imageViewsList.add(view);
        }

    }

    class GuideAdater extends PagerAdapter{

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
