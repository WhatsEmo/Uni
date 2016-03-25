package com.example.whatsemo.classmates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WhatsEmo on 3/14/2016.
 */
public class LoginActivity extends Activity {

    @Bind(R.id.emailField)
    public EditText emailBox;

    @Bind(R.id.passwordField)
    public EditText passwordBox;

    @OnClick(R.id.signUpButton)
    public void openSignUpScreen(){
        startSignUpAcitivty();
    }

    @OnClick(R.id.loginButton)
    public void logInNow(){
        logIn();
    }

    Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity = this;
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
    }

    //When Users click on the Sign Up button, it will start the SignUpActivity
    private void startSignUpAcitivty(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    //When users click on the Login Button
    private void logIn(){
        String email = emailBox.getText().toString();
        String password = emailBox.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            //Do this later
            //generate text messages next to EditText boxes
        }else if(password.length() <= 8){
            //Do this later
            //generate text messages next to EditText boxes
        }else{
            Firebase ref = new Firebase("https://uni-database.firebaseio.com/");
            ref.authWithPassword(email,password, new Firebase.AuthResultHandler(){
                @Override
                public void onAuthenticated(AuthData authData) {
                    //Ends this Acitivty and goes back to the Main Activity
                    thisActivity.finish();
                }
                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    // there was an error
                    //Put something here to let the user know that there was an error
                    //ie. Email already registered
                }
            });
        }
    }

    @Override
    public void onBackPressed(){
        //Do nothing because we want the user to log in
    }

}
