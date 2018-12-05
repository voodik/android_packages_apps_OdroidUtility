package com.hardkernel.voodik.odroidutility;


import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class UpdaterFragment extends Fragment {

    private static String mzipFile;
    private static String mmdsumFile;
    private static ObjectAnimator anim1;
    private static LinearLayout LLops;
    private static Context context = null;

    private static String mUnzipLocation = Environment.getExternalStorageDirectory() + "/";

    public UpdaterFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static UpdaterFragment newInstance(ArrayList<Uri> FileUris) {
        UpdaterFragment frag = new UpdaterFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("url", FileUris);
        frag.setArguments(args);
        return frag;
    }


    private static CircleProgressDialog dgextract;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_updater, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        boolean mhaszipfile = false;
        boolean mhassumfile = false;
        final ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        final ImageView iconPaste = (ImageView) view.findViewById(R.id.iconPaste);
        final Button btn = view.findViewById(R.id.button);
        final EditText edurl = view.findViewById(R.id.editText_url);
        final TextView changelog = view.findViewById(R.id.TVchangelog);
        final CheckBox cBoxFormatUdata = view.findViewById(R.id.cBoxFormatUdata);
        final CheckBox cBoxFormatFat = view.findViewById(R.id.cBoxFormatFat);
        final CheckBox cBoxFormatEnv = view.findViewById(R.id.cBoxFormatEnv);
        final CheckBox cBoxUpUboot = view.findViewById(R.id.cBoxUpUboot);
        ArrayList<Uri> FileUris = getArguments().getParcelableArrayList("url");
        LLops = view.findViewById(R.id.options);
        boolean haserror = false;
        mzipFile = null;
        mmdsumFile = null;
        edurl.setFocusable(false);
        cBoxUpUboot.setChecked(true);
        cBoxUpUboot.setEnabled(false);

        anim1 = ObjectAnimator.ofInt(LLops, "backgroundColor", Color.WHITE, Color.RED,
                Color.WHITE);
        anim1.setDuration(1500);
        anim1.setEvaluator(new ArgbEvaluator());
        anim1.setRepeatMode(ValueAnimator.REVERSE);
        anim1.setRepeatCount(ValueAnimator.INFINITE);
//tmp
        anim1.start();
        ((MainActivity)context).animatefab();

        if (FileUris.isEmpty()){
            Log.v("MNG Utility", "isEmpty ");
            mzipFile = Constants.UPZIPFILE;
            mmdsumFile = Constants.UPSUMFILE;
        } else if(FileUris.size() == 2) {
            for (int i = 0; i < FileUris.size(); i++) {
                Uri fileuri = FileUris.get(i);
                if (fileuri.toString().startsWith("content://")) {

                    Log.v("MNG Utility", "texturis " + getfilenamefromuri(fileuri));
                    if (getfilenamefromuri(fileuri).equals("update.zip.md5sum")) {
                        mmdsumFile = fileuri.toString();
                        mhassumfile = true;
                    } else if (getfilenamefromuri(fileuri).equals("update.zip")) {
                        mzipFile = fileuri.toString();
                        mhaszipfile = true;
                    }
                }
            }
            haserror = !(mhaszipfile && mhassumfile);
        } else {
            haserror = true;
        }


        if(haserror) {
            String title = "Error";
            String message = "Please select proper update.zip and update.zip.md5sum files";

            new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("OK", null)
                    .show();
            mzipFile = Constants.UPZIPFILE;
            mmdsumFile = Constants.UPSUMFILE;
        } else if (mhaszipfile && mhassumfile) {
            String title = "Internal storage update";
            String message = "Are you sure you want to start update process?";

            AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    unzip();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mzipFile = Constants.UPZIPFILE;
                    mmdsumFile = Constants.UPSUMFILE;
                }
            });
            builder.show();

        }

        cBoxFormatUdata.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                stopanim();
                if (isChecked) {
                    cBoxFormatUdata.setTextColor(Color.parseColor("#FF5252"));
                } else {
                    cBoxFormatUdata.setTextColor(Color.parseColor("#212121"));
                }
            }
        });
        cBoxFormatFat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                stopanim();
            }
        });
        cBoxFormatEnv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                stopanim();
            }
        });
        cBoxUpUboot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                stopanim();
            }
        });



//        edurl.setText(mzipFile);
        changelog.setMovementMethod(new ScrollingMovementMethod());
        btn.setEnabled(true);

        iconPaste.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String pasteData = "";

                // If it does contain data, decide if you can handle the data.
                if (!(clipboard.hasPrimaryClip())) {

                } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))) {

                    // since the clipboard has data but it is not plain text

                } else {

                    //since the clipboard contains plain text.
                    ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

                    // Gets the clipboard as text.
                    pasteData = item.getText().toString();
                    edurl.setText(pasteData);
                }
            }
            });



        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                StatFs stat = new StatFs(Environment.getExternalStorageDirectory().toString());
                long sdAvailSize = stat.getFreeBytes();
                Log.v("MNG Utility", "getExternalStorageDirectory " + Environment.getExternalStorageDirectory().toString());
                Log.v("MNG Utility", "getFreeBytes " + Long.toString(sdAvailSize));
                unzip();
            }
        });

        RotateAnimation anim = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(700);

        // Start animating the image
        final ImageView iconUpdate = (ImageView) view.findViewById(R.id.iconUpdate);
        iconUpdate.startAnimation(anim);
        // Later.. stop the animation
        iconUpdate.setAnimation(null);


    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private static void unzip() {
//        String zipFile = Environment.getExternalStorageDirectory() + "/update.zip"; //your zip file location
 // unzip location
/*        dgextract = CircleProgressDialog.newInstance("Please Wait... Extracting zip file... ");
        dgextract.setCancelable(false);
        dgextract.show(getFragmentManager(), "extractdialog");
        new UnZipTask().execute(mzipFile, mUnzipLocation);*/
        Log.v("MNG Utility", "mzipFile " + mzipFile);
        Log.v("MNG Utility", "mmdsumFile " + mmdsumFile);

        new MyUnzip(context, Uri.parse(mzipFile), Uri.parse(mmdsumFile), mUnzipLocation) {

            @Override
            protected void onPostExecute( Long result ) {

                super.onPostExecute( result );
                // Do something with result here
                anim1.start();
                ((MainActivity)context).animatefab();
            }
        }.execute();

    }

    String getfilenamefromuri(Uri uri){
        Cursor returnCursor =
                getActivity().getContentResolver().query(uri, null, null, null, null);

        returnCursor.moveToFirst();
        return  returnCursor.getString(returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
    }

    static void stopanim(){
        if (anim1.isStarted()){
            anim1.cancel();
            LLops.setBackgroundColor(Color.TRANSPARENT);
        }
    }

}
