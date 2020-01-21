package com.example.helppiertestlibrary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;

public class HelppierApp {
    // helppier authentication key
    private String helppierKey;
    // the activity that we will be rendering over
    private Activity activity;
    // the current view in use
    private View view;
    private ViewGroup vg;
    // reference to our screen shot UI
    private View screenshotUI;
    private String requestUrl = "http://10.0.2.2:3000/widget/android/";

    public HelppierApp(String helppierKey, Activity activity, View view) {
        this.helppierKey = helppierKey;
        this.activity = activity;
        this.view = view;


        ViewGroup vg = (ViewGroup) view;
        this.vg = vg;

    }

    private void requestOnBoarding() {
        new OnboardingRequest(activity, helppierKey, requestUrl);
    }

    private void renderOverlay() { new RecordingOverlay(activity, view); }

    public void init() {
        requestOnBoarding();
        renderOverlay();
    }


    // converts a bitmap with the screenshot to a base 64
    private String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // inflates client activity with our screnshot UI
    private void renderScreenshotUI() {
        TextView topBox = new TextView(activity);
        topBox.setText("TopBox");
        int topBoxId = View.generateViewId();
        topBox.setId(topBoxId);
        vg.addView(topBox);
        ViewGroup.LayoutParams lpTopBox = topBox.getLayoutParams();
        lpTopBox.height = 400;
        topBox.setLayoutParams(lpTopBox);

        TextView bottomBox = new TextView(activity);
        bottomBox.setText("bottomBox");
        final int bottomBoxId = View.generateViewId();
        bottomBox.setId(bottomBoxId);
        vg.addView(bottomBox);
        ViewGroup.LayoutParams lpBottomBox = bottomBox.getLayoutParams();
        lpBottomBox.height = 400;
        bottomBox.setLayoutParams(lpBottomBox);

        Button addScreenshot = new Button(activity);
        addScreenshot.setText("Capture Screenshota");
        final int btnId = View.generateViewId();
        addScreenshot.setId(btnId);

        addScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone((ConstraintLayout)view);
                // constraintSet.connect(btnId, ConstraintSet.BOTTOM, bottomBoxId, ConstraintSet.TOP, 0);
                constraintSet.connect(btnId, ConstraintSet.TOP, 2131165258, ConstraintSet.BOTTOM, 0);
                constraintSet.applyTo((ConstraintLayout)view);
            }
        });

        vg.addView(addScreenshot);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone((ConstraintLayout)view);
        // placing a textview at the top of the screen
        constraintSet.connect(topBoxId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
        constraintSet.connect(topBoxId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
        constraintSet.connect(topBoxId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);

        // placing a textview at the bottom of the screne
        constraintSet.connect(bottomBoxId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
        constraintSet.connect(bottomBoxId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
        constraintSet.connect(bottomBoxId, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);

        // constraintSet.connect(btnId, ConstraintSet.TOP, topBoxId, ConstraintSet.BOTTOM, 0);
        // constraintSet.connect(btnId, ConstraintSet.BOTTOM, bottomBoxId, ConstraintSet.TOP, 0);

        constraintSet.applyTo((ConstraintLayout)view);
    }

    // removes the screenshot UI we appended to the clients view
    private void removeScreenshotUI() {
        vg.removeView(screenshotUI);
    }

    // take the screenshot and upload it
    private void takeScreenshot() {
        final Activity finalActivity = activity;

        Log.i("TakeScreenshot", "Taking screenshot");

        try {
            // remove our UI from the screenshotq
            removeScreenshotUI();
            // take the bitmap / screenshot
            View windowView = view.getRootView();
            final Bitmap bitmap = Bitmap.createBitmap(
                    windowView.getWidth(),
                    windowView.getHeight(),
                    Bitmap.Config.ARGB_8888
            );
            // re-add our UI to enable more screenshots
            renderScreenshotUI();

            StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
            "http://10.0.2.2:3000/widget/android/screenshot",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Showing toast message of the response
                        try {
                            if (response != "") {
                                Toast.makeText(finalActivity, "Image uploaded!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(finalActivity, "Error: " + response, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {
                            Toast.makeText(finalActivity, "Failed to upload.", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        //Showing toast
                        try {
                            JSONObject jsonObject = new JSONObject(volleyError.getMessage());
                            int responseCode = Integer.parseInt(jsonObject.getString("responseCode"));
                            String response = jsonObject.getString("response");
                            if (responseCode == 1) {
                                Toast.makeText(finalActivity, response, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(finalActivity, "Error: " + response, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {
                            Toast.makeText(finalActivity, "Failed to upload.", Toast.LENGTH_LONG).show();
                        }
                    }
                }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    //Converting Bitmap to String
                    String image = toBase64(bitmap);

                    //Getting Image Name

                    //Creating parameters
                    Map<String,String> params = new Hashtable<>();

                    //Adding parameters
                    params.put("base64", image);
                    params.put("helppierKey", helppierKey);

                    //returning parameters
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(activity);
            //Adding request to the queue
            queue.add(stringRequest);
        } catch (Throwable e) {
            Log.i("TakeScreenShotFailed", "Taking screenshot failed");

            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }


}
