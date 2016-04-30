package com.example.whatsemo.classmates;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WhatsEmo on 4/29/2016.
 */
public class SearchActivity extends Activity {

    @Bind(R.id.search_results)
    ListView searchResults;

    @Bind(R.id.search_back_button)
    ImageButton searchBackButton;

    @Bind(R.id.search_text)
    EditText searchText;

    @OnClick(R.id.search_back_button)
    public void back(){
        onBackPressed();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        ButterKnife.bind(this);
    }

    public void queryFireBase(String searchString){

    }
}
