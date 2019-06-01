package qunincey.com.smartcity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import qunincey.com.smartcity.R;
import qunincey.com.smartcity.utils.Util;


public class FragmentSetting extends Fragment {

    private static boolean isServerSideLogin = false;
    private Tencent mTencent;
    private IUiListener loginListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mTencent = Tencent.createInstance("101584630", getActivity().getApplicationContext());

        loginListener = new BaseUiListener() {
            @Override
            protected void doComplete(JSONObject values) {
                Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
//                initOpenidAndToken(values);
//                updateUserInfo();
//                updateLoginButton();
            }
        };
        View view=inflater.inflate(R.layout.fragment_fragment_setting,container,false);
        Button button=view.findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });



        return view;
    }

    private class BaseUiListener implements IUiListener {


        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Util.showResultDialog(getActivity(), "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Util.showResultDialog(getActivity(), "返回为空", "登录失败");
                return;
            }
            Util.showResultDialog(getActivity(), response.toString(), "登录成功");
//            // 有奖分享处理
//            handlePrizeShare();
            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError uiError) {
            Util.toastMessage(getActivity(), "onError: " + uiError.errorDetail);
            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            Util.toastMessage(getActivity(), "onCancel: ");
            Util.dismissDialog();
            if (isServerSideLogin) {
                isServerSideLogin = false;
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void login()
    {
        mTencent = Tencent.createInstance("101584630", getActivity());
        if (!mTencent.isSessionValid())
        {
            mTencent.login(this, "all", loginListener);
        }
    }
}
