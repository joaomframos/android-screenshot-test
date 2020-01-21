package com.example.helppiertestlibrary;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class RecordingOverlay extends LinearLayout {
    View view;
    ViewGroup vg;
    LinearLayout overlay;
    String helppierRequestUrl;

    public RecordingOverlay(Context context, View view, String helppierRequestUrl) {
        super(context);

        if(view instanceof ConstraintLayout || view instanceof  RelativeLayout) {

            this.view = view;
            this.vg = (ViewGroup)view;
            this.helppierRequestUrl = helppierRequestUrl;

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.recording_overlay, (ViewGroup) view, true);

            this.overlay = view.findViewById(R.id.recordingOverlay);
            this.positionOverlay();
            this.setEventListener(context);
            return;
        }

        Toast.makeText(context, "This layout does not support selection", Toast.LENGTH_SHORT).show();
    }

    private void positionOverlay() {
        if(view instanceof ConstraintLayout) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone((ConstraintLayout)this.view);
            constraintSet.connect(R.id.recordingOverlay, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
            constraintSet.connect(R.id.recordingOverlay, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
            constraintSet.connect(R.id.recordingOverlay, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
            constraintSet.connect(R.id.recordingOverlay, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
            constraintSet.constrainDefaultHeight(R.id.recordingOverlay, ConstraintSet.MATCH_CONSTRAINT_SPREAD);
            constraintSet.constrainDefaultWidth(R.id.recordingOverlay, ConstraintSet.MATCH_CONSTRAINT_SPREAD);
            constraintSet.applyTo((ConstraintLayout)this.view);
        } else if(view instanceof LinearLayout) {
            // does not work
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            overlay.setLayoutParams(params);
        } else if(view instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)this.overlay.getLayoutParams();
            params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            this.overlay.setLayoutParams(params);
        }
    }

    private void setEventListener(final Context context) {
        this.overlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
            // new BubbleWebView(activity);
            int x = Math.round(event.getX());
            int y = Math.round(event.getY());

            int elementId = view.getId();

            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                int childId = child.getId();
                if (childId != elementId
                        && x > child.getLeft() && x < child.getRight()
                        && y > child.getTop() && y < child.getBottom()) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        Log.i("Match Element", Integer.toString(childId));
                        requestBubbleRender(context, childId);
                        removeOverlay();
                    }
                }
            }

            return true;
            }
        });
    }

    private void removeOverlay() {
        vg.removeView(this.overlay);
    }

    private void requestBubbleRender(Context context, int childId) {
        new BubbleWebView(context, this.view, this.helppierRequestUrl, childId);
    }
}
