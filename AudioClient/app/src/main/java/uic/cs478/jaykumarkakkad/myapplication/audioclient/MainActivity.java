package uic.cs478.jaykumarkakkad.myapplication.audioclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import uic.cs478.jaykumarkakkad.myapplication.commonInterface.MusicPlayerInterface;

public class MainActivity extends AppCompatActivity {

    protected static final String TAG = "AudioClient";
    protected static final int PERMISSION_REQUEST = 0;
    private MusicPlayerInterface mMusicPlayerInterface;
    private boolean mIsBound = false;

    Context c = null ;

    int MusicCode = 0;

    private RadioGroup radioGroup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ------------- START SERVICE --------------------------
        Button startServicebutton = findViewById(R.id.start_service);

        //---------- Exp ------

        final Intent musicServiceIntent;
        boolean b ;
        musicServiceIntent = new Intent(MusicPlayerInterface.class.getName());
        Log.e("Key client", musicServiceIntent.toString()) ;


        ResolveInfo info = getPackageManager().resolveService(musicServiceIntent, PackageManager.MATCH_ALL);
        musicServiceIntent.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

//-----------------------------------------

//        // get context
//
//        try {
//            c = createPackageContext("uic.cs478.jaykumarkakkad.myapplication.clipserver", 0) ;
//            Log.e("Context", "Context says: " + c.toString()) ;
//        } catch (PackageManager.NameNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        String serviceClassName = "uic.cs478.jaykumarkakkad.myapplication.clipserver.MyService";
//
//
//        // Explicit intent used for starting the MusicService
//        final Intent musicServiceIntent = new Intent();
//        musicServiceIntent.setClassName(c ,serviceClassName);
//
        //----------------------------

        startServicebutton.setOnClickListener(
                (view) -> {
                    startForegroundService(musicServiceIntent);
                    Button b1 = findViewById(R.id.end_service);
                    b1.setEnabled(true);
                    b1.setBackgroundColor(getResources().getColor(R.color.teal_200));

                    b1 = findViewById(R.id.listView_button);
                    b1.setEnabled(true);
                    b1.setBackgroundColor(getResources().getColor(R.color.teal_200));
                }

        ) ;

        // ============== END SERVICE IMPLEMENT --------------------------------------

        Button endService_button = findViewById(R.id.end_service);

        endService_button.setOnClickListener(
                (view) ->
                {  check_and_unbind();
                    stopService(musicServiceIntent);
                    Button listviewbutton = findViewById(R.id.listView_button);
                    ConstraintLayout cl = findViewById(R.id.optionsLayout);
                    cl.setVisibility(View.INVISIBLE);
                    listviewbutton.setEnabled(false);
                    listviewbutton.setBackgroundColor(getResources().getColor(R.color.gray));

                }
        );


        // ----- Set the List view button
        Button listviewbutton = findViewById(R.id.listView_button);
        ConstraintLayout cl = findViewById(R.id.optionsLayout);
        cl.setVisibility(View.INVISIBLE);

        listviewbutton.setOnClickListener(
                (view)-> {

                    cl.setVisibility(View.VISIBLE);
                }

                );

        radioGroup = findViewById(R.id.radiobuttons);

        // Set listener for radio button changes
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                int selectedIndex = radioGroup.indexOfChild(selectedRadioButton);
                MusicCode = selectedIndex;
                Toast.makeText(MainActivity.this, "Selected index: " + selectedIndex, Toast.LENGTH_SHORT).show();

                reset_all_buttons();

                check_and_unbind();
            }
            });



        // ============= Music playing Buttons =====================================

        Button play_button_1 = findViewById(R.id.playmusic_1);

        play_button_1.setOnClickListener(
                (view)-> {

                    Button b1 = findViewById(R.id.pause_music_1);
                    b1.setEnabled(true);
                    b1.setBackgroundColor(getResources().getColor(R.color.teal_200));
                    Button b2 = findViewById(R.id.stop_music_1);
                    b2.setEnabled(true);
                    b2.setBackgroundColor(getResources().getColor(R.color.teal_200));
                    Button b3 = findViewById(R.id.playmusic_1);
                    b3.setEnabled(false);
                    b3.setBackgroundColor(getResources().getColor(R.color.gray));

                    checkBindingAndBind();

                }
        );

        // ======== MUSIC stopping Buttons ===========================

        Button stop_button_1 = findViewById(R.id.stop_music_1);

        stop_button_1.setOnClickListener(
                (view) -> {
                    Log.i(TAG, " AT STOP, is Service bound: " + mIsBound);
                    try {

                        // Call KeyGenerator and get a new ID
                        if (mIsBound) {
                            // Calling the service through proxy
                            mMusicPlayerInterface.stopMusic();
                            check_and_unbind();
                            reset_all_buttons();
                        } else {
                            Log.i(TAG, "Ugo says that the service was not bound!");
                        }

                    } catch (RemoteException e) {

                        Log.e(TAG, e.toString());

                    };
                   }
        );


        /// ================  Pause button ===============
        Button pause_button = findViewById(R.id.pause_music_1);
        pause_button.setOnClickListener((view)-> {
                    try {

                        // Call KeyGenerator and get a new ID
                        if (mIsBound) {
                            // Calling the service through proxy
                            mMusicPlayerInterface.pause();
                            reset_all_buttons();
                           Button b1 = findViewById(R.id.resume_music_1);
                            b1.setEnabled(true);
                            b1.setBackgroundColor(getResources().getColor(R.color.teal_200));

                        } else {
                            Log.i(TAG, "Ugo says that the service was not bound!");
                        }

                    } catch (RemoteException e) {

                        Log.e(TAG, e.toString());

                    };

                }

                );

        // ======================== Resume Button =================================================
        Button resume_button = findViewById(R.id.resume_music_1);
        resume_button.setOnClickListener((view)-> {
            try {

                // Call KeyGenerator and get a new ID
                if (mIsBound) {
                    // Calling the service through proxy
                    mMusicPlayerInterface.resume();
                    Button b1 = findViewById(R.id.resume_music_1);
                    b1.setEnabled(false);
                    b1.setBackgroundColor(getResources().getColor(R.color.gray));
                    b1 = findViewById(R.id.pause_music_1);
                    b1.setEnabled(true);
                    b1.setBackgroundColor(getResources().getColor(R.color.teal_200));
                    b1 = findViewById(R.id.stop_music_1);
                    b1.setEnabled(true);
                    b1.setBackgroundColor(getResources().getColor(R.color.teal_200));


                } else {
                    Log.i(TAG, "Ugo says that the service was not bound!");
                }

            } catch (RemoteException e) {

                Log.e(TAG, e.toString());

            };


        });


    }

    protected void check_and_unbind(){
        if (mIsBound) {
            unbindService(this.mConnection);
        }
        mIsBound = false;

    }

    protected void reset_all_buttons(){

        Button b1 = findViewById(R.id.playmusic_1);
        b1.setEnabled(true);
        b1.setBackgroundColor(getResources().getColor(R.color.teal_200));

        b1 = findViewById(R.id.resume_music_1);
        b1.setEnabled(false);
        b1.setBackgroundColor(getResources().getColor(R.color.gray));

        b1 = findViewById(R.id.stop_music_1);
        b1.setEnabled(false);
        b1.setBackgroundColor(getResources().getColor(R.color.gray));
        b1 = findViewById(R.id.pause_music_1);
        b1.setEnabled(false);
        b1.setBackgroundColor(getResources().getColor(R.color.gray));
    }

    protected void checkBindingAndBind() {
        if (!mIsBound) {
            Intent bind_intent;
            boolean b ;
            bind_intent = new Intent(MusicPlayerInterface.class.getName());
            Log.e("audioclient", bind_intent.toString()) ;


            ResolveInfo info = getPackageManager().resolveService(bind_intent, PackageManager.MATCH_ALL);
            bind_intent.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

            b = bindService(bind_intent, this.mConnection, Context.BIND_AUTO_CREATE);
            if (b) {
                Log.i(TAG, "Ugo says bindService() succeeded!");
            } else {
                Log.i(TAG, "Ugo says bindService() failed!");
            }
        }
    }


    private final ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder iservice) {

            mMusicPlayerInterface = MusicPlayerInterface.Stub.asInterface(iservice);
            mIsBound = true;

            playMyMusic();



        }

        public void onServiceDisconnected(ComponentName className) {

            mMusicPlayerInterface = null;
            mIsBound = false;

        }
    };

    protected void playMyMusic(){
        try {

            // Calling the service through proxy
            mMusicPlayerInterface.playMusic(MusicCode);
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (mIsBound) {
            unbindService(this.mConnection);
        }
    }



}