package com.example.whatsemo.classmates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by WhatsEmo on 3/14/2016.
 */
public class LoginActivity extends Activity {

    EditText emailBox;
    EditText passwordBox;
    Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity = this;
        setContentView(R.layout.login_layout);
        emailBox = (EditText) findViewById(R.id.emailField);
        passwordBox = (EditText) findViewById(R.id.passwordField);
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

}
