package qunincey.com.smartcity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import qunincey.com.smartcity.global.GlobalConstants;
import qunincey.com.smartcity.utils.CacheUtils;
import qunincey.com.smartcity.utils.OkhttpUtils;
import qunincey.com.smartcity.utils.PrefUtils;
import qunincey.com.smartcity.utils.ProcessDataUtils;

public class SplashActivity extends AppCompatActivity {

    private ConstraintLayout rlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getDataFromServer();

        rlRoot = (ConstraintLayout) findViewById(R.id.rl_root);
        /*属性动画--旋转*/
        ObjectAnimator animator=ObjectAnimator.ofFloat(rlRoot,"rotation",0f,360f);


        /*属性动画--缩放*/
        ObjectAnimator scaleY=ObjectAnimator.ofFloat(rlRoot,"scaleY",0,1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(rlRoot, "scaleX", 0, 1f);

        /*属性动画--渐变*/
        ObjectAnimator animator2=ObjectAnimator.ofFloat(rlRoot,"alpha",0f,1.0f);


        /*组合动画*/
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.play(animator).with(scaleY).with(scaleX).with(animator2);
        animatorSet.setDuration(5000);
        animatorSet.start();

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent;

                boolean isFirstEnter=PrefUtils.getBoolean(SplashActivity.this,"is_First_Enter",true);
                if (isFirstEnter){
                    intent=new Intent(getApplicationContext(),GuideActivity.class);
                }else{
                    intent=new Intent(getApplicationContext(),MainActivity.class);
                }

                startActivity(intent);
                finish();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void getDataFromServer() {

        /*
         * 主线程不能开网络请求  会阻塞  用异步的
         *
         * */
        OkhttpUtils.sendRequestWithOkhttp(GlobalConstants.SERVER_URL + "/categories.json", new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(SplashActivity.this, "网络不通畅", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    ProcessDataUtils.processDataByNewsMenu(responseStr, SplashActivity.this);
                    System.out.println("闪屏页请求成功");
                } else {
//                    Toast.makeText(MainActivity.this,"服务器错误",Toast.LENGTH_SHORT).show();
                }
            }
        });

        OkhttpUtils.sendRequestWithOkhttp(GlobalConstants.PHOTOS_URL, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(SplashActivity.this, "网络不通畅", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    CacheUtils.setCache(GlobalConstants.PHOTOS_URL,result,SplashActivity.this);
                } else {
//                    Toast.makeText(MainActivity.this,"服务器错误",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
