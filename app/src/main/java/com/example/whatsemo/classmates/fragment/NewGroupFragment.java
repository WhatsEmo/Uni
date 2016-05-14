package com.example.whatsemo.classmates.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.whatsemo.classmates.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by marcelo on 5/13/2016.
 */
public class NewGroupFragment extends DialogFragment {

    @Bind(R.id.new_group_name)
    EditText newGroupName;

    @Bind(R.id.search_members)
    SearchView searchView;

    @Bind(R.id.friends_result_view)
    RecyclerView recyclerView;

    public NewGroupFragment(){
        // Required empty public constructor
    }

    public static NewGroupFragment newInstance() {
        Bundle args = new Bundle();
        NewGroupFragment fragment = new NewGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_group_layout, container, false);
        return view;
    }
}
