package com.wyj.wan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.wyj.wan.R;
import com.wyj.wan.base.BaseActivity;

import butterknife.BindView;

public class ArticleActivity extends BaseActivity {
    private static final String ARG_PARAM_URL = "ArticleWebUrl";
    private static final String ARG_PARAM_TITLE = "ArticleTitle";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.container)
    LinearLayout mContainer;
    private Bundle bundle;
    private String title;
    private String articleLink;
    private AgentWeb mAgentWeb;
    private AlertDialog mAlertDialog;

    @Override
    protected int getContentViewID() {
        return R.layout.activity_article;
    }

    @Override
    protected void setStatusBar() {
    }

    public static void startActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.putExtra(ARG_PARAM_URL, url);
        intent.putExtra(ARG_PARAM_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        getBundleData();
        mToolbar.setTitle(title);
    }

    private void getBundleData() {
        bundle = getIntent().getExtras();
        title = bundle.getString(ARG_PARAM_TITLE);
        articleLink = bundle.getString(ARG_PARAM_URL);
//        articleId = ((int) bundle.get(Constants.ARTICLE_ID));
//        isCommonSite = ((boolean) bundle.get(Constants.IS_COMMON_SITE));
//        isCollect = ((boolean) bundle.get(Constants.IS_COLLECT));
//        isCollectPage = ((boolean) bundle.get(Constants.IS_COLLECT_PAGE));
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    protected void initListener() {
        mToolbar.setNavigationOnClickListener(v -> showDialog());
    }

    @Override
    protected void initCompleted() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mContainer, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .ready()
                .go(articleLink);
        if (mAgentWeb != null) {
            //注入对象
            mAgentWeb.getJsInterfaceHolder().addJavaObject("imagelistner", new AndroidInterface(mAgentWeb, this));
        }
    }

    // 注入js函数监听
    private void addImageClickListener(WebView webView) {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，
        //函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
                "var imgs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<imgs.length;i++)  " +
                "{"
                + "    imgs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            addImageClickListener(view);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            Log.i("Info", "BaseWebActivity onPageStarted");
        }
    };
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
//            Log.i("Info","onProgress:"+newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mToolbar != null) {
                mToolbar.setTitle(title);
            }
        }
    };

    private void showDialog() {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(this)
                    .setMessage("您确定要关闭该页面吗?")
                    .setNegativeButton("再逛逛", (dialog, which) -> {
                        if (mAlertDialog != null) {
                            mAlertDialog.dismiss();
                        }
                    })//
                    .setPositiveButton("确定", (dialog, which) -> {

                        if (mAlertDialog != null) {
                            mAlertDialog.dismiss();
                        }
                        ArticleActivity.this.finish();
                    }).create();
        }
        mAlertDialog.show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mAgentWeb.destroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }

    public class AndroidInterface {

        private AgentWeb agent;
        private Context context;

        public AndroidInterface(AgentWeb agent, Context context) {
            this.agent = agent;
            this.context = context;
        }


        @JavascriptInterface
        public void openImage(final String msg) {
            Toast.makeText(context.getApplicationContext(),  msg, Toast.LENGTH_LONG).show();
        }

    }
}
