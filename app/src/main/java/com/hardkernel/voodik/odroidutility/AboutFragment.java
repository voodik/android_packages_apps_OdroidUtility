package com.hardkernel.voodik.odroidutility;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    private static final Uri sUri = Uri.parse("content://com.google.android.gsf.gservices");
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Activity activity = (Activity)getContext();
        final ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        TextView TVgsfid = view.findViewById(R.id.textView_gsfid);
        TextView GsfHelp = view.findViewById(R.id.textView_gsf_url);
        GsfHelp.setMovementMethod(LinkMovementMethod.getInstance());
        long gsfint = Long.parseLong(getGSFID(activity), 16);
        final String gsfid = Long.toString(gsfint);
        TVgsfid.setText(gsfid);
        ImageView copyText = view.findViewById(R.id.copy);
        copyText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ClipData clip = ClipData.newPlainText("Copied Text", gsfid);
                clipboard.setPrimaryClip(clip);
                Snackbar.make(v, "Text copied to Clipboard", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public static String getGSFID(Context context) {
        try {
            Cursor query = context.getContentResolver().query(sUri, null, null, new String[] { "android_id" }, null);
            if (query == null) {
                return "Not found";
            }
            if (!query.moveToFirst() || query.getColumnCount() < 2) {
                query.close();
                return "Not found";
            }
            final String toHexString = Long.toHexString(Long.parseLong(query.getString(1)));
            query.close();
            return toHexString.toUpperCase().trim();
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
}
