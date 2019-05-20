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

import qunincey.com.smartcity.utils.PrefUtils;

public class SplashActivity extends AppCompatActivity {

    private ConstraintLayout rlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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
}
