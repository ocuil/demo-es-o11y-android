package minimal.todo.com.todo.view;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import minimal.todo.com.todo.R;
import minimal.todo.com.todo.database.DatabaseHelper;
import minimal.todo.com.todo.database.DatabaseHelperb;
import minimal.todo.com.todo.database.model.Note;
import minimal.todo.com.todo.database.model.Noted;

/**
 * Created by Michal Štrba on 01/08/18.
 */

public class MainActivity extends Activity  {
    private List<Noted> notesLists = new ArrayList<>();
    private List<Note> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView noNotesView, noNotesView2;
    private DatabaseHelper db;
    private   boolean useDarkTheme ;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";
    private static final String PREFS_NAME2 = "prefs";
    private static final String PREF_DARK_THEME2 = "theme";
    private boolean home = false;
    private CompletedActivity.NotesAdaptered mAdapter;
    private boolean isChecked;
    private ImageButton imageButton;
    private DatabaseHelperb dbs;
    private View views;
    private View viewss;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
        SharedPreferences p1 = getSharedPreferences(PREFS_NAME2,MODE_PRIVATE);
        isChecked = p1.getBoolean(PREF_DARK_THEME2,false);
        if (useDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        }        setContentView(R.layout.activity_new_task);
        db = new DatabaseHelper(getApplication());
        notesList.addAll(db.getAllData());
        if (useDarkTheme) {
            imageButton = findViewById(R.id.imageButton);
            imageButton.setBackground(getResources().getDrawable(R.drawable.sun_dark)); }
        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         views = layoutInflater.inflate(R.layout.activity_completed, null);
        LayoutInflater layoutInflater1 = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewss = layoutInflater1.inflate(R.layout.content_main, null);
        noNotesView = viewss.findViewById(R.id.empty_notes_view2);
        noNotesView2 = viewss.findViewById(R.id.empty_notes_view);
        if (useDarkTheme){
            noNotesView.setTextColor(Color.WHITE);
            noNotesView2.setTextColor(Color.WHITE);
        }
        toggleEmptyNotes();
        dbs = new DatabaseHelperb(getApplicationContext());
        notesLists.addAll(dbs.getAllDakar());
        someMethod();
        if (useDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
            Toolbar toolbar = findViewById(R.id.toolbar3);
            toolbar.setBackgroundColor(Color.parseColor("#15181e"));
            ImageButton imageButton = findViewById(R.id.imageView2);
            imageButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.home_click_white));
            ImageButton imageButton2 = findViewById(R.id.imageView3);
            imageButton2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dashboar_white));
        }



        AnalyticsApplication application = (AnalyticsApplication) getApplication();
         mTracker = application.getDefaultTracker();
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share")
                .build());

        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View button) {
                if (isChecked) {
                    isChecked = false;
                    toggleTheme(isChecked);
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME2, MODE_PRIVATE).edit();
                    editor.putBoolean(PREF_DARK_THEME2, isChecked);
                    editor.apply();
                } else {
                    isChecked = true;
                    toggleTheme(isChecked);
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME2, MODE_PRIVATE).edit();
                    editor.putBoolean(PREF_DARK_THEME2, isChecked);
                    editor.apply();
                }
            }

        });
        FloatingActionButton fab = findViewById(R.id.NewTaskFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (home){
                    recyclerView = views.findViewById(R.id.lil);
                    mAdapter = new CompletedActivity.NotesAdaptered(getApplication(), notesLists);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplication());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(mAdapter);
                    dbs = new DatabaseHelperb(getApplication());
                    notesLists.addAll(dbs.getAllDakar());
                    dbs.clearDatabase();
                    notesLists.clear();
                    someMethod();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_place,new CompletedActivity()).addToBackStack(null).commit();
                            toggleEmptyNotes();
                        }
                    }, 200);
            } else {
                    Intent i = new Intent(getBaseContext(), NewTaskActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);
                    startActivity(i);
                }}});
        toggleEmptyNotes();  }

    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
    void someMethod() {
        Runnable task = new Runnable() {
            public void run() {
                int x = mAdapter.getItemCount();
                final   TextView textView = views.findViewById(R.id.TaskView);
                textView.setText(String.valueOf(x));
            }};
        worker.schedule(task, 1, TimeUnit.MILLISECONDS);
    }
    public void stackFlag(View view) {
       Fragment fr = null;
        ImageButton imageButton = findViewById(R.id.imageView2);
        ImageButton imageButton2 = findViewById(R.id.imageView3);
        if (view == findViewById(R.id.imageView2)) {
            home = false;
            if (useDarkTheme) {
                fr = new TaskActivity();
                imageButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.home_click_white));
                imageButton2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dashboar_white));
                FloatingActionButton fab = findViewById(R.id.NewTaskFab);
                fab.setImageResource(R.drawable.plus);
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(33, 137, 231)));
                toggleEmptyNotes();
            } else {
                imageButton.setBackgroundResource(R.drawable.home_click);
                imageButton2.setBackgroundResource(R.drawable.dashboar);
                FloatingActionButton fab = findViewById(R.id.NewTaskFab);
                fab.setImageResource(R.drawable.plus);
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(33, 137, 231)));
                fr = new TaskActivity();
            }} else {
            home = true;
            if (view == findViewById(R.id.imageView3)){
                if (useDarkTheme) {
                    imageButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.home_dark));
                    imageButton2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.dashboarh_white));
                    FloatingActionButton fab = findViewById(R.id.NewTaskFab);
                    fab.setImageResource(R.drawable.deleted_dark);
                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(224, 83, 86)));
                    fr = new CompletedActivity();
                } else{
                    fr = new CompletedActivity();
                imageButton.setBackgroundResource(R.drawable.home);
                imageButton2.setBackgroundResource(R.drawable.dashboarh);
                FloatingActionButton fab = findViewById(R.id.NewTaskFab);
                fab.setImageResource(R.drawable.deleted_dark);
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(224, 83, 86)));
            }}}
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void toggleEmptyNotes() {
        if (db.getNotesCount() > 0) {
            noNotesView.setVisibility(View.GONE);
            noNotesView2.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
            noNotesView2.setVisibility(View.VISIBLE);
        }}
    private void toggleTheme(boolean darkTheme) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_DARK_THEME, darkTheme);
        editor.apply();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        finish();

        startActivity(intent);
    }


    @Override
protected void onResume() {
        super.onResume();
        mTracker.setScreenName("MainActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onBackPressed(){

        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .icon(getResources().getDrawable(R.drawable.logo_l))
                .session(2)
                .title("How was your experience with us?")
                .titleTextColor(R.color.black)
                .positiveButtonText("Not Now")
                .negativeButtonText("Never")
                .positiveButtonTextColor(R.color.colorAccent)
                .negativeButtonTextColor(R.color.colorAccent)
                .formTitle("Submit Feedback")
                .formHint("Tell us where we can improve")
                .formSubmitText("Submit")
                .formCancelText("Cancel")
                .ratingBarColor(R.color.colorAccent)
                .playstoreUrl("https://play.google.com/store/apps/details?id=minimal.todo.com.todo")
                .onThresholdCleared(new RatingDialog.Builder.RatingThresholdClearedListener() {
                    @Override
                    public void onThresholdCleared(RatingDialog ratingDialog, float rating, boolean thresholdCleared) {
                        //do something
                        ratingDialog.dismiss();



                    }
                })
                .onThresholdFailed(new RatingDialog.Builder.RatingThresholdFailedListener() {
                    @Override
                    public void onThresholdFailed(RatingDialog ratingDialog, float rating, boolean thresholdCleared) {
                        //do something
                        ratingDialog.dismiss();

                    }
                })
                .onRatingChanged(new RatingDialog.Builder.RatingDialogListener() {
                    @Override
                    public void onRatingSelected(float rating, boolean thresholdCleared) {


                    }
                })
                .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                    @Override
                    public void onFormSubmitted(String feedback) {

                    }
                }).build();


        ratingDialog.show();


if (ratingDialog.isShowing()){

} else
{
    Intent a = new Intent(Intent.ACTION_MAIN);
    a.addCategory(Intent.CATEGORY_HOME);
    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(a);
}






    }}