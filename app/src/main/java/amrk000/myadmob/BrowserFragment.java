package amrk000.myadmob;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class BrowserFragment extends Fragment {
    WebView browser;
    ConstraintLayout loading,disconnected;
    ImageView trex,admobTrack;
    TextView tips;
    String pageUrl = "";
    boolean zoom;

    LinearLayout zoomPanel;
    ImageButton zoomIn,zoomOut;

    ConnectivityManager connectivityManager;

    public BrowserFragment(String pageUrl,boolean zoom){
        this.pageUrl = pageUrl;
        this.zoom=zoom;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browser, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        browser = view.findViewById(R.id.browser);
        loading = view.findViewById(R.id.loading_layout);
        tips = view.findViewById(R.id.tip);

        connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        disconnected = view.findViewById(R.id.disconnected_layout);
        trex = view.findViewById(R.id.disconnected_trex);
        admobTrack = view.findViewById(R.id.disconnected_admob_icon);

        zoomPanel=view.findViewById(R.id.zoom_panel);
        zoomIn=view.findViewById(R.id.zoom_in);
        zoomOut=view.findViewById(R.id.zoom_out);

        zoomPanel.setVisibility(View.GONE);

        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browser.zoomIn();
            }
        });

        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browser.zoomOut();
            }
        });

        String[] allTips = getResources().getStringArray(R.array.tips);
        tips.setText(allTips[new Random().nextInt(allTips.length)]);

        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(browser,true);

        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setDomStorageEnabled(true);
        browser.getSettings().setBuiltInZoomControls(false);
        browser.getSettings().setSupportZoom(true);
        browser.getSettings().setUseWideViewPort(true);
        browser.getSettings().setAllowContentAccess(true);

        browser.getSettings().setAppCacheEnabled(true);
        browser.getSettings().setAllowFileAccess(true);
        browser.getSettings().setAppCachePath(getContext().getCacheDir().getPath());
        browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        browser.clearCache(false);


        browser.setLayerType(View.LAYER_TYPE_HARDWARE, null);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ((MainActivity)getActivity()).darkMode) {
            browser.getSettings().setForceDark(WebSettings.FORCE_DARK_ON);
        }

        browser.setWebViewClient(new WebViewClient(){
            //on page loading
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loading.setVisibility(View.VISIBLE);

                if(!networkConnected()) {
                    browser.stopLoading();
                    disconnected.setVisibility(View.VISIBLE);
                    trex.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.trex));
                    admobTrack.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.admob_track));

                } else disconnected.setVisibility(View.GONE);
            }

            //on page loaded
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.contains("apps.admob.com/v2/")) {
                    browser.evaluateJavascript("document.getElementsByClassName('app-bar-panel')[0].style.display='none';", null);
                    browser.evaluateJavascript("document.getElementsByClassName('tlc-content')[0].style.height='100%';", null);
                }

                if(url.contains("apps.admob.com/v2/reports/library")) {
                    browser.evaluateJavascript("document.getElementsByClassName('banner-container')[0].style.width='100%';", null);
                    browser.evaluateJavascript("document.getElementsByClassName('no-reports')[0].style.marginBottom='25%';", null);

                }

                if(zoom && getActivity()!=null){
                    zoomPanel.setVisibility(View.VISIBLE);
                    zoomPanel.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
                    browser.scrollTo(0,0);
                }

                if(((MainActivity)getActivity())!=null) {
                    if (url.contains("apps.admob.com/v2/")) ((MainActivity) getActivity()).adapter.setInAdmob();
                    else if (url.contains("accounts.google.com")) ((MainActivity) getActivity()).adapter.setOutOfAdmob("Sign in");
                    else {
                        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(browser.getUrl())));
                        browser.stopLoading();
                        browser.goBack();
                    }
                }

            }

            //on page change
            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
                if(!isReload && browser.canZoomOut()) browser.zoomBy(0.1f);

                if(url.contains("reports/") && !pageUrl.contains("reports/")){
                    BrowserFragment reports = (BrowserFragment) ((MainActivity)getActivity()).adapter.instantiateItem(((MainActivity)getActivity()).viewPager, 2);
                    reports.browser.loadUrl(url);
                    ((MainActivity)getActivity()).viewPager.setCurrentItem(2);

                    browser.loadUrl(pageUrl);
                }
                else if(url.contains("apps/") && !pageUrl.contains("apps/")){
                    BrowserFragment apps = (BrowserFragment) ((MainActivity)getActivity()).adapter.instantiateItem(((MainActivity)getActivity()).viewPager, 1);
                    apps.browser.loadUrl(url);
                    ((MainActivity)getActivity()).viewPager.setCurrentItem(1);

                    browser.loadUrl(pageUrl);
                }

            }
        });

        //progress tracking
        browser.setWebChromeClient(new WebChromeClient(){

            //track progress
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

            }

            //when web page icon load : the page is fully rendered & loaded
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);

                loading.setVisibility(View.GONE);

            }

        });



    }

    @Override
    public void onResume() {
        super.onResume();
        if(browser.getUrl()==null) browser.loadUrl(pageUrl);

    }

    private boolean networkConnected(){
        return connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}