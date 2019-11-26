package com.example.helppiertestlibrary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;


public class HelppierApp {
    private String helppierKey;
    private Activity activity;
    private LinearLayout view;

    private View.OnClickListener screenshotListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            takeScreenshot();
        }
    };

    public HelppierApp(String helppierKey, Activity activity, LinearLayout view) {
        this.helppierKey = helppierKey;
        this.activity = activity;
        this.view = view;
    }

    private String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void renderScreenshotUI() {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View myView = layoutInflater.inflate(R.layout.screenshot, null, false);
        view.addView(myView);


        Button screenshotC = activity.findViewById(R.id.button);
        screenshotC.setOnClickListener(screenshotListener);
    }

    public void init() {
        renderScreenshotUI();
    }

    private void renderScreenshot(Bitmap bitmap) {
        ImageView screenshotView = activity.findViewById(R.id.screenShot);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        screenshotView.setImageBitmap(bitmap);
    }


    private void takeScreenshot() {
        final Activity finalActivity = activity;

        Log.i("TakeScreenshot", "Taking screenshot");

        try {
            final Bitmap bitmap = Bitmap.createBitmap(
                    view.getWidth(),
                    view.getHeight(),
                    Bitmap.Config.ARGB_8888
            );
            StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
            "http://10.0.2.2:3000/widget/android",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Showing toast message of the response
                        try {
                            JSONObject jsonObject = new JSONObject(s);
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
}
