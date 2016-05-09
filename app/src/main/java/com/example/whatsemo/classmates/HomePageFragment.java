package com.example.whatsemo.classmates;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.whatsemo.classmates.model.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

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

    private int mPage;

    private OnFragmentInteractionListener mListener;

    public ImageHandler imageHandler;

    private Firebase ref;

    private User appUser;

    @Bind(R.id.homeUserName)
    TextView userName;

    @Bind(R.id.homeUserMajor)
    TextView userMajor;

    @Bind(R.id.profile_picture)
    ImageButton profilePicture;

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
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page_layout, container, false);
        ButterKnife.bind(this, view);
        ref = new Firebase(getResources().getString(R.string.database));

        appUser = ((MainActivity)getActivity()).getUser();

        imageHandler = new ImageHandler(this.getActivity());

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
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        Intent startNotificationActivityIntent = new Intent(this.getActivity(), NotificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("appUser", appUser);
        startNotificationActivityIntent.putExtras(bundle);
        this.getActivity().startActivity(startNotificationActivityIntent);
    }

}
