package com.example.whatsemo.classmates;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class SignUpActivity extends Activity {

    @Bind(R.id.firstNameField)
    public EditText firstNameBox;

    @Bind(R.id.lastNameField)
    public EditText lastNameBox;

    @Bind(R.id.schoolNameFirst)
    public EditText schoolNameBox;

    @Bind(R.id.singUpEmailField)
    public EditText emailBox;

    @Bind(R.id.signUpPasswordField)
    public EditText passwordBox;

    @OnClick(R.id.signUpScreenButton)
    public void signUp(View view){
        signUpToFireBase();
    }

    @OnClick(R.id.loginButtonSignUpScreen)
    public void goBack(View view){
        onBackPressed();
    }

    private Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);
        thisActivity = this;
    }


    private void signUpToFireBase(){

        String email = emailBox.getText().toString();
        String password = passwordBox.getText().toString();
        String firstName = firstNameBox.getText().toString();
        String lastName = lastNameBox.getText().toString();
        String schoolName = schoolNameBox.getText().toString();

        Firebase ref = new Firebase("https://uni-database.firebaseio.com/");
        ref.createUser(email,password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                thisActivity.finish();
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }

}
