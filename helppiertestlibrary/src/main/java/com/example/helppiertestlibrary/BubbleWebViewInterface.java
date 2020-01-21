package com.example.helppiertestlibrary;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;

public class BubbleWebViewInterface {   
    Context mContext;
    BubbleWebView bubbleWebView;

    /** Instantiate the interface and set the context */
    BubbleWebViewInterface(Context c, BubbleWebView bwv) {
        mContext = c;
        bubbleWebView = bwv;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void getBubbleSize(final int width, final int height) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            public void run() {
                bubbleWebView.setSize(width, height);
                bubbleWebView.positionBubble();
            }
        });
    }
}