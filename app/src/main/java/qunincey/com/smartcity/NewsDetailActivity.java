package qunincey.com.smartcity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import qunincey.com.smartcity.utils.MyWebChromeClient;
import qunincey.com.smartcity.utils.PrefUtils;

import static android.provider.UserDictionary.Words.APP_ID;

public class NewsDetailActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private String mUrl;
    private WebSettings settings;
    private Tencent mTencent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Toolbar toolbar=findViewById(R.id.my_toolbar);

        progressBar = findViewById(R.id.pb_webview);


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        WebView webView=findViewById(R.id.webView1);
        settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);

        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        ImageButton imageButton= findViewById(R.id.btn_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsDetailActivity.this.finish();
            }
        });

        mUrl = getIntent().getStringExtra("url");

        webView.loadUrl(mUrl);
        webView.setWebChromeClient(new MyWebChromeClient(progressBar));
        webView.setWebViewClient( new WebViewClient(){



            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                System.out.println("开始加载");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                System.out.println("加载失败"+error.toString());
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }


        });

        mTencent = Tencent.createInstance("101584630", this.getApplicationContext());


    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.btn_back:
                    System.out.println("被点击了");
                    finish();
                case R.id.action_edit:
                    showChooseDialog();
                    break;
                case R.id.action_share:
                    final Bundle params = new Bundle();
                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                    params.putString(QQShare.SHARE_TO_QQ_TITLE,"震惊，舍友竟然还在打游戏");
                    params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "没什么好分享的");
                    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  mUrl);
                    params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "智慧北京"+"101584630");
                    mTencent.shareToQQ(NewsDetailActivity.this,params,qqShareListener);
                    break;
            }
            return true;
        }
    };

    private int mTemWhich;
    private int mCurrentWhich =2;

    private void showChooseDialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("字体设置");

        String [] items = new String[]{"超大号字体","大号字体","正常字体","小号字体","小号字体"};

        mCurrentWhich=PrefUtils.getInt(this,"mCurrentWhich",2);

        builder.setSingleChoiceItems(items, mCurrentWhich , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTemWhich = which;
                PrefUtils.setInt(NewsDetailActivity.this,"mCurrentWhich",mCurrentWhich);
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (mTemWhich){

                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                        default:
                            break;

                }
                mCurrentWhich = mTemWhich;
            }
        });

        builder.setNegativeButton("取消",null);

        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
        }
        @Override
        public void onComplete(Object response) {

        }
        @Override
        public void onError(UiError e) {

        }
    };


}
