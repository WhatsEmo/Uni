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

    public QueryManager(Firebase data, User appUser) {
        fireData = data;
    }

    /*
        Function adds lists of either Courses/Interests/Groups into the database
        Target locations: User, School->Interests/Classes/Groups
        This is only used when the Tutorial Activity is called.
     */
    public void tutorialHelper(User user, String label, List<String> data){
        Firebase userDataRef = fireData.child("users").child(user.getUid()); //firebase reference

        if (label.equals("classes")){

            userDataRef.child("classes").setValue("classes", data);

            Firebase addingToSchool = fireData.child("school").child(user.getSchoolId());
            Map<String, Object> addClass = new HashMap<String, Object>();
            addClass.put(user.getUid(), user.getName());
            for(String course : data){
                addingToSchool.child(course).child("enrolled").updateChildren(addClass);
            }

        }
        else if(label.equals("interests")){
            userDataRef.child("interests").setValue("interests", data);

            Firebase addingToSchoolInterests = fireData.child("school").child(user.getSchoolId()).child("interests");
            Map<String, Object> addUser = new HashMap<String, Object>();
            addUser.put(user.getUid(), user.getName());
            for (String interest: data){
                addingToSchoolInterests.child(interest).updateChildren(addUser);
            }

        }
        else if(label.equals("groups")){
            userDataRef.child("groups").setValue("groups", data);

            Firebase addingToSchoolInterests = fireData.child("school").child(user.getSchoolId()).child("groups");
            Map<String, Object> addUser = new HashMap<String, Object>();
            addUser.put(user.getUid(), user.getName());
            for (String group: data){
                addingToSchoolInterests.child(group).updateChildren(addUser);
            }
        }
    }

    /*
        Function allows user to edit only these categories in DB:
            email, name, school, isTutorialDone
     */
    public User modifyUserStringData(User user, String label, String data){
        Firebase addingDataRef = fireData.child("users").child(user.getUid()); //firebase reference

        if(label.equals("email")){
            user.setEmail(data);
            addingDataRef.child(label).setValue(data);
        }
        else if(label.equals("name")){
            user.setName(data);
            addingDataRef.child(label).setValue(data);
        }
        else if(label.equals("school")){
            user.setSchoolId(data);
            addingDataRef.child(label).setValue(data);
        }
        else if(label.equals("isTutorialDone")){
            addingDataRef.child(label).setValue(data);
        }else{
            //show some sort of error
        }
        return user;
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
