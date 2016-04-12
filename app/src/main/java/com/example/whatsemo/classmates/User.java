package com.example.whatsemo.classmates;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by WhatsEmo on 3/5/2016.
 *
 * Used to store User data
 */
public class User implements Parcelable {
    private String uid;
    private String name;
    private String schoolId;
    private String email;
    private List<String> interests;

    public User(String uid,
                String name,
                String school,
                String email,
                List<String> interests
                ){
        this.uid = uid;
        this.name = name;
        this.schoolId = school;
        this.email = email;
        this.interests = interests;
    }

    protected User(Parcel in) {
        uid = in.readString();
        name = in.readString();
        schoolId = in.readString();
        email = in.readString();
        interests = in.createStringArrayList();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeString(schoolId);
        dest.writeString(email);
        dest.writeStringList(interests);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
