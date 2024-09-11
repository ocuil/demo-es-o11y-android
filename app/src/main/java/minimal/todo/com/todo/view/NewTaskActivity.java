package minimal.todo.com.todo.view;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;

import androidx.core.content.ContextCompat;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.Tracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import minimal.todo.com.todo.R;
import minimal.todo.com.todo.database.DatabaseHelper;
import minimal.todo.com.todo.database.model.Note;

/**
 * Created by Michal Štrba on 01/08/18.
 */

public class NewTaskActivity extends Activity implements View.OnClickListener {
    private List<Note> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView noNotesView;
    private DatabaseHelper db;
    private   Button AddButton;
    private  EditText inputNote;
    private  EditText inputNote2;
    private Calendar myCalendar;
    private int mYear, mMonth, mDay;
    private RelativeLayout rDate;
    private ImageView  iDate;
    private AdView mAdView;
    private Tracker mTracker;

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
          SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
          final  boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8337023763045237/5455518533");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });


        if(useDarkTheme) {
            setTheme(R.style.ColorWhite);
        }
        setContentView(R.layout.activityse);
        if(useDarkTheme) {
            setTheme(R.style.ColorWhite);
            RelativeLayout l = findViewById(R.id.main);
            l.setBackgroundColor(getResources().getColor(R.color.darkbackground));
            TextView textView = findViewById(R.id.textView4);
            textView.setTextColor(Color.WHITE);
            ImageView imageButton2 = findViewById(R.id.imageView9);
            imageButton2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_white));
            ImageView imageView2 = findViewById(R.id.IconDate);
            imageView2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.calendars_dark));
            ImageView imageView3 = findViewById(R.id.imageView);
            imageView3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.details_dark));
            TextView textView4 = findViewById(R.id.DateInIcon);
            textView4.setTextColor(Color.WHITE);
        }

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-8337023763045237~5314632945");
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-8337023763045237/2614734487");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
         final EditText edit = (EditText) findViewById(R.id.EditTextNewTask);
        edit.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyboardShown(edit.getRootView())) {
                    Log.d("keyboard", "keyboard UP");


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            mAdView.setVisibility(View.GONE);
                            // Actions to do after 10 seconds
                        }
                    }, 100);

                } else {
                    Log.d("keyboard", "keyboard Down");


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            mAdView.setVisibility(View.VISIBLE);
                            // Actions to do after 10 seconds
                        }
                    }, 500);

                }




            }
        });
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
            }
        });


        ImageView imageView = findViewById(R.id.imageView9);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        final Intent intent = this.getIntent();
        RelativeLayout rootLayout =  findViewById(R.id.MainLayout);
        recyclerView = findViewById(R.id.RecyclerView);
        noNotesView = findViewById(R.id.empty_notes_view);
        db = new DatabaseHelper(this);
        notesList.addAll(db.getAllData());
        TextView textView1 = findViewById(R.id.DateInIcon);
        Calendar cal1 = Calendar.getInstance();
        DateFormat df1 = new SimpleDateFormat("dd");
        String date_str1 = df1.format(cal1.getTime());
        textView1.setText(date_str1);
        myCalendar = Calendar.getInstance();
        final   TextView textView = findViewById(R.id.TextDate);
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String date_str = df.format(cal.getTime());
        textView.setText(date_str);
        inputNote = findViewById(R.id.EditTextNewTask);
        inputNote.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        inputNote.setRawInputType(InputType.TYPE_CLASS_TEXT);
        inputNote2 = findViewById(R.id.editTextNewTask2);
        inputNote2.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputNote2.setRawInputType(InputType.TYPE_CLASS_TEXT);
        AddButton =  findViewById(R.id.AddButton);
        AddButton.setOnClickListener(this);
        rDate = findViewById(R.id.DateLayout);
        rDate.setOnClickListener(this);
        iDate = findViewById(R.id.IconDate);
        iDate.setOnClickListener(this);
        iDate.setOnClickListener(this);
    }




    private boolean keyboardShown(View rootView) {

        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    public void onClick(View v) {
        if (v == iDate){
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.TimePickerDialogTheme,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateLabel();
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
            final   TextView  textDate2  =  findViewById(R.id.TextDate);
            textDate2.setTextColor(Color.parseColor("#218be7"));
        }
        if (v == rDate){
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.TimePickerDialogTheme,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateLabel();
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
            final   TextView  textDate2  =  findViewById(R.id.TextDate);
            textDate2.setTextColor(Color.parseColor("#218be7"));
        }
        if (v == AddButton) {


            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {





                        if (TextUtils.isEmpty(inputNote.getText().toString())) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Refreshing...", Toast.LENGTH_SHORT);
                              SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                              final  boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
                            View view = toast.getView();
                            if (useDarkTheme){
                                view.setBackgroundResource(R.drawable.bgtoast);
                            }else {
                            view.setBackgroundResource(android.R.drawable.toast_frame);}
                            view.setMinimumWidth(500);
                            TextView toastMessage = (TextView) view.findViewById(android.R.id.message);
                            toast.show();
                            return;
                        } else {}
                        boolean shouldUpdate = true;
                        Note note = null;
                        int position = -1;
                        if (shouldUpdate) {
                            String myFormat = "dd MMM yyyy";
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                            createNote(inputNote.getText().toString(), inputNote2.getText().toString(), sdf.format(myCalendar.getTime()));
                            Intent intent = new Intent(NewTaskActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            String myFormat = "dd MMM yyyy";
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                            createNote(inputNote.getText().toString(), inputNote2.getText().toString(), sdf.format(myCalendar.getTime()));
                        }} }else {}}


    private void updateLabel() {
        String myFormat = "dd MMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        TextView textView = findViewById(R.id.TextDate);
        textView.setText(sdf.format(myCalendar.getTime()));
        TextView textView1 = findViewById(R.id.DateInIcon);
        DateFormat df1 = new SimpleDateFormat("dd");
        String date_str1 = df1.format(myCalendar.getTime());
        textView1.setText(date_str1);
    }

    private void createNote(String note ,String time,String timestamp ) {
        long id = db.insertNote(note,time,timestamp);
        Note n = db.getNote(id);
        if (n != null) {
            notesList.add(0, n);
        }}
    private void updateNote(String note,String time , String timestamp, int position){
        Note n = notesList.get(position);
        n.setNote(note);
        n.setTime(time);
        n.setTimestamp(timestamp);
        db.updateNote(n);
        notesList.set(position, n);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i);
        startActivity(i); }}
