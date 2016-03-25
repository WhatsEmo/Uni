package com.example.whatsemo.classmates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    static final int USER_LOGGED_IN_THROUGH_SIGNUP = 0;
    static final int USER_DID_NOT_LOG_IN = 1;
    static final int SIGN_UP_RESULTS = 2;

    private Activity thisActivity;
    private Firebase ref;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity = this;
        setContentView(R.layout.login_layout);
        Firebase.setAndroidContext(this);
        ButterKnife.bind(this);
        ref = new Firebase("https://uni-database.firebaseio.com/");
    }

    //When Users click on the Sign Up button, it will start the SignUpActivity
    private void startSignUpAcitivty(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, SIGN_UP_RESULTS);
    }

    //When users click on the Login Button
    private void logIn(){
        String email = emailBox.getText().toString();
        String password = passwordBox.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            //Do this later
            //generate text messages next to EditText boxes
        }else{
            ref.authWithPassword(email,password, new Firebase.AuthResultHandler(){
                @Override
                public void onAuthenticated(AuthData authData) {

                    //Checks if user has done the tutorial, if not we start the tutorial.
                    //After tutorial is done, we exit to MainActivity
                    //first checks if the isTutorialDone variable a null because it would crash otherwise
                    if(ref.getAuth().getProviderData().get("isTutorialDone") != null &&
                            ref.getAuth().getProviderData().get("isTutorialDone").equals(false)) {
                        //Going to implement this.
                    }
                    //Ends this Acitivty and goes back to the Main Activity
                    thisActivity.finish();
                }
                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    // there was an error
                    //Put something here to let the user know that there was an error
                    //Probably a visual pop-up
                    System.out.println("Error: " + firebaseError.getMessage());
                }
            });
        }
    }

    @Override
    public void onBackPressed(){
        //Do nothing because we want the user to log in
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        /*
            Starts the SignUpActivity and sees if the user has created an account (which will automatically log the user in)
            If the user didn't create an account and ended the SignUpActivity, we do something different

            Don't understand? :
            http://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android/10407371#10407371
         */

        if(requestCode == SIGN_UP_RESULTS){
            if(resultCode == USER_LOGGED_IN_THROUGH_SIGNUP){
                //first checks if the isTutorialDone variable a null because it would crash otherwise
                if(ref.getAuth().getProviderData().get("isTutorialDone") != null &&
                        ref.getAuth().getProviderData().get("isTutorialDone").equals(false)){

                    //start tutorial
                }
                thisActivity.finish();
            }
            if(resultCode == USER_DID_NOT_LOG_IN){
                //Don't do anything
            }
        }
    }
}
