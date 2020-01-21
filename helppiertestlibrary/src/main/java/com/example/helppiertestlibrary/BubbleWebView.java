package com.example.helppiertestlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class BubbleWebView extends WebView {
    View view;
    int elementId;
    WebView bubble;

    public BubbleWebView(Context context, View view, int elementId) {
        super(context);

        this.elementId = elementId;
        this.view = view;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bubble_webview, (ViewGroup) view, true);

        this.bubble = view.findViewById(R.id.bubbleWebview);

        // set the access to the webview route
        WebSettings webSettings = this.bubble.getSettings();
        webSettings.setJavaScriptEnabled(true);
        this.bubble.loadUrl("http://10.0.2.2:3000/widget/backoffice/mobilebubble");

        // setup the javascript interface to determine the size required
        this.bubble.setWebContentsDebuggingEnabled(true);
        this.bubble.addJavascriptInterface(new BubbleWebViewInterface(context, this), "Android");
    }

    public void setSize(int width, int height) {
        float density = getResources().getDisplayMetrics().density;

        ViewGroup.LayoutParams lpWebView = this.bubble.getLayoutParams();
        float heightF = height * density;
        lpWebView.height = Math.round(heightF);
        float widthF = width * density;
        lpWebView.width = Math.round(widthF);
        Toast.makeText(getContext(), lpWebView.width + " - " + lpWebView.height, Toast.LENGTH_SHORT).show();

        this.bubble.setLayoutParams(lpWebView);
    }


    public void positionBubble() {
        if(view instanceof ConstraintLayout) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone((ConstraintLayout) view);
            constraintSet.connect(R.id.bubbleWebview, ConstraintSet.TOP, elementId, ConstraintSet.BOTTOM, 0);
            constraintSet.applyTo((ConstraintLayout) view);
        } else if(view instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)this.bubble.getLayoutParams();
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.addRule(RelativeLayout.ALIGN_TOP, elementId);
            this.bubble.setLayoutParams(params);
        }
    }

}
