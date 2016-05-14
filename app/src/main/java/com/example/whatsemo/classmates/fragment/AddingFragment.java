package com.example.whatsemo.classmates.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.whatsemo.classmates.R;

/**
 * Created by WhatsEmo on 5/13/2016.
 */
public class AddingFragment extends Fragment {
    private final static int ADD_COURSES = 0;
    private final static int ADD_INTERESTS = 1;
    private int addingType;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        addingType = getArguments().getInt(getString(R.string.home_frag_adding_info));
        super.onViewCreated(view, savedInstanceState);
    }
}
