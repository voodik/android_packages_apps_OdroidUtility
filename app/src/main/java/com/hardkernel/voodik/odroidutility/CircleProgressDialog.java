package com.hardkernel.voodik.odroidutility;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CircleProgressDialog extends DialogFragment {

    public CircleProgressDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static CircleProgressDialog newInstance(String title) {
        CircleProgressDialog frag = new CircleProgressDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Please Wait... Extracting zip file... ");
        View v = inflater.inflate(R.layout.dialog_circle, null);

        return v;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        TextView textView_title = (TextView) view.findViewById(R.id.dialogTitle);
        textView_title.setGravity(Gravity.CENTER);
        String title = getArguments().getString("title", "");
        if (title.length()>0){
            textView_title.setText(title);
        } else {
            textView_title.setVisibility(View.INVISIBLE);
        }

    }

}
