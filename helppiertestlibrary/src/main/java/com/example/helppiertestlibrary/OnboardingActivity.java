package com.example.helppiertestlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class OnboardingActivity extends AppCompatActivity {

    private int totalImages = 0;
    private int currentIndex = 0;
    private JSONArray imagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        Intent intent = getIntent();
        String message = intent.getStringExtra(HelppierApp.IMAGES_LIST);
        try {
            imagesList = new JSONArray(message);
            totalImages = imagesList.length();
            prepareBtns();
            loadImage();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadImage() {
        try {
            ImageView imageView = findViewById(R.id.onboardingimage);
            String img = imagesList.getString(currentIndex);
            Picasso.get().load(img).into(imageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupNextEventListener(final Button nextBtn, final Button previousBtn) {
        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*final int nextIndex = currentIndex + 1;

                if(totalImages == 0) {
                    // this should have never happened
                    nextBtn.setVisibility(View.INVISIBLE);
                    previousBtn.setVisibility(View.INVISIBLE);
                    return;
                }

                // we are out of bounds ; most likely a bug
                if(totalImages == nextIndex) {
                    nextBtn.setVisibility(View.INVISIBLE);
                    previousBtn.setVisibility(View.INVISIBLE);
                    return;
                }

                // we will be reaching the last one
                if(totalImages - 1 == nextIndex) {
                    currentIndex = nextIndex;
                    loadImage();

                    nextBtn.setVisibility(View.INVISIBLE);
                    previousBtn.setVisibility(View.VISIBLE);
                    return;
                }

                currentIndex = nextIndex;
                loadImage();

                // nextBtn.setVisibility(View.VISIBLE);
                previousBtn.setVisibility(View.VISIBLE);*/

                currentIndex = currentIndex + 1;
                loadImage();
                updateButtons(currentIndex);

            }
        });
    }

    private void setupPreviousEventListener(final Button nextBtn, final Button previousBtn) {
        previousBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*final int nextIndex = currentIndex - 1;

                if(totalImages == 0) {
                    // this should have never happened
                    nextBtn.setVisibility(View.INVISIBLE);
                    previousBtn.setVisibility(View.INVISIBLE);
                    return;
                }

                // we are out of bounds ; most likely a bug
                if(totalImages == nextIndex) {
                    nextBtn.setVisibility(View.INVISIBLE);
                    previousBtn.setVisibility(View.INVISIBLE);
                    return;
                }

                // we will be reaching the last one
                if(nextIndex == 0) {
                    currentIndex = nextIndex;
                    loadImage();

                    nextBtn.setVisibility(View.VISIBLE);
                    previousBtn.setVisibility(View.INVISIBLE);
                    return;
                }

                currentIndex = nextIndex;
                loadImage();

                nextBtn.setVisibility(View.VISIBLE);
                // previousBtn.setVisibility(View.VISIBLE);*/
                currentIndex = currentIndex - 1;
                loadImage();
                updateButtons(currentIndex);
            }
        });
    }

    private void setupSkipEventListener(Button skipBtn) {
        skipBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void prepareBtns() {
        Button nextBtn = findViewById(R.id.nextBtn);
        Button previousBtn = findViewById(R.id.previousBtn);
        Button skipBtn = findViewById(R.id.skipBtn);

        validateBtnVisibility(nextBtn);
        setupBtnEventListeners(nextBtn, previousBtn, skipBtn);
    }

    private void updateButtons(int nextIndex) {
        boolean[] btnStatus = validateTest(nextIndex);

        Button nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setVisibility(btnStatus[0] ? View.VISIBLE : View.INVISIBLE);


        Button previousBtn = findViewById(R.id.previousBtn);
        previousBtn.setVisibility(btnStatus[1] ? View.VISIBLE : View.INVISIBLE);

        Button skipBtn = findViewById(R.id.skipBtn);
        skipBtn.setVisibility(btnStatus[2] ? View.VISIBLE : View.INVISIBLE);
    }

    private boolean[] validateTest(int nextIndex) {
        // resetting buttons
        boolean nextStatus = false;
        boolean previousStatus = false;
        boolean skipStatus = true;

        if(totalImages == 0) {
            boolean[] result = new boolean[3];
            result[0] = false;
            result[1] = false;
            result[2] = false;

            return result;
        }

        if(nextIndex > 0) {
            previousStatus = true;
        }

        if(nextIndex < totalImages - 1) {
            nextStatus = true;
        }

        if(nextIndex == totalImages - 1) {
            skipStatus = false;
        }

        boolean[] result = new boolean[3];
        result[0] = nextStatus;
        result[1] = previousStatus;
        result[2] = skipStatus;

        return result;

    }

    private void validateBtnVisibility(Button nextBtn) {
        if(totalImages == 0 || totalImages == 1) {
            // the btns are invisible by default
            return;
        }

        nextBtn.setVisibility(View.VISIBLE);
    }

    private void setupBtnEventListeners(Button nextBtn, Button previousBtn, Button skipBtn) {
        setupNextEventListener(nextBtn, previousBtn);
        setupPreviousEventListener(nextBtn, previousBtn);
        setupSkipEventListener(skipBtn);
    }
}
