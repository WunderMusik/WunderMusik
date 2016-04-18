package ru.bmstu.wundermusik;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.malinskiy.superrecyclerview.swipe.SparseItemRemoveAnimator;
import com.malinskiy.superrecyclerview.swipe.SwipeDismissRecyclerViewTouchListener;

import java.util.ArrayList;

import ru.bmstu.wundermusik.adapters.TrackListAdapter;

public abstract class BaseRecycleActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SwipeDismissRecyclerViewTouchListener.DismissCallbacks {

    private SuperRecyclerView          mRecycler;
    private TrackListAdapter mAdapter;
    private SparseItemRemoveAnimator   mSparseAnimator;
    private RecyclerView.LayoutManager mLayoutManager;
    private Handler                    mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        ArrayList<String> list = new ArrayList<>();
        mAdapter = new TrackListAdapter(list);

        mRecycler = (SuperRecyclerView) findViewById(R.id.list);
        mLayoutManager = getLayoutManager();
        mRecycler.setLayoutManager(mLayoutManager);

        boolean dismissEnabled = isSwipeToDismissEnabled();
        if (dismissEnabled) {
            mRecycler.setupSwipeToDismiss(this);
            mSparseAnimator = new SparseItemRemoveAnimator();
            mRecycler.getRecyclerView().setItemAnimator(mSparseAnimator);
        }
        mHandler = new Handler(Looper.getMainLooper());
        mRecycler.setAdapter(mAdapter);

        mRecycler.setRefreshListener(this);
        mRecycler.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
    }

    protected abstract int getLayoutId();

    protected abstract boolean isSwipeToDismissEnabled();

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    @Override
    public void onRefresh() {
        Log.d("Refresh", "Refresh");

        mHandler.postDelayed(new Runnable() {
            public void run() {
                mAdapter.add("New stuff");
            }
        }, 2000);
    }

    @Override
    public boolean canDismiss(int position) {
        return true;
    }

    @Override
    public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            mSparseAnimator.setSkipNext(true);
            mAdapter.remove(position);
        }
    }
}
