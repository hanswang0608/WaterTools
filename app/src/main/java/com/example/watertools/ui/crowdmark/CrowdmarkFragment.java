//Credit to petrnohejl for lines 34-57 on Stack Overflow
//https://stackoverflow.com/questions/6077141/how-to-go-back-to-previous-page-if-back-button-is-pressed-in-webview
package com.example.watertools.ui.crowdmark;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.watertools.MainActivity;
import com.example.watertools.R;

public class CrowdmarkFragment extends Fragment {

    private CrowdmarkViewModel CrowdmarkViewModel;
    private WebView crowdmarkWebView;

    @Override
    public void onDestroy(){
        super.onDestroy();
        MainActivity.oldCrowdmarkURL = crowdmarkWebView.getUrl();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_crowdmark, container, false);

        crowdmarkWebView = (WebView)root.findViewById(R.id.crowdmarkWebView);
        crowdmarkWebView.getSettings().setJavaScriptEnabled(true);
        crowdmarkWebView.setWebViewClient(new WebViewClient());
        crowdmarkWebView.loadUrl(MainActivity.oldCrowdmarkURL);
        CookieManager.getInstance().setAcceptCookie(true);
        crowdmarkWebView.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    WebView webView = (WebView) v;

                    switch(keyCode)
                    {
                        case KeyEvent.KEYCODE_BACK:
                            if(webView.canGoBack())
                            {
                                webView.goBack();
                                return true;
                            }
                            break;
                    }
                }

                return false;
            }
        });

        //partial credit to https://stackoverflow.com/questions/10069050/download-file-inside-webview for the download function
        crowdmarkWebView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        return root;
    }
}