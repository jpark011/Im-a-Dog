package com.cs446w18.a16.imadog.views;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs446w18.a16.imadog.R;

import java.util.ArrayList;

/**
 * Created by Jean-Baptiste on 2018-03-25.
 */

public class TabBarFragment extends Fragment {

    /* ----------------------------- ATTRIBUTES ----------------------------- */

    ArrayList<ImageView> selectors;


    /* ----------------------------- SETUP ----------------------------- */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tabbar, container, false);

        // Selectors
        selectors = new ArrayList<>();

        ImageView selector1 = view.findViewById(R.id.selector1);
        selectors.add(selector1);
        ImageView selector2 = view.findViewById(R.id.selector2);
        selectors.add(selector2);
        ImageView selector3 = view.findViewById(R.id.selector3);
        selectors.add(selector3);
        ImageView selector4 = view.findViewById(R.id.selector4);
        selectors.add(selector4);

        setPage(0);

        return view;
    }

    /* ----------------------------- METHODS ----------------------------- */

    public void setPage(int position) {

        for (int i=0; i<selectors.size(); i++) {
            selectors.get(i).setVisibility(position == i ? View.VISIBLE : View.INVISIBLE);
        }

    }


}
