package com.example.whatsemo.classmates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.whatsemo.classmates.model.User;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends Activity {

    @Bind(R.id.firstNameField)
    public EditText firstNameBox;

    @Bind(R.id.lastNameField)
    public EditText lastNameBox;

    @Bind(R.id.singUpEmailField)
    public EditText emailBox;

    @Bind(R.id.signUpPasswordField)
    public EditText passwordBox;

    @OnClick(R.id.signUpScreenButton)
    public void signUp(){
        signUpToFireBase();
    }

    @OnClick(R.id.loginButtonSignUpScreen)
    public void goBack(){
        onBackPressed();
    }

    private Activity thisActivity;
    private Firebase ref;
    static final int USER_LOGGED_IN_THROUGH_SIGNUP = 0;
    static final int USER_DID_NOT_LOG_IN = 1;
    static final int SIGN_UP_RESULTS = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);
        thisActivity = this;
        Firebase.setAndroidContext(this);
        ButterKnife.bind(this);
        ref = new Firebase(getResources().getString(R.string.database));
    }


    private void signUpToFireBase(){

        final String email = emailBox.getText().toString();
        final String password = passwordBox.getText().toString();
        final String name = firstNameBox.getText().toString() + ' ' + lastNameBox.getText().toString();
        final String schoolId;



        if(email.isEmpty() || password.isEmpty() || name.isEmpty()){
            // If anything is empty, do something here.
        }else { //Only happens if all the fields are filled in

            //Regular expression that matches @schoolID.edu or @something.schoolID.edu
            String pattern = "([@\\.])\\w+(\\.)edu";
            Pattern reg = Pattern.compile(pattern);
            Matcher m = reg.matcher(email);
            if(m.find()){
                schoolId = m.group(0).substring(1, m.group(0).length()-4);
            }else{
                emailBox.setHint("Enter .edu email!");
                emailBox.setText("");
                return;
            }

            ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> result) {
                    final Map<String, Object> userInfo = result;

                    //WE HAVE TO LOG USERS IN IF WE WANT TO ADD DATA BECAUSE OF THE RULES WE PLACED
                    ref.authWithPassword(email,password, new Firebase.AuthResultHandler(){
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            ref.child(getResources().getString(R.string.database_users_key)).setValue(userInfo.get(getResources().getString(R.string.user_id_key)).toString()); // this creates a new user (with their uid) in database

                            Firebase newUserRef = ref.child(getResources().getString(R.string.database_users_key)).child(userInfo.get(getResources().getString(R.string.user_id_key)).toString()); //firebase reference
                            User user = new User(name, email, schoolId);

                            //Begin setting First Name, Last Name, School Name, etc
                            /*
                            newUserRef.child(getResources().getString(R.string.user_name_key)).setValue(name);
                            newUserRef.child(getResources().getString(R.string.user_school_key)).setValue(schoolId);
                            newUserRef.child(getResources().getString(R.string.user_email_key)).setValue(email);
                            newUserRef.child(getResources().getString(R.string.user_interests_key)).setValue(null);
                            newUserRef.child(getResources().getString(R.string.user_courses_key)).setValue(null);
                            */
                            newUserRef.setValue(user);
                            newUserRef.child("isTutorialDone").setValue(false);

                            //Ends this Acitivty -> LoginActivity -> MainActivity
                            Intent returnIntent = new Intent();
                            setResult(USER_LOGGED_IN_THROUGH_SIGNUP, returnIntent);
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

                @Override
                public void onError(FirebaseError firebaseError) {
                    //Do visual pop-up when the user encounters the error.
                    //firebaseError.message() <- method to obtain error information
                }
            });
        }
    }

    @Override
    public void onBackPressed(){
        Intent returnIntent = new Intent();
        setResult(USER_DID_NOT_LOG_IN, returnIntent);
        finish();
    }

}
