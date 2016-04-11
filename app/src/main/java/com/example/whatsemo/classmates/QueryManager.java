package com.example.whatsemo.classmates;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WhatsEmo on 4/8/2016.
 */
public class QueryManager {
    private static Firebase fireData = new Firebase("https://uni-database.firebaseio.com/");

    public void AddUserData(User user, String label, List<String> data){
        Firebase addingDataRef = fireData.child("users").child(user.getUid()); //firebase reference

        if (label.equals("class")){

            addingDataRef.setValue("classes", data);

            Firebase addingToSchool = fireData.child("school").child(user.getSchoolId());
            Map<String, Object> addClass = new HashMap<String, Object>();
            addClass.put(user.getUid(), user.getName());
            for(String course : data){
                addingToSchool.child(course).child("enrolled").updateChildren(addClass);
            }

        }
        else if(label.equals("interest")){
            addingDataRef.setValue("interests",data);

            Firebase addingToSchoolInterests = fireData.child("school").child(user.getSchoolId()).child("interests");
            Map<String, Object> addUser = new HashMap<String, Object>();
            addUser.put(user.getUid(), user.getName());
            for (String interest: data){
                addingToSchoolInterests.child(interest).updateChildren(addUser);
            }

        }
        else if(label.equals("group")){
            ;
        }
    }


}
