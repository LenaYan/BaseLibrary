/*
 *  Copyright (C) 2015 Rayman Yan
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.ray.mvvm.lib.view.web;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.ray.mvvm.lib.R;
import com.ray.mvvm.lib.databinding.ActivityWebViewBinding;
import com.ray.mvvm.lib.view.base.page.BaseActivity;
import com.ray.mvvm.lib.view.base.view.IRedirect;

public class WebViewActivity extends BaseActivity {

    private static final String PARA_TITLE = "PARA_TITLE";
    private static final String PARA_URL = "PARA_URL";

    public static void intent(IRedirect redirect, String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(PARA_TITLE, title);
        bundle.putString(PARA_URL, url);
        redirect.intent(WebViewActivity.class, bundle);
    }

    private WebViewVM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWebViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String title = intent.getStringExtra(PARA_TITLE);
        viewModel = new WebViewVM(intent.getStringExtra(PARA_URL));
        setTitle(title);
        WebView webView = (WebView) findViewById(R.id.web_view);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && progressBar.getVisibility() == View.GONE) {
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }

                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                    viewModel.setRefreshing(false);
                }
            }
        });
        binding.setViewModel(viewModel);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String title = intent.getStringExtra(PARA_TITLE);
        setTitle(title);
        viewModel.loadUrl(intent.getStringExtra(PARA_URL));
    }
}
