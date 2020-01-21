package com.example.helppiertestlibrary;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class OnboardingRequest {
    final Activity activity;
    String helppierKey;
    String helppierRequestUrl;
    String requestPath = "/onboarding";

    public static final String IMAGES_LIST = "com.example.helppierTestLibrary.IMAGES_LIST";

    public OnboardingRequest(Activity activity, String helppierKey, String helppierRequestUrl) {
        this.activity = activity;
        this.helppierKey = helppierKey;
        this.helppierRequestUrl = helppierRequestUrl;

        // check the onboarding on startup
        validateAutomaticOnboarding();
        // TODO: Add a mechanism to request onboarding per Activity change
    }

    private void validateAutomaticOnboarding() {
        // add some interesting validation here
        if(true) {
            requestOnboarding();
        }
    }

    // take the screenshot and upload it
    private void requestOnboarding() {
        Log.i("RequestOnboarding", "Request onboarding");

        try {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    helppierRequestUrl + requestPath,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (response != "") {
                                    Log.i("ImagesReceived", response);
                                    Toast.makeText(activity, "Images received", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(activity, OnboardingActivity.class);
                                    intent.putExtra(IMAGES_LIST, response);
                                    activity.startActivity(intent);
                                } else {
                                    Toast.makeText(activity, "Error: " + response, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception ex) {
                                Toast.makeText(activity, "Failed to get images.", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            try {
                                JSONObject jsonObject = new JSONObject(volleyError.getMessage());
                                int responseCode = Integer.parseInt(jsonObject.getString("responseCode"));
                                String response = jsonObject.getString("response");
                                if (responseCode == 1) {
                                    Toast.makeText(activity, response, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(activity, "Error: " + response, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception ex) {
                                Toast.makeText(activity, "Failed to get images.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Creating parameters
                    Map<String,String> params = new Hashtable<>();
                    params.put("helppierKey", helppierKey);
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
