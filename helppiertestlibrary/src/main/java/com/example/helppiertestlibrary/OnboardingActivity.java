package com.example.helppiertestlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class OnboardingActivity extends AppCompatActivity {

    private int totalImages = 0;
    private int currentIndex = 0;
    private JSONArray imagesList;
    private float swipeStartX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        Intent intent = getIntent();
        String message = intent.getStringExtra(OnboardingRequest.IMAGES_LIST);
        try {
            this.imagesList = new JSONArray(message);
            this.totalImages = imagesList.length();
            this.prepareBtns();
            this.loadImage();
            this.updateCounter(currentIndex);
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

    private void updateCounter(int nextIndex) {
        TextView counterLabel = findViewById(R.id.counterLabel);
        String newLabel = (nextIndex + 1) + "/" + totalImages;
        counterLabel.setText(newLabel);
    }


    private void updateUIAfterAction(int nextIndex) {
        this.currentIndex = nextIndex;
        this.loadImage();
        this.updateButtons(this.currentIndex);
        this.updateCounter(this.currentIndex);
    }

    private void setupNextEventListener(Button nextBtn) {
        nextBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateUIAfterAction(currentIndex + 1);
            }
        });
    }

    private void setupPreviousEventListener(Button previousBtn) {
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            updateUIAfterAction(currentIndex - 1);
            }
        });
    }

    private void setupSkipEventListener(Button skipBtn) {
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void prepareBtns() {
        Button nextBtn = findViewById(R.id.nextBtn);
        Button previousBtn = findViewById(R.id.previousBtn);
        Button skipBtn = findViewById(R.id.skipBtn);
        ImageView image = findViewById(R.id.onboardingimage);

        this.validateBtnVisibility(nextBtn);
        this.setupSwipeImageEventListeners(image);
        this.setupBtnEventListeners(nextBtn, previousBtn, skipBtn);
    }

    private void updateButtons(int nextIndex) {
        boolean[] btnStatus = this.validateTest(nextIndex);

        Button nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setVisibility(btnStatus[0] ? View.VISIBLE : View.INVISIBLE);

        Button previousBtn = findViewById(R.id.previousBtn);
        previousBtn.setVisibility(btnStatus[1] ? View.VISIBLE : View.INVISIBLE);

        Button skipBtn = findViewById(R.id.skipBtn);
        // skipBtn.setVisibility(btnStatus[2] ? View.VISIBLE : View.INVISIBLE);
        skipBtn.setText(btnStatus[2] ? "Skip" : "End");
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
        this.setupNextEventListener(nextBtn);
        this.setupPreviousEventListener(previousBtn);
        this.setupSkipEventListener(skipBtn);
    }

    private void setupSwipeImageEventListeners(ImageView image) {
        image.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // have same code as onTouchEvent() (for the Activity) above
                int action = event.getActionMasked();
                Log.d("ACTION ON TOUCH", String.valueOf(action));
                switch(action) {
                    case MotionEvent.ACTION_DOWN:
                        swipeStartX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        float swipeEndX = event.getX();
                        float deltaX = swipeEndX - swipeStartX;
                        if(Math.abs(deltaX) > 150) {
                            // left to right
                            if(swipeEndX > swipeStartX) {
                                if(currentIndex != 0) updateUIAfterAction(currentIndex - 1);
                                return true;
                            }

                            // right to left
                            if(currentIndex <= totalImages - 1 ) updateUIAfterAction(currentIndex + 1);
                            return true;
                        }
                        break;
                }
                return true;
            }
        });
    }
}
