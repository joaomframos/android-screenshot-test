package com.example.helppiertestlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

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

public class ScreenshotUI extends ConstraintLayout {
    View view;
    Button screenshotBtn;
    final String helppierKey;
    final String helppierRequestUrl;
    final String requestPath = "/android/screenshot";

    public ScreenshotUI(Context context, View view, String helppierKey, String helppierRequestUrl) {
        super(context);

        this.view = view;
        this.helppierKey = helppierKey;
        this.helppierRequestUrl = helppierRequestUrl;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.screenshot, (ViewGroup) view, true);

        this.screenshotBtn = view.findViewById(R.id.btnScreenshot);

        this.setEventListener();
    }

    private void setEventListener() {
        this.screenshotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();
            }
        });

    }

    // converts a bitmap with the screenshot to a base 64
    private String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // TODO: This is helpful for debugging purposes but should never be called otherwise
    private void renderScreenshot(Bitmap bitmap) {
        new ScreenshotHelper(getContext(), this.view, bitmap);
    }

    // take the screenshot and upload it
    private void takeScreenshot() {
        Log.i("TakeScreenshot", "Taking screenshot");

        try {
            // remove our UI from the screenshot
            ((ViewGroup) this.view).removeView(this.screenshotBtn);

            // take the bitmap / screenshot
            View windowView = this.view.getRootView();

            final Bitmap bitmap = Bitmap.createBitmap(
                    windowView.getWidth(),
                    windowView.getHeight(),
                    Bitmap.Config.ARGB_8888
            );

            // re-add our UI to enable more screenshots
            new ScreenshotUI(getContext(), this.view, this.helppierKey, this.helppierRequestUrl);

            // TODO: This is helpful for debugging purposes but should always be disabled
            // renderScreenshot(bitmap);

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    this.helppierRequestUrl + requestPath,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Showing toast message of the response
                            try {
                                if (response != "") {
                                    Toast.makeText(getContext(), "Image uploaded!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "Error: " + response, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception ex) {
                                Toast.makeText(getContext(), "Failed to upload.", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(), "Error: " + response, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception ex) {
                                Toast.makeText(getContext(), "Failed to upload.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    //Converting Bitmap to String
                    String image = toBase64(bitmap);

                    //Getting Image Name

                    //Creating parameters
                    Map<String, String> params = new Hashtable<>();

                    //Adding parameters
                    params.put("base64", image);
                    params.put("helppierKey", helppierKey);

                    //returning parameters
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(getContext());
            //Adding request to the queue
            queue.add(stringRequest);
        } catch (Throwable e) {
            Log.i("TakeScreenShotFailed", "Taking screenshot failed");

            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
}
