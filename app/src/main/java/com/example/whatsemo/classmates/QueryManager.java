package com.example.whatsemo.classmates;

import com.example.whatsemo.classmates.model.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WhatsEmo on 4/8/2016.
 */
public class QueryManager {
    private static Firebase fireData = new Firebase("https://uni-database.firebaseio.com/");

    public Map <String, String> courseMap;

    public List<String> courseList;

    /*
        Function adds lists of either Courses/Interests/Groups into the database
        Target locations: User, School->Interests/Classes/Groups
        This is only used when the Tutorial Activity is called.
     */
    public void setListData(User user, String label, List<String> data){
        Firebase userDataRef = fireData.child("users").child(user.getUid()); //firebase reference
        userDataRef.child(label).setValue(data);
    }

    public void setMapData(User user, String label, Map<String, String> data){
        Firebase userDataRef = fireData.child("users").child(user.getUid());
        userDataRef.child(label).setValue(data);
    }

    /*
    Set user's 'courses' field in the database as a map with course codes as keys and course names as values
     */
    public void setUserCourses(User user, List<String> courses){
        courseList = courses;
        courseMap = new HashMap<String, String>();
        final String uid = user.getUid();

        fireData.child("schools").child(user.getSid()).child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (String course : courseList) {
                    String name = snapshot.child(course).child("name").getValue(String.class);
                    courseMap.put(course, name);
                }
                fireData.child("users").child(uid).child("courses").setValue(courseMap);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The course read failed: " + firebaseError.getMessage());
            }
        });

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
            user.setSid(data);
        }
        else if(!(label.equals("isTutorialDone"))){
            //show some sort of error
        }

        userDataRef.child(label).setValue(data);
        return user;
    }

    public void updateRoster(User user, String label, List<String> data){
        Firebase schoolRef = fireData.child("schools").child(user.getSid());
        Map<String, Object> addUser = new HashMap<String, Object>();
        addUser.put(user.getUid(), user.getName());
        for(String key : data){
            if(label.equals("courses")) {
                schoolRef.child(label).child(key).child("roster").updateChildren(addUser);
            }else{
                //If interests
                schoolRef.child(label).child(key).updateChildren(addUser);
            }
        }
    }

/*
    public User modifyingUserListData(User user, String label, List<String> data){
        Firebase addingDataRef = fireData.child(getResources().getString(R.string.database_users_key)).child(user.getUid()); //firebase reference

        if(label.equals(getResources().getString(R.string.user_courses_key))){
            addingDataRef.child(getResources().getString(R.string.user_courses_key)).setValue(getResources().getString(R.string.user_courses_key), data);

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
