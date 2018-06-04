package com.aslammaududy.realtimetranslator;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aslammaududy.realtimetranslator.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class CallDialogFragment extends DialogFragment {


    private Dialog d = getDialog();
    private RadioGroup langGroupCall;
    private User user, user1;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference dbReference;
    private String caller;

    public CallDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CallDialog);
    }

    @Override
    public void onStart() {

        super.onStart();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commit();
        } catch (IllegalStateException e) {
            Log.d("ABSDIALOGFRAG", "Exception", e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_dialog, container, false);

        AppCompatImageButton callBtn = view.findViewById(R.id.call);
        AppCompatImageButton endBtn = view.findViewById(R.id.call_end);

        final TextView callerName = view.findViewById(R.id.caller_name);

        langGroupCall = view.findViewById(R.id.lang_group_call);
        final AppCompatRadioButton chinese = view.findViewById(R.id.lang_china_call);
        final AppCompatRadioButton english = view.findViewById(R.id.lang_english_call);
        final AppCompatRadioButton indonesian = view.findViewById(R.id.lang_indonesian_call);

        user = new User();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference();

        langGroupCall.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (langGroupCall.getCheckedRadioButtonId()) {
                    case R.id.lang_china_call:
                        user.setLang("zh");
                        break;
                    case R.id.lang_english_call:
                        user.setLang("en");
                        break;
                    case R.id.lang_indonesian_call:
                        user.setLang("id");
                        break;
                }
            }
        });

        dbReference.child(user.NODE_USERS).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user1 = dataSnapshot.getValue(User.class);
                if (user1 != null) {
                    caller = user1.getCaller();
                    switch (user1.getLang()) {
                        case "zh":
                            chinese.setVisibility(View.GONE);
                            break;
                        case "en":
                            english.setVisibility(View.GONE);
                            break;
                        case "id":
                            indonesian.setVisibility(View.GONE);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbReference.child(user.NODE_USERS).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    user1 = snapshot.getValue(User.class);
                    if (user1 != null) {
                        if (user1.getUid().equals(caller)) {
                            callerName.setText(user1.getName() + "");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (firebaseUser != null) {
                    user.setUid(firebaseUser.getUid());
                    dbReference.child(user.NODE_USERS).child(user.getUid()).child(user.NODE_CALL).setValue(User.ON_CALL);
                    dbReference.child(user.NODE_USERS).child(user.getUid()).child(user.NODE_LANG).setValue(user.getLang());

                    Intent intent = new Intent(getActivity(), SpeakActivity.class);
                    intent.putExtra("dataLoad", new String[]{user.getLang(), caller});
                    startActivity(intent);
                }

                dismiss();
            }
        });

        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (firebaseUser != null) {
                    user.setUid(firebaseUser.getUid());
                    user.setName(firebaseUser.getDisplayName());
                    dbReference.child(user.NODE_USERS).child(user.getUid()).child(user.NODE_CALL).setValue(User.INITIAL_CALL);
                }
                dismiss();
            }
        });
        return view;
    }

}
