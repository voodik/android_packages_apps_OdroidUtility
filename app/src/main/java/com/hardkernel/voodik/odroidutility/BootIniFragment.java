package com.hardkernel.voodik.odroidutility;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class BootIniFragment extends Fragment {

    public static BootIniFragment newInstance() {
        return new BootIniFragment();
    }

    protected static final String TAG = Constants.TAG;
    private Activity mActivity;
    public MainActivity mAcct;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bootini, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        mAcct = (MainActivity)getActivity();
        TextView bootiniTextView = view.findViewById(R.id.bootiniTv);

        BootIniHelper bh = mAcct.mBh;

        ArrayList<String> StringArray = bh.writeini();
        bh.syncprefs();

        String line;
        for (int i=0; i<StringArray.size();i++){
            line = StringArray.get(i).trim();

            if (line.isEmpty() || line.startsWith("#")) continue;

            if (line.contains("bootcmd") || line.contains("bootargs")) continue;

            bootiniTextView.append(line);
            bootiniTextView.append("\n");
        }
        bootiniTextView.setMovementMethod(new ScrollingMovementMethod());
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            mActivity=(Activity) context;
        }
    }
}
