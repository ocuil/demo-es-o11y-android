package minimal.todo.com.todo.view;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import minimal.todo.com.todo.R;
import minimal.todo.com.todo.database.DatabaseHelper;
import minimal.todo.com.todo.database.DatabaseHelperb;
import minimal.todo.com.todo.database.model.Note;
import minimal.todo.com.todo.database.model.Noted;
import minimal.todo.com.todo.utils.RecyclerTouchListener;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Michal Štrba on 01/08/18.
 */

public class TaskActivity extends Fragment {
    private NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();
    private RelativeLayout main_layout;
    private RecyclerView recyclerView;
    private TextView noNotesView, noNotesView2;
    private CheckBox checkBox;
    private DatabaseHelper db;
    private AdView mAdView;
    private boolean delete = false;
    private boolean animation = true;
    private Calendar myCalendar;
    private Calendar myCalendarTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private   boolean useDarkTheme ;
    private Switch toggle1;
    public static final String TAG = MainActivity.class
            .getSimpleName();
    private static MainActivity mInstance;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";
    private CompletedActivity.NotesAdaptered mAdapters;
    private List<Noted> notesLists = new ArrayList<>();
    private DatabaseHelperb dbb;
   private View v;
    private static final int TIME_TO_AUTOMATICALLY_DISMISS_ITEM = 3000;
    private String noted;
    private String timestamped;
    private String timed;
    public TaskActivity() {}

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       v = inflater.inflate(R.layout.activity_home, container, false);
        SharedPreferences preferences = this.getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
if (useDarkTheme){
    TextView textView = v.findViewById(R.id.textViewCompleted);
    textView.setTextColor(Color.WHITE);
    noNotesView = v.findViewById(R.id.empty_notes_view);
    noNotesView.setTextColor(Color.WHITE);
    noNotesView2 = v.findViewById(R.id.empty_notes_view2);
    noNotesView2.setTextColor(Color.WHITE);

    RelativeLayout l = v.findViewById(R.id.main_layout);
    l.setBackgroundColor(Color.parseColor("#15181e"));
}
        checkBox = v.findViewById(R.id.checkBox);
        noNotesView = v.findViewById(R.id.empty_notes_view);
        noNotesView2 = v.findViewById(R.id.empty_notes_view2);
        db = new DatabaseHelper(getActivity());
        notesList.addAll(db.getAllData());
        recyclerView = v.findViewById(R.id.RecyclerView);
        mAdapter = new TaskActivity.NotesAdapter(getActivity(), notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        dbb = new DatabaseHelperb(getActivity());
        notesLists.addAll(dbb.getAllDakar());
        myCalendar = Calendar.getInstance();
        myCalendarTime = Calendar.getInstance();
        Adapter();
        someMethod();
        toggleEmptyNotes();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            public void checks(View view , final int position){deleteNote(position);}
            @Override
            public void onClick(View view, final int position) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (delete){
                           deleteNote(position);
                        }}
                }, 150);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (delete){
                            notesList.remove(position);
                            mAdapter.notifyItemRemoved(position);
                            someMethod();
                            toggleEmptyNotes();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_place, new TaskActivity()).addToBackStack(null).commit();
                    }}
                }, 950);
            }
            @Override
            public void onLongClick(View view, int position) {}
        }));
        return v; }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public  void Adapter(){
        Collections.sort(notesList, new Comparator<Note>(){
            public int compare(Note left, Note right) {
                return left.getTimestamp().compareTo(right.getTimestamp());
            }
        });
    }
    private void showNoteDialog(final boolean shouldUpdate, final Note note, final int position) {


        if(useDarkTheme) {
            final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dark);
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
            final View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);
            alertDialogBuilderUserInput.setView(view);
            final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
            alertDialog.show();



            // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
            MobileAds.initialize(getActivity(), "ca-app-pub-8337023763045237~5314632945");
            AdView adView = new AdView(getActivity());
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId("ca-app-pub-8337023763045237/7804202476");
            mAdView = view.findViewById(R.id.adView2);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
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




            TextView textView = view.findViewById(R.id.textView4);
            textView.setTextColor(Color.WHITE);
            ImageView imageButton2 = view.findViewById(R.id.imageView9);
            imageButton2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.back_white));
            RelativeLayout l = view.findViewById(R.id.mainl);
            l.setBackgroundColor(Color.parseColor("#15181e"));
            ImageView imageView2 = view.findViewById(R.id.IconDate);
            imageView2.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.calendars_dark));
            ImageView imageView3 = view.findViewById(R.id.imageView);
            imageView3.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.details_dark));
            TextView textView4 = view.findViewById(R.id.DateInIcon);
            textView4.setTextColor(Color.WHITE);
            imageButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.hide();
                }
            });
            final EditText inputNote = view.findViewById(R.id.EditTextNewTask);
            inputNote.setText(note.getNote());
            inputNote.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            inputNote.setRawInputType(InputType.TYPE_CLASS_TEXT);
            final EditText inputNote2 = view.findViewById(R.id.editTextNewTask2);
            inputNote2.setText(note.getTime());
            inputNote2.setImeOptions(EditorInfo.IME_ACTION_DONE);
            inputNote2.setRawInputType(InputType.TYPE_CLASS_TEXT);
            final   TextView  textDate2  =  view.findViewById(R.id.TextDate);
            textDate2.setText(note.getTimestamp());
            String x = textDate2.getText().toString();
            RelativeLayout rDate = view.findViewById(R.id.DateLayout);
            rDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.TimePickerDialogTheme,
                            new DatePickerDialog.OnDateSetListener() {

                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    myCalendar.set(Calendar.YEAR, year);
                                    myCalendar.set(Calendar.MONTH, monthOfYear);
                                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                    String myFormat = "dd MMM yyyy"; //In which you need put here
                                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                                    textDate2.setText(sdf.format(myCalendar.getTime()));
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                    final   TextView  textDate2  =  view.findViewById(R.id.TextDate);
                    textDate2.setTextColor(Color.parseColor("#218be7"));
                }
            });
            Button button = view.findViewById(R.id.AddButton);
            button.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(inputNote.getText().toString())) {
                        Toast.makeText(getActivity(), "You must write new task", Toast.LENGTH_SHORT).show();
                        return;
                    } else {}
                    boolean shouldUpdate = true;
                    Note note = null;

                    if (shouldUpdate) {
                        String myFormat = "dd MMM yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                        String myFormat2 = "hh:mm a"; //In which you need put here
                        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2);
                        updateNote(inputNote.getText().toString(), inputNote2.getText().toString(),sdf.format(myCalendar.getTime()),position);
                    } else {
                        String myFormat = "dd MMM yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                        String myFormat2 = "hh:mm yyyy"; //In which you need put here
                        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2);
                        createNote(inputNote.getText().toString(), inputNote2.getText().toString() ,sdf.format(myCalendar.getTime()));
                    }
                    alertDialog.dismiss();
                }


            });

        } else {
            final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Light);
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
            final View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);
            alertDialogBuilderUserInput.setView(view);
            final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
            alertDialog.show();


            // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
            MobileAds.initialize(getActivity(), "ca-app-pub-8337023763045237~5314632945");
            AdView adView = new AdView(getActivity());
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId("ca-app-pub-8337023763045237/7804202476");
            mAdView = view.findViewById(R.id.adView2);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            final EditText inputNote = view.findViewById(R.id.EditTextNewTask);
            inputNote.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                                                     @Override
                                                                     public void onGlobalLayout() {
                                                                         if (keyboardShown(inputNote.getRootView())) {
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



            ImageView imageView5 = view.findViewById(R.id.imageView9);
            imageView5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.hide();
                }
            });
            inputNote.setText(note.getNote());
            inputNote.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            inputNote.setRawInputType(InputType.TYPE_CLASS_TEXT);
            final EditText inputNote2 = view.findViewById(R.id.editTextNewTask2);
            inputNote2.setText(note.getTime());
            inputNote2.setImeOptions(EditorInfo.IME_ACTION_DONE);
            inputNote2.setRawInputType(InputType.TYPE_CLASS_TEXT);
            final TextView textDate2 = view.findViewById(R.id.TextDate);
            textDate2.setText(note.getTimestamp());
            String x = textDate2.getText().toString();
            RelativeLayout rDate = view.findViewById(R.id.DateLayout);
            rDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.TimePickerDialogTheme,
                            new DatePickerDialog.OnDateSetListener() {

                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    myCalendar.set(Calendar.YEAR, year);
                                    myCalendar.set(Calendar.MONTH, monthOfYear);
                                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                    String myFormat = "dd MMM yyyy"; //In which you need put here
                                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                                    textDate2.setText(sdf.format(myCalendar.getTime()));

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                    final TextView textDate2 = view.findViewById(R.id.TextDate);
                    textDate2.setTextColor(Color.parseColor("#218be7"));
                }
            });
            Button button = view.findViewById(R.id.AddButton);
            button.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(inputNote.getText().toString())) {
                        Toast.makeText(getActivity(), "You must write new task", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                    }
                    boolean shouldUpdate = true;
                    Note note = null;
                    if (shouldUpdate) {
                        String myFormat = "dd MMM yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                        String myFormat2 = "hh:mm a"; //In which you need put here
                        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2);
                        updateNote(inputNote.getText().toString(), inputNote2.getText().toString(), sdf.format(myCalendar.getTime()), position);
                    } else {
                        String myFormat = "dd MMM yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                        String myFormat2 = "hh:mm yyyy"; //In which you need put here
                        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2);
                        createNote(inputNote.getText().toString(), inputNote2.getText().toString(), sdf.format(myCalendar.getTime()));
                    }
                    alertDialog.dismiss();
                }});}}
    private void createNote(String note, String timestamp,String time) {
        long id = db.insertNote(note,timestamp,time);
        Note n = db.getNote(id);
        if (n != null) {
            notesList.add(0, n);
            mAdapter.notifyDataSetChanged();
            toggleEmptyNotes();
        }}
    private void updateNote(String note, String time , String timestamp,  int position) {
        Note n = notesList.get(position);
        n.setNote(note);
        n.setTime(time);
        n.setTimestamp(timestamp);
        db.updateNote(n);
        notesList.set(position, n);
        mAdapter.notifyItemChanged(position);
        toggleEmptyNotes();
    }

    private boolean keyboardShown(View rootView) {

        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    private void createNoted(String noted, String timestamped,String timed) {
        long idb = dbb.insertNoted(noted,timestamped,timed);
        Noted n = dbb.getNoteb(idb); }
    private void updateNoted(String noted, String timed , String timestamped,  int position) {
        Noted n = notesLists.get(position);
        n.setNoted(noted);
        n.setTimed(timed);
        n.setTimestamped(timestamped);
        dbb.updateNoteb(n);
        notesLists.set(position, n);
        mAdapters.notifyItemChanged(position);
    }
    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
    void someMethod() {
        Runnable task = new Runnable() {
            public void run() {
                int x = mAdapter.getItemCount();
                final   TextView textView = v.findViewById(R.id.TaskView);
                textView.setText(String.valueOf(x));
                }};
        worker.schedule(task, 1, TimeUnit.MILLISECONDS);
    }
    private void toggleEmptyNotes() {
        if (db.getNotesCount() > 0) {
           noNotesView.setVisibility(View.GONE);
            noNotesView2.setVisibility(View.GONE);
        } else {
           noNotesView.setVisibility(View.VISIBLE);
            noNotesView2.setVisibility(View.VISIBLE);
        }}
    public void deleteNote(int position) {
        db.deleteNote(notesList.get(position));
    }
    public void deleteB(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                delete = true;
            }}, 3000);
    }

        public class NotesAdapter extends RecyclerView.Adapter<minimal.todo.com.todo.view.TaskActivity.NotesAdapter.MyViewHolder>  {
        private Context context;
        private List<Note> notesList;
        private CheckBox checkBox;
        private Toolbar toolbar;
        private TaskActivity.NotesAdapter mAdapter;
        private RecyclerView recyclerView;
        private TextView noNotesView;
        private MainActivity mainActivity;
        private DatabaseHelper db;
        final private float PADDING = 45;

        public class MyViewHolder extends RecyclerView.ViewHolder  {
            public TextView note;
            public TextView time;
            public TextView timestamp;
            public CheckBox checkBox;
            public Toolbar toolbar;
            public Toolbar relativeLayout;
            public CardView cardView;
            public RelativeLayout l;

            public MyViewHolder(final View view) {
                super(view);
                note = view.findViewById(R.id.TextTask);
               timestamp = view.findViewById(R.id.time);
                checkBox = view.findViewById(R.id.checkBox);
                toolbar = view.findViewById(R.id.toolbar22);
                relativeLayout = view.findViewById(R.id.toolbar);
                cardView = view.findViewById(R.id.card);
                l = view.findViewById(R.id.line1);
            }}
        public NotesAdapter(Context context, List<Note> notesList) {
            this.context = context;
            this.notesList = notesList;
        }
        @Override
        public minimal.todo.com.todo.view.TaskActivity.NotesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.note_list_row, parent, false);
            return new minimal.todo.com.todo.view.TaskActivity.NotesAdapter.MyViewHolder(itemView);        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onBindViewHolder(final minimal.todo.com.todo.view.TaskActivity.NotesAdapter.MyViewHolder holder, final int position) {
            final Note note = notesList.get(position);
            if (useDarkTheme) {
                holder.cardView.setBackgroundColor(Color.parseColor("#3c4048"));
                holder.l.setBackgroundColor(Color.parseColor("#3c4048"));
                holder.note.setTextColor(Color.WHITE);
                holder.note.setBackgroundColor(Color.parseColor("#3c4048"));
                holder.timestamp.setBackgroundColor(Color.parseColor("#3c4048"));
            }
            holder.note.setText(note.getNote());
            String x =  note.getTimestamp();
            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();
            java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("dd MMM yyyy");
            java.text.DateFormat dateFormat2 = new java.text.SimpleDateFormat("dd MMM yyyy");
            String todayAsString = dateFormat.format(today);
                        String date = note.getTimestamp();
            if (date.equals(todayAsString)){
                holder.timestamp.setText("Today");
            } else {
                holder.timestamp.setText(note.getTimestamp());
            }
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNoteDialog(true, notesList.get(position), position);
                }
            });
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    delete = true;
                    String x = note.getTimestamp();
                    String y = note.getTime();
                    String z = note.getNote();
                    createNoted(z,y,x);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TranslateAnimation moveLeft = new TranslateAnimation(0, 0,
                                    Animation.ABSOLUTE, 1100, Animation.ABSOLUTE,
                                    0.0f, Animation.ABSOLUTE, Animation.ABSOLUTE);
                            moveLeft.setDuration(300);
                            moveLeft.setFillAfter(true);
                            holder.cardView.startAnimation(moveLeft);
                            holder.cardView.setPadding(0, 0, 0, 0);
                        }
                    }, 550); }});}
            @Override
        public int getItemCount() { return notesList.size(); }}
}