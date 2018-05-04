package com.aslammaududy.realtimetranslator;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class CallDialogFragment extends DialogFragment {


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
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_call_dialog, container, false);
    }

}
