package com.example.androidcodes.autosmoothscrollrecyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /*
    *   Also Check Following Links :
    *
    *   https://blog.stylingandroid.com/scrolling-recyclerview-part-1/
    *   https://blog.stylingandroid.com/scrolling-recycler-view-part-2/
    *   https://blog.stylingandroid.com/scrolling-recyclerview-part-3/
    *
    * */

    private ArrayList<Integer> arrayList = null;

    private Activity activity;

    private RecyclerView rv_autoScrollList = null;

    private LinearLayoutManager layoutManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;

        arrayList = new ArrayList<>();

        for (int i = 0; i < 20; i++) {

            arrayList.add(i);

        }

        rv_autoScrollList = (RecyclerView) findViewById(R.id.rv_autoScrollList);
        rv_autoScrollList.setHasFixedSize(true);

        setUpRecyclerView();

    }

    private void autoScroll() {

        final int speedScroll = 1000;

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            int count = 0;

            @Override
            public void run() {

                if (count == rv_autoScrollList.getAdapter().getItemCount())
                    count = 0;
                if (count <= rv_autoScrollList.getAdapter().getItemCount()) {

                    rv_autoScrollList.smoothScrollToPosition(++count);

                    handler.postDelayed(this, speedScroll);

                }
            }
        }, speedScroll);
    }

    private void setUpRecyclerView() {

        layoutManager = new LinearLayoutManager(activity) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                               int position) {

                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(activity) {

                    private static final float SPEED = 2000f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {

                        return SPEED / displayMetrics.densityDpi;

                    }
                };

                smoothScroller.setTargetPosition(recyclerView.getAdapter().getItemCount());
                //smoothScroller.setTargetPosition(position); //This Will Scroll Recyclerview From Top to Bottom And Automatically Bottom To top

                startSmoothScroll(smoothScroller);

            }
        };

        autoScroll();

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_autoScrollList.setLayoutManager(layoutManager);
        rv_autoScrollList.setAdapter(new RecyclerViewCustomAdapter(arrayList));

    }

    private class RecyclerViewCustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<Integer> arrayList = null;

        private LinearLayoutCompat.LayoutParams layoutParams = null;

        private LinearLayoutCompat view = null;

        public RecyclerViewCustomAdapter(ArrayList<Integer> arrayList) {

            this.arrayList = arrayList;

            layoutParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.
                    MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5, 5, 5, 5);

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LinearLayoutCompat layoutCompat = new LinearLayoutCompat(activity);
            layoutCompat.setLayoutParams(layoutParams);
            layoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
            layoutCompat.setGravity(Gravity.CENTER);

            return new RecyclerView.ViewHolder(layoutCompat) {

                @Override
                public String toString() {
                    return super.toString();
                }
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            view = (LinearLayoutCompat) holder.itemView.getRootView();

            if (view.getChildAt(position) == null) {

                Button textView = new Button(activity);
                textView.setLayoutParams(layoutParams);
                textView.setGravity(Gravity.CENTER);
                textView.setText(String.valueOf(position));
                textView.setAllCaps(true);
                textView.setMinHeight(36);
                textView.setTextSize(15.0f);

                view.addView(textView);

            }
        }

        @Override
        public int getItemCount() {

            return arrayList != null ? arrayList.size() : 0;

        }
    }
}
