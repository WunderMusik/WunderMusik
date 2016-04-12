package ru.bmstu.wundermusik;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

public class ListActivity extends BaseRecycleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_list;
    }

    @Override
    protected boolean isSwipeToDismissEnabled() {
        return true;
    }

    @Override
    protected LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }
}