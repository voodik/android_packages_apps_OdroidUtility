package com.hardkernel.voodik.odroidutility;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

//import com.google.firebase.analytics.FirebaseAnalytics;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    public BootIniHelper mBh;
//    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "mainacct");
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text");
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBh = new BootIniHelper(this,Constants.PATH_BOOTINI);
        mBh.syncprefs();

        fab = findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#bcbcbc")));
        fab.setEnabled(false);

        String title = "Internal storage update";
        String message = "Are you sure you want to start update process?";

        final AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                builder.show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };


        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        String  action = intent.getAction();
        String type = intent.getType();
        ArrayList<String> texturis = new ArrayList<>();

        if (savedInstanceState == null) {
            Fragment fragment = null;

            if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null){
                Log.v("MNG Utility", "type " + type);
                ArrayList<Uri> FileUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);

                try {
                    fragment = UpdaterFragment.newInstance(FileUris);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    fragment = DisplayFragment.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            // Вставляем фрагмент, заменяя текущий фрагмент
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Создадим новый фрагмент
        Fragment fragment = null;

        fab.setEnabled(true);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0266")));

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_display) {
            fragment = DisplayFragment.newInstance();
        } else if (id == R.id.nav_cpu) {
            fragment = CpuFragment.newInstance();
        } else if (id == R.id.nav_ethernet) {
            fragment = EthFragment.newInstance();
        } else if (id == R.id.nav_fan) {

        } else if (id == R.id.nav_shortcuts) {
            fragment = ShortcutsFragment.newInstance();
        } else if (id == R.id.nav_misc) {

        } else if (id == R.id.nav_update) {
            fragment = UpdaterFragment.newInstance(new ArrayList<Uri>());
        } else if (id == R.id.nav_bootini) {
            fragment = BootIniFragment.newInstance();
        } else if (id == R.id.nav_about) {
            fragment = AboutFragment.newInstance();
        }


        // Вставляем фрагмент, заменяя текущий фрагмент
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        // Выделяем выбранный пункт меню в шторке
        item.setChecked(true);
        // Выводим выбранный пункт в заголовке
        setTitle(item.getTitle());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void animatefab() {
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.vibrate);

        myAnim.setAnimationListener(new Animation.AnimationListener() {
            int count = 0;

            @Override
            public void onAnimationEnd(Animation arg0) {
                if (count < 2) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fab.startAnimation(myAnim);
                        }
                    }, 1000);// de
                    count++;
                }
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

        });
        fab.startAnimation(myAnim);


    }
}
