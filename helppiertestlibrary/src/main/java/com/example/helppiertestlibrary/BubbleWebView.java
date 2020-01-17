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
    int viewId;
    WebView webView;
    View view;
    int elementId;

    public BubbleWebView(Context context, View view, int elementId) {
        super(context);

        this.elementId = elementId;
        this.view = view;
        this.viewId = view.generateViewId();

        // LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // this.webView = (WebView)inflater.inflate(R.layout.bubble_webview, null, false);
        // ConstraintLayout clView = (ConstraintLayout)inflater.inflate(R.layout.bubble_layout, null, false);
        // this.webView = clView.findViewById(R.id.bubbleWebView);
        // this.webView = (WebView)inflater.inflate(R.layout.bubble_layout, null, false);
        //this.webView = (WebView)inflater.inflate(R.layout.bubble_layout, (ViewGroup)view, true);
        //inflater.inflate(R.layout.bubble_layout)

        // addView(webView);
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        loadUrl("http://10.0.2.2:3000/widget/backoffice/mobilebubble");
        // loadUrl("http://10.0.2.2:1337/android");

        setId(viewId);
        setWebContentsDebuggingEnabled(true);
        addJavascriptInterface(new BubbleWebViewInterface(context, this), "Android");

        // position();
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

    public void position() {
        // ViewGroup.LayoutParams lpWebView = webView.getLayoutParams();
        // ViewGroup.LayoutParams lpWebView = getLayoutParams();
        // lpWebView.height = 426;
        // lpWebView.width = 270;
        // setLayoutParams(lpWebView);

        if(view instanceof ConstraintLayout) {
            setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
        } else if(view instanceof RelativeLayout) {
            setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        }


        positionBubble();
    }

    private void positionBubble() {
        if(view instanceof ConstraintLayout) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone((ConstraintLayout) view);
            // constraintSet.connect(elementId, ConstraintSet.BOTTOM, webviewId, ConstraintSet.TOP, 0);
            constraintSet.connect(viewId, ConstraintSet.TOP, elementId, ConstraintSet.BOTTOM, 0);
            constraintSet.applyTo((ConstraintLayout) view);
        } else if(view instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)webView.getLayoutParams();
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.addRule(RelativeLayout.ALIGN_TOP, elementId);
            webView.setLayoutParams(params);
        }
    }

}
