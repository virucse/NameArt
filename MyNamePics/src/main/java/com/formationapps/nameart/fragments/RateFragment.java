package com.formationapps.nameart.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.formationapps.nameart.R;
import com.formationapps.nameart.helper.OpenPlayStore;

/**
 * Created by caliber fashion on 9/12/2017.
 */

public class RateFragment extends BaseFragment {
    public static RateFragment instanse;
    private WebView mWebView_pp;
    private WebViewClient wClientPP = new WebViewClient() {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            view.loadUrl(url);
            return false;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instanse = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ratefragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWebView_pp = (WebView) view.findViewById(R.id.webView_pp);
        mWebView_pp.getSettings().setJavaScriptEnabled(true);

        Button rate = (Button) view.findViewById(R.id.action_rate_app);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
                new OpenPlayStore(getActivity()).execute(url);
            }
        });
        Button share = (Button) view.findViewById(R.id.action_share_app);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShare();
            }
        });
        Button email = (Button) view.findViewById(R.id.action_email_app);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailAppToFriend();
            }
        });
        Button suggestion = (Button) view.findViewById(R.id.action_suggestion_app);
        suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });
        Button privacy = (Button) view.findViewById(R.id.privacy_policy_app);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url2 = "http://formationapps-privacy-policy.blogspot.in/";
                mWebView_pp.setVisibility(View.VISIBLE);
                mWebView_pp.loadUrl(url2);
                mWebView_pp.setWebViewClient(wClientPP);
            }
        });
    }

    private void onShare() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.putExtra(android.content.Intent.EXTRA_SUBJECT, "" + getResources().getString(R.string.app_name));
        share.putExtra(android.content.Intent.EXTRA_TEXT,
                "Hey!\nTry My Name Pics an awesome app '" + getResources().getString(R.string.app_name)
                        + "'having 50+ famous unique fonts style ,Greeting Cards,imogies to make your name on and more" +
                        "at\nhttps://goo.gl/jSzZwL"
        );
        //share.putExtra(Intent.EXTRA_STREAM, fromMyHtml(html));
        startActivity(Intent.createChooser(share, "AppUtils Via"));
    }

    private void sendFeedback() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        String aEmailList[] = {"care.formationapps@gmail.com"};
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                getString(R.string.app_name) + " " + getString(R.string.email_subject));
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "" + getString(R.string.email_msg));
        startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.chooser_text)));
    }

    private void emailAppToFriend() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        String aEmailList[] = {"<--Enter Your Friend Email Here-->"};
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                getString(R.string.app_name));
        emailIntent.setType("plain/text");
        String url = "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey!\nTry My Name Pics an awesome app '" + getResources().getString(R.string.app_name)
                + " 'having 50+ famous unique fonts style ,Greeting Cards,imogies to make your name on and more " +
                "at\nhttps://goo.gl/50msVC");
        startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.email_send_choose_text)));
    }

    public boolean onBack() {
        if (mWebView_pp != null) {
            if (mWebView_pp.getVisibility() == View.VISIBLE) {
                mWebView_pp.setVisibility(View.GONE);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
