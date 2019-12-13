package com.example.helppiertestlibrary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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
    private LinearLayout view;
    // reference to our screen shot UI
    private View screenshotUI;

    public static final String IMAGES_LIST = "com.example.helppierTestLibrary.IMAGES_LIST";


    // event listener for the screenshot button
    private View.OnClickListener screenshotListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // takeScreenshot();
            requestOnboarding();
        }
    };

    public HelppierApp(String helppierKey, Activity activity, LinearLayout view) {
        this.helppierKey = helppierKey;
        this.activity = activity;
        this.view = view;
    }

    public void init() {
        requestOnboarding();
        // renderScreenshotUI();
        renderWebviewUI();
    }

    private void renderScreenshot(Bitmap bitmap) {
        ImageView screenshotView = activity.findViewById(R.id.screenShot);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        screenshotView.setImageBitmap(bitmap);
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
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        screenshotUI = layoutInflater.inflate(R.layout.screenshot, null, false);
        view.addView(screenshotUI);

        Button screenshotC = activity.findViewById(R.id.button);
        screenshotC.setOnClickListener(screenshotListener);
    }

    // inflats client activity with our webview UI
    private void renderWebviewUI() {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        screenshotUI = layoutInflater.inflate(R.layout.webview, null, false);
        view.addView(screenshotUI);


        WebView myWebView = activity.findViewById(R.id.bubbleWebview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.loadUrl("http://10.0.2.2:3000/widget/backoffice/mobilebubble");
    }

    // removes the screenshot UI we appended to the clients view
    private void removeScreenshotUI() {
        view.removeView(screenshotUI);
    }

    // take the screenshot and upload it
    private void takeScreenshot() {
        final Activity finalActivity = activity;

        Log.i("TakeScreenshot", "Taking screenshot");

        try {
            // remove our UI from the screenshot
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

            renderScreenshot(bitmap);


        } catch (Throwable e) {
            Log.i("TakeScreenShotFailed", "Taking screenshot failed");

            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    // take the screenshot and upload it
    private void requestOnboarding() {
        final Activity finalActivity = activity;

        Log.i("RequestOnboarding", "Request onboarding");

        try {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    "http://10.0.2.2:3000/widget/android/onboarding",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Showing toast message of the response
                            try {
                                if (response != "") {
                                    Log.i("ImagesReceived", response);
                                    Toast.makeText(finalActivity, "Images received", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(activity, OnboardingActivity.class);
                                    intent.putExtra(IMAGES_LIST, response);
                                    activity.startActivity(intent);
                                } else {
                                    Toast.makeText(finalActivity, "Error: " + response, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception ex) {
                                Toast.makeText(finalActivity, "Failed to get images.", Toast.LENGTH_LONG).show();
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
                                Toast.makeText(finalActivity, "Failed to get images.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Creating parameters
                    Map<String,String> params = new Hashtable<>();

                    params.put("helppierKey", helppierKey);
                    params.put("helppierKeAy", "asd");


                    //returning parameters
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(activity);
            //Adding request to the queue
            queue.add(stringRequest);

        } catch (Throwable e) {
            Log.i("GetScreenShotFailed", "Getting screenshot failed");

            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
}
