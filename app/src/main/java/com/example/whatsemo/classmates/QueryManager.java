package com.example.whatsemo.classmates;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WhatsEmo on 4/8/2016.
 */
public class QueryManager {
    private static Firebase fireData;

    public QueryManager(Firebase data) {
        fireData = data;
    }

    /*
        Function adds lists of either Courses/Interests/Groups into the database
        Target locations: User, School->Interests/Classes/Groups
        This is only used when the Tutorial Activity is called.
     */
    public void setListData(User user, String label, List<String> data){
        Firebase userDataRef = fireData.child("users").child(user.getUid()); //firebase reference
        userDataRef.child(label).setValue(label, data);
    }

    /*
        Function allows user to edit only these categories in DB:
            email, name, school, isTutorialDone
     */
    public User setUserStringData(User user, String label, String data){
        Firebase userDataRef = fireData.child("users").child(user.getUid()); //firebase reference

        if(label.equals("email")){
            user.setEmail(data);
        }
        else if(label.equals("name")){
            user.setName(data);
        }
        else if(label.equals("school")){
            user.setSchoolId(data);
        }
        else if(!(label.equals("isTutorialDone"))){
            //show some sort of error
        }

        userDataRef.child(label).setValue(data);
        return user;
    }

    public static void updateRoster(User user, String label, List<String> data){
        Firebase schoolRef = fireData.child("schools").child(user.getSchoolId());
        Map<String, Object> addUser = new HashMap<String, Object>();
        addUser.put(user.getUid(), user.getName());
        for(String key : data){
            schoolRef.child(label).child(key).child("roster").updateChildren(addUser);
        }
    }

/*
    public User modifyingUserListData(User user, String label, List<String> data){
        Firebase addingDataRef = fireData.child("users").child(user.getUid()); //firebase reference

        if(label.equals("classes")){
            addingDataRef.child("classes").setValue("classes", data);

            Firebase addingToSchool = fireData.child("school").child(user.getSchoolId());
            user.setEmail(data);
            addingDataRef.child(label).setValue(data);
        }
        else if(label.equals("interests")){
            user.setName(data);
            addingDataRef.child(label).setValue(data);
        }
        else if(label.equals("groups")){
            user.setSchoolId(data);
            addingDataRef.child(label).setValue(data);
        }else if(label.equals("isTutorialDone")){
            addingDataRef.child(label).setValue(data);
        }else{
            //show some sort of error
        }
        return user;
    }

*/

}
