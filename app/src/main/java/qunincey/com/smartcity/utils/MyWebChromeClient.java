package qunincey.com.smartcity.utils;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MyWebChromeClient extends WebChromeClient {

    private  ProgressBar mProgressBar;

    public MyWebChromeClient(ProgressBar mProgressBar) {
        this.mProgressBar=mProgressBar;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {

        if (newProgress == 100) {
            mProgressBar.setVisibility(GONE);
        } else {
            if (mProgressBar.getVisibility() == GONE)
                mProgressBar.setVisibility(VISIBLE);
            mProgressBar.setProgress(newProgress);
        }
        super.onProgressChanged(view, newProgress);
    }

}
