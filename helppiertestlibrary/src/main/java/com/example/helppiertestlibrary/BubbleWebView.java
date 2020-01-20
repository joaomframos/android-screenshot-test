package com.example.helppiertestlibrary;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class BubbleWebView extends WebView {
    int viewId = R.id.bubbleWebview;
    View view;
    int elementId;

    public BubbleWebView(Context context, View view, int elementId) {
        super(context);

        this.elementId = elementId;
        this.view = view;

        // set the access to the webview route
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        loadUrl("http://10.0.2.2:3000/widget/backoffice/mobilebubble");

        // set the component id for future positioning purposes
        this.setId(this.viewId);

        // setup the javascript interface to determine the size required
        setWebContentsDebuggingEnabled(true);
        addJavascriptInterface(new BubbleWebViewInterface(context, this), "Android");
    }

    public void setSize(int width, int height) {
        float density = getResources().getDisplayMetrics().density;

        ViewGroup.LayoutParams lpWebView = getLayoutParams();
        float heightF = height * density;
        lpWebView.height = Math.round(heightF);
        float widthF = width * density;
        lpWebView.width = Math.round(widthF);
        Toast.makeText(getContext(), lpWebView.width + " - " + lpWebView.height, Toast.LENGTH_SHORT).show();

        setLayoutParams(lpWebView);
    }


    public void positionBubble() {
        if(view instanceof ConstraintLayout) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone((ConstraintLayout) view);
            constraintSet.connect(viewId, ConstraintSet.TOP, elementId, ConstraintSet.BOTTOM, 0);
            constraintSet.applyTo((ConstraintLayout) view);
        } else if(view instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)this.getLayoutParams();
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.addRule(RelativeLayout.ALIGN_TOP, elementId);
            this.setLayoutParams(params);
        }
    }

}
