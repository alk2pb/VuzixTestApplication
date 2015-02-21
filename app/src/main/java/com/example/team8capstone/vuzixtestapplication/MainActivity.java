package com.example.team8capstone.vuzixtestapplication;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team8capstone.vuzixtestapplication.video.VideoActivity;


public class MainActivity extends Activity implements RecognitionListener {
    // Array of Card Infos
    private static ArrayList<CardInfo> cardInfos = new ArrayList<CardInfo>();

    SpeechRecognizer mSpeechRecognizer;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private Intent video;

    private boolean playing = false;

    AlertDialog.Builder builder1;

    AlertDialog alert11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(AUDIO_SERVICE,"onCreate");

        setCardInfo();

        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

//        mSpeechRecognizer = getSpeechRecognizer();

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                getPackageName());

        getSpeechRecognizer().startListening(intent);

        // Instantiates a new intent for the VideoActivity that will be activated when
        // the user wishes to view a video
        video = new Intent(this, VideoActivity.class);

        setPageSelectedListener();
        setMediaResources(0);

        builder1 = new AlertDialog.Builder(MainActivity.this);

        builder1.setMessage("Repeat Shut Down to Exit");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Shut Down",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        builder1.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        alert11 = builder1.create();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.v(AUDIO_SERVICE,"onResume");
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//
//        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
//                getPackageName());
//
//        getSpeechRecognizer().startListening(intent);
//
//    }

//    @Override
//    protected void onPause() {
//        Log.v(AUDIO_SERVICE,"onPause");
//        if(getSpeechRecognizer()!=null){
//            getSpeechRecognizer().stopListening();
//            getSpeechRecognizer().cancel();
////            getSpeechRecognizer().destroy();
//        }
////        mSpeechRecognizer = null;
//
//        super.onPause();
//    }

    @Override
    public void onDestroy(){
        Log.v(AUDIO_SERVICE,"onDestroy");
        if(mSpeechRecognizer!=null){
            mSpeechRecognizer.stopListening();
            mSpeechRecognizer.cancel();
//            mSpeechRecognizer.destroy();
        }
//        mSpeechRecognizer = null;

        super.onDestroy();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Toast.makeText(this, "Selected.", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onReadyForSpeech(Bundle params) {
//        Toast.makeText(this, "Voice Recognition Ready.", Toast.LENGTH_SHORT).show();
        //iv.setImageResource(R.drawable.ready);
    }

    @Override
    public void onBeginningOfSpeech() {
//        Toast.makeText(this, "Please speak command.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.v(AUDIO_SERVICE,"recieve : " + rmsdB + "dB");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.v(AUDIO_SERVICE,"recieved");
    }

    @Override
    public void onEndOfSpeech() {
        Log.v(AUDIO_SERVICE,"finished.");
    }

    @Override
    public void onError(int error) {
        Log.v(AUDIO_SERVICE,"Error: " + error);
        finish();
    }

    @Override
    public void onResults(Bundle results) {
        // Vuzix M100 v1.0.8 only can support 'onPartialResuts'
        Log.v(AUDIO_SERVICE,"onResults called");

    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        // Vuzix M100 v1.0.8 only can support 'onPartialResuts'
        // Supported keywords:
        //   move left/right/up/down
        //   go left/right/up/down
        //   left/right/up/down
        //   next, previous, forward, back
        //   select, cancel
        //   complete, stop, exit, go home
        //   menu, volume up/down
        //   mute, call, dial, hang up, answer
        //   ignore, end, redial, call back
        //   contacts, favorites, pair, unpair
        //   sleep, shut down
        //   set clock/time
        //   cut, copy, paste, delete
        //   0, 1, 2, 3, 4, 5, 6, 7, 8, 9
        String[] keywords = new String[]{"next", "previous", "select", "stop", "cancel", "exit", "shut down"};
        ArrayList<String> recData = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String getData = new String();



        for (String s : recData) {
            getData += s + ",";
        }

        // Show filtered keyword
        String result = "";
        for (String s: recData){
            for (String t: keywords){
                if(s.equals(t)){
                    result = t;
                    break;
                }
            }
            if(!result.isEmpty()){
//                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                switch (result) {
                    case "next":
                        if (mViewPager.getCurrentItem() < mViewPager.getChildCount()) {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                        }
                        break;
                    case "previous":
                        if (mViewPager.getCurrentItem() > 0) {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
                        }
                        break;
                    case "select":
                        if (!playing) {
                            startMedia(mViewPager.getCurrentItem());
                            playing = true;
                        }
                        break;
                    case "stop":
                        if (playing) {
                            Intent i = new Intent(MainActivity.this, VideoActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            i.putExtra("finish",true);
                            MainActivity.this.startActivity(i);
//                            playing = false;
                        }
                        break;
                    case "cancel":
                        if (alert11.isShowing()) {
                            alert11.getButton(AlertDialog.BUTTON_NEGATIVE).performClick();
                        }
                        break;
                    case "shut down":
                        if (!playing) {
                            if (!alert11.isShowing()) {
                                alert11.show();
                            }
                            else {
                                alert11.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
                            }
                        }
                        break;
                    default:
                        break;
                }
                result = "";
            }
        }
        // Raw Result
        Log.v(AUDIO_SERVICE, "Result: " + getData);
    }


    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.v(AUDIO_SERVICE,"onEvent called");
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);


        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int position = (int) getArguments().get(ARG_SECTION_NUMBER);

            View rootView = inflater.inflate(cardInfos.get(position).xmlLayout, container, false);

            ViewGroup rightColumn = (ViewGroup) rootView.findViewById(R.id.right_column);

            if (cardInfos.get(position).hasImage){
                ImageView imageView = (ImageView) rightColumn.findViewById(R.id.image);
                imageView.setImageResource(cardInfos.get(position).imageResource);
            }

            ViewGroup leftColumn = (ViewGroup) rootView.findViewById(R.id.left_column);

            if (cardInfos.get(position).hasHeader) {
                TextView textViewHeader = (TextView) leftColumn.findViewById(R.id.header);
                textViewHeader.setText(cardInfos.get(position).header);
                textViewHeader.setTextSize(cardInfos.get(position).headerTextSize);
            }

            if (cardInfos.get(position).hasText) {
                TextView textViewContent = (TextView) leftColumn.findViewById(R.id.content);
                textViewContent.setText(cardInfos.get(position).text);
                textViewContent.setTextSize(cardInfos.get(position).textSize);
            }
            return rootView;
        }
    }

    private SpeechRecognizer getSpeechRecognizer() {
        if(mSpeechRecognizer == null){
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
            mSpeechRecognizer.setRecognitionListener(MainActivity.this);
        }
        return mSpeechRecognizer;
    }

    // Set Card Info
    private void setCardInfo() {
        cardInfos.add(new CardInfo(cardInfos.size(), R.layout.fragment_main)
                .setHeader("Card Header 1")
                .setText("• Card Content\n" +
                        "• Card Content\n" +
                        "• Card Content\n" +
                        "• Card Content")
                .setVideoResource(R.raw.video_sample)
                .setImageResource(R.drawable.beach));
        cardInfos.add(new CardInfo(cardInfos.size(), R.layout.fragment_main)
                .setHeader("Card Header 2")
                .setText("• Card Content\n" +
                        "• Card Content\n" +
                        "• Card Content\n" +
                        "• Card Content")
                .setVideoResource(R.raw.video_sample)
                .setImageResource(R.drawable.beach));
        cardInfos.add(new CardInfo(cardInfos.size(), R.layout.fragment_main)
                .setHeader("Card Header 3")
                .setText("• Card Content\n" +
                        "• Card Content\n" +
                        "• Card Content\n" +
                        "• Card Content")
                .setVideoResource(R.raw.video_sample)
                .setImageResource(R.drawable.beach));
    }

    // Set media resources based on slide position
    private void setMediaResources(int position){
        if (cardInfos.get(position).hasVideo){
            video.removeExtra("videoResource");
            video.putExtra("videoResource", cardInfos.get(position).videoResource);
        }
    }

    // Starts audio and video when card is selected
    private void startMedia(int position) {
        if (cardInfos.get(position).hasVideo) {
            startActivityForResult(video,1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                playing = false;
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    private void setPageSelectedListener() {
        mViewPager.setOnPageChangeListener( new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setMediaResources(position);
            }
        });

        mViewPager.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!playing){
                    startMedia(mViewPager.getCurrentItem());
                    playing = true;
                }
            }
        });
    }





}
