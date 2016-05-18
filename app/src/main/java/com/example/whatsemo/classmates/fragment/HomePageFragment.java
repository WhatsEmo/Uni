package com.example.whatsemo.classmates.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.whatsemo.classmates.ImageHandler;
import com.example.whatsemo.classmates.MainActivity;
import com.example.whatsemo.classmates.NotificationActivity;
import com.example.whatsemo.classmates.R;
import com.example.whatsemo.classmates.adapter.ProfileCourseAdapter;
import com.example.whatsemo.classmates.adapter.ProfileInterestAdapter;
import com.example.whatsemo.classmates.adapter.SchedulingAdapter;
import com.example.whatsemo.classmates.model.Course;
import com.example.whatsemo.classmates.model.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomePageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageFragment extends Fragment {
    private static final String ARG_PAGE = "ARG_PAGE";
    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;
    private static final int AMOUNT_OF_HOURS_TO_DISPLAY = 13;
    private final static int STARTING_HOUR = 8;
    private final static int IN_HOME = 0;

    private int mPage;
    private OnFragmentInteractionListener mListener;
    public ImageHandler imageHandler;
    private Firebase ref;
    private Firebase triggerRef;
    private int day;
    private User appUser;

    private ArrayList<Course> userCourses = new ArrayList<Course>();
    private ArrayList<String> userInterests = new ArrayList<String>();
    private ArrayList<Boolean> hasFreeTime = new ArrayList<Boolean>(Collections.nCopies(AMOUNT_OF_HOURS_TO_DISPLAY, false));;
    private ArrayList<Integer> freeTimeInHours = new ArrayList<Integer>();

    private LinearLayoutManager coursesLayoutManager;
    private LinearLayoutManager interestsLayoutManager;
    private LinearLayoutManager scheduleLayoutManager;

    private ProfileCourseAdapter courseAdapter;
    private ProfileInterestAdapter interestAdapter;
    private SchedulingAdapter scheduleAdapter;

    @OnClick(R.id.logoutButton)
    public void logout(){
        ref.unauth();
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }

    @Bind(R.id.monday_button)
    TextView mondayButton;

    @Bind(R.id.tuesday_button)
    TextView tuesdayButton;

    @Bind(R.id.wednesday_button)
    TextView wednesdayButton;

    @Bind(R.id.thursday_button)
    TextView thursdayButton;

    @Bind(R.id.friday_button)
    TextView fridayButton;

    @Bind(R.id.homeUserName)
    TextView userName;

    @Bind(R.id.homeUserMajor)
    TextView userMajor;

    @Bind(R.id.profile_picture)
    ImageButton profilePicture;

    @Bind(R.id.classes_recycler_view)
    RecyclerView coursesRecyclerView;

    @Bind(R.id.interests_recycler_view)
    RecyclerView interestsRecyclerView;

    @Bind(R.id.schedule_recylcer_view)
    RecyclerView scheduleRecyclerView;

    @OnClick(R.id.profile_picture)
    public void changeProfilePicture(){
        selectPicture();
    }

    @Bind(R.id.friend_request_button)
    Button friendRequestButton;

    @OnClick(R.id.friend_request_button)
    public void viewNotifications(){
        startNotificationActivity();
    }

    @OnClick(R.id.monday_button)
    public void setMondayButton(){
        day = Calendar.MONDAY;
        setTextColors(day);
        triggerRef.setValue("1");
        triggerRef.setValue(null);
    }

    @OnClick(R.id.tuesday_button)
    public void setTuesdayButton(){
        day = Calendar.TUESDAY;
        setTextColors(day);
        triggerRef.setValue("1");
        triggerRef.setValue(null);
    }

    @OnClick(R.id.wednesday_button)
    public void setWednesdayButton(){
        day = Calendar.WEDNESDAY;
        setTextColors(day);
        triggerRef.setValue("1");
        triggerRef.setValue(null);
    }

    @OnClick(R.id.thursday_button)
    public void setThursdayButton(){
        day = Calendar.THURSDAY;
        setTextColors(day);
        triggerRef.setValue("1");
        triggerRef.setValue(null);
    }

    @OnClick(R.id.friday_button)
    public void setFridayButton(){
        day = Calendar.FRIDAY;
        setTextColors(day);
        triggerRef.setValue("1");
        triggerRef.setValue(null);
    }

    public HomePageFragment() {
        // Required empty public constructor
    }

    public static HomePageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        HomePageFragment fragment = new HomePageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page_layout, container, false);
        ButterKnife.bind(this, view);
        ref = new Firebase(getResources().getString(R.string.database));

        appUser = ((MainActivity)getActivity()).getUser();

        imageHandler = new ImageHandler(this.getActivity());

        day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        setTextColors(day);
        triggerRef = ref.child(getString(R.string.database_users_key))
                .child(appUser.getUid())
                .child("1");

        if (ref.getAuth() != null){
            String uid = ref.getAuth().getUid();
            ref.child(getResources().getString(R.string.database_users_key)).child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    String name = snapshot.child(getResources().getString(R.string.user_name_key)).getValue(String.class);
                    userName.setText(name);
                    if(snapshot.child("picture").exists()) {
                        Bitmap bm = imageHandler.convertByteArrayToBitmap(snapshot.child("picture").getValue().toString());
                        profilePicture.setImageBitmap(bm);
                    }

                    if(snapshot.child(getString(R.string.user_interests_key)).exists()){
                        ArrayList<String> checkInterests = (ArrayList<String>) snapshot.child(getString(R.string.user_interests_key)).getValue();

                        if(userInterests.size() != checkInterests.size()){
                            userInterests.clear();
                            userInterests.addAll(checkInterests);
                            interestAdapter.notifyDataSetChanged();
                        }
                    }

                    if(snapshot.child(getString(R.string.user_courses_key)).exists()){
                        ArrayList<Course> checkCourses = new ArrayList<Course>();
                        for(Map.Entry<String,String> courseMapEntry : ((Map<String, String>) snapshot.child(getString(R.string.user_courses_key)).getValue()).entrySet()){
                            Course course = new Course(courseMapEntry.getKey(), courseMapEntry.getValue());
                            checkCourses.add(course);
                        }

                        if(checkCourses.size() != userCourses.size()){
                            userCourses.clear();
                            userCourses.addAll(checkCourses);
                            courseAdapter.notifyDataSetChanged();
                        }
                    }

                    if(snapshot.child(getString(R.string.user_schedule_key)).exists()){
                        ArrayList<Integer> checkSchedule = snapshot.child(getString(R.string.user_schedule_key)).child(Integer.toString(day)).getValue(ArrayList.class);

                        if(checkSchedule != null){
                            freeTimeInHours = checkSchedule;
                            hasFreeTime.clear();
                            hasFreeTime.addAll(new ArrayList(Collections.nCopies(AMOUNT_OF_HOURS_TO_DISPLAY, Boolean.FALSE)));
                            for (Integer time : freeTimeInHours){
                                hasFreeTime.set(time-STARTING_HOUR, Boolean.TRUE);
                            }
                        }else{
                            hasFreeTime.clear();
                            hasFreeTime.addAll(new ArrayList(Collections.nCopies(AMOUNT_OF_HOURS_TO_DISPLAY, Boolean.FALSE)));
                        }
                        scheduleAdapter.notifyDataSetChanged();
                    }
                    setUpAdapters();

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The name read failed: " + firebaseError.getMessage());
                }
            });

        }
        else{
            System.out.println("Home Page: Authentication failed");
        }
        setUpAdapters();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!isAdded()){
            return;
        }
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void selectPicture() {
        final String[] options = {"Take Picture", "Choose from Library", "Cancel"};
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getActivity());
        dialogBuilder.setTitle("Choose Profile Picture!");
        dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        //From Camera
                        Intent startCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(startCameraIntent, REQUEST_CAMERA);
                        break;
                    case 1:
                        //From Library
                        Intent startLibraryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startLibraryIntent.setType("image/");
                        startActivityForResult(Intent.createChooser(startLibraryIntent, "Select File"), SELECT_FILE);
                        break;
                    case 2:
                        //Cancel
                        dialog.dismiss();
                        break;
                }
            }
        });
        dialogBuilder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Result code = either RESULT_OK = user selected image and RESULT_CANCEL = user didn't select image
        if(resultCode == getActivity().RESULT_OK){
            if(requestCode == REQUEST_CAMERA){
                Bitmap bm = imageHandler.fromCamera(data);
                profilePicture.setImageBitmap(bm);
                imageHandler.uploadToFirebase(ref, bm, "picture");

            }else if(requestCode == SELECT_FILE){
                Bitmap bm = imageHandler.fromLibrary(data);
                profilePicture.setImageBitmap(bm);
                imageHandler.uploadToFirebase(ref, bm, "picture");
            }
        }
    }


    private void startNotificationActivity() {
        /*
        NotificationFragment notificationFragment = new NotificationFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_layout, notificationFragment, null)
                .addToBackStack(null)
                .commit();
*/
        Intent startNotificationActivityIntent = new Intent(this.getActivity(), NotificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("appUser", appUser);
        startNotificationActivityIntent.putExtras(bundle);
        this.getActivity().startActivity(startNotificationActivityIntent);
    }

    private void setUpAdapters() {

        //**********COURSES***********
        coursesLayoutManager = new LinearLayoutManager(getActivity());
        coursesLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        courseAdapter = new ProfileCourseAdapter(userCourses, getActivity(), getFragmentManager(),ref, appUser);
        coursesRecyclerView.setAdapter(courseAdapter);

        //**********INTEREST***********
        interestsLayoutManager = new LinearLayoutManager(getActivity());
        interestsLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        interestsRecyclerView.setLayoutManager(interestsLayoutManager);

        interestAdapter = new ProfileInterestAdapter(userInterests, getActivity(), getFragmentManager(), ref, appUser);
        interestsRecyclerView.setAdapter(interestAdapter);


        scheduleLayoutManager = new LinearLayoutManager(getActivity());
        scheduleLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        scheduleRecyclerView.setLayoutManager(scheduleLayoutManager);

        scheduleAdapter = new SchedulingAdapter(hasFreeTime, getActivity(), ref, day, IN_HOME);
        scheduleRecyclerView.setAdapter(scheduleAdapter);

    }


    private void setTextColors(int date) {
        mondayButton.setTextColor(getResources().getColor(R.color.gray));
        tuesdayButton.setTextColor(getResources().getColor(R.color.gray));
        wednesdayButton.setTextColor(getResources().getColor(R.color.gray));
        thursdayButton.setTextColor(getResources().getColor(R.color.gray));
        fridayButton.setTextColor(getResources().getColor(R.color.gray));
        switch (date){
            case Calendar.MONDAY:
                mondayButton.setTextColor(getResources().getColor(R.color.realOrange));
                break;
            case Calendar.TUESDAY:
                tuesdayButton.setTextColor(getResources().getColor(R.color.realOrange));
                break;
            case Calendar.WEDNESDAY:
                wednesdayButton.setTextColor(getResources().getColor(R.color.realOrange));
                break;
            case Calendar.THURSDAY:
                thursdayButton.setTextColor(getResources().getColor(R.color.realOrange));
                break;
            case Calendar.FRIDAY:
                fridayButton.setTextColor(getResources().getColor(R.color.realOrange));
                break;
            default:
                break;
        }
    }
}
