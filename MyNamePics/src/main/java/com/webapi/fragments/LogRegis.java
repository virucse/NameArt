package com.webapi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;

/**
 * Created by caliber fashion on 8/31/2017.
 */

public class LogRegis extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.logregis_dummy, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button user = (Button) view.findViewById(R.id.user_section);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NameArtMenu) (getActivity())).addNewFragment(new UserFragment());
            }
        });
        Button feed = (Button) view.findViewById(R.id.feed_section);
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NameArtMenu) (getActivity())).addNewFragment(new FeedFragment());
            }
        });
    }
}
