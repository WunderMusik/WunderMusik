package ru.bmstu.wundermusik.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;

import ru.bmstu.wundermusik.R;


public class SearchListFragment extends Fragment {
    private OnSearchListListener mListener;
    private SuperRecyclerView mRecycler;
    public SearchListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<String> list = new ArrayList<>();


        return inflater.inflate(R.layout.fragment_search_list, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchListListener) {
            mListener = (OnSearchListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSearchListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnSearchListListener {
        void onFragmentInteraction(Uri uri);
    }
}
