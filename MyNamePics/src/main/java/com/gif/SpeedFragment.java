package com.gif;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.formationapps.nameart.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpeedFragment extends Fragment {
    private SeekBar speed_SeekBar;


    public SpeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_speed, container, false);
        speed_SeekBar = (SeekBar)view.findViewById(R.id.speed_seekBar);
        speed_SeekBar.setMax(50);
        speed_SeekBar.setProgress(10);
        speed_SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int min = 1;
                int time;
                if (progress<1) {
                    time = min; // we not interested go on 0.
                }else {
                    time=progress;
                }

                passData(time*100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;

    }
   OnDelaySelectListener mOnDelaySelectListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnDelaySelectListener = (OnDelaySelectListener) context;
    }
    public void passData(int delay){
        mOnDelaySelectListener.onDelaySelectListener(delay);

    }
}
