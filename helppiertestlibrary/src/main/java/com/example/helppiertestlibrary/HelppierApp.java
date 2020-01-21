package com.example.helppiertestlibrary;

import android.app.Activity;
import android.view.View;

public class HelppierApp {
    // helppier authentication key
    private String helppierKey;
    // the activity that we will be rendering over
    private Activity activity;
    // the current view in use
    private View view;
    private final String helppierRequestUrl = "http://10.0.2.2:3000/widget";

    public HelppierApp(String helppierKey, Activity activity, View view) {
        this.helppierKey = helppierKey;
        this.activity = activity;
        this.view = view;
    }

    private void requestOnBoarding() {
        new OnboardingRequest(this.activity, this.helppierKey, this.helppierRequestUrl);
    }

    private void renderOverlay() {
        new RecordingOverlay(this.activity, this.view, this.helppierRequestUrl);
    }

    // inflates client activity with our screenshot UI
    // TODO: Screenshot process is a secondary and should not be enable by default
    private void renderScreenshotUI() {
        new ScreenshotUI(this.activity, this.view, this.helppierKey, this.helppierRequestUrl);
    }

    public void init() {
        this.requestOnBoarding();
        this.renderOverlay();

        // TODO: Screenshot process is a secondary and should not be enable by default
        // renderScreenshotUI();
    }


}
