package minimal.todo.com.todo.view;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Calendar;
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

public class CompletedActivity extends Fragment {
    private CompletedActivity.NotesAdaptered mAdapter;
    private List<Noted> notesList = new ArrayList<>();
    private List<Note> notesLists = new ArrayList<>();
    private DatabaseHelperb db;
    private DatabaseHelperb dbs;
    private AdView mAdView;
    private DatabaseHelper dbss;
    private boolean useDarkTheme ;
    private View v ;
    private TextView noNotesView;
    private TextView noNotesView2;
    private RecyclerView recyclerView;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_completed, container, false);
        noNotesView = v.findViewById(R.id.textV);
        noNotesView2 = v.findViewById(R.id.textV2);
        SharedPreferences preferences = this.getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
        dbs = new DatabaseHelperb(getActivity());
        notesList.addAll(dbs.getAllDakar());
        dbss = new DatabaseHelper(getActivity());
        notesLists.addAll(dbss.getAllData());
        if(useDarkTheme) {
            TextView textView = v.findViewById(R.id.textViewCompleted);
            textView.setTextColor(Color.WHITE);
            noNotesView = v.findViewById(R.id.textV);
            noNotesView2 = v.findViewById(R.id.textV2);
            noNotesView.setTextColor(Color.WHITE);
            RelativeLayout l = v.findViewById(R.id.main_layout);
            l.setBackgroundColor(Color.parseColor("#15181e"));
        }


        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(getActivity(), "ca-app-pub-8337023763045237~5314632945");
        AdView adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-8337023763045237/3357814719");
        mAdView = v.findViewById(R.id.adView3);
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


        recyclerView = v.findViewById(R.id.lil);
        mAdapter = new CompletedActivity.NotesAdaptered(getActivity(), notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        someMethod();
        toggleEmptyNotes();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view,final int position) {
                final Noted noted = notesList.get(position);
                String x = noted.getTimestamped();
                String y = noted.getTimed();
                String z = noted.getNoted();
                createNoteds(z,y,x);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        deleteNoted(position);
                        toggleEmptyNotes();
                        someMethod();
                    }
                }, 950); }
            @Override
            public void onLongClick(View view, int position) {}
        }));
        return v;
            }
    private  void createNoted(String noted, String timestamped, String timed) {
        long idb = db.insertNoted(noted,timestamped,timed);
        Noted n = db.getNoteb(idb);
        if (n != null) {
            notesList.add(0, n);
            mAdapter.notifyDataSetChanged();
        }}
    private  void createNoteds(String noted, String timestamped, String timed) {
        long id = dbss.insertNote(noted,timestamped,timed);
        Note n = dbss.getNote(id); }
    private void toggleEmptyNotes() {
        if (dbs.getNotesCountb() > 0) {
            noNotesView.setVisibility(View.GONE);
            noNotesView2.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
            noNotesView2.setVisibility(View.VISIBLE);
        }}
    private void updateNoted(String noted, String timed , String timestamped,  int position) {
        Noted n = notesList.get(position);
        n.setNoted(noted);
        n.setTimed(timed);
        n.setTimestamped(timestamped);
        db.updateNoteb(n);
        notesList.set(position, n);
        mAdapter.notifyItemChanged(position);
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
    public void deleteNoted(int position) {
        dbs.deleteNoted(notesList.get(position));
        notesList.remove(position);
        mAdapter.notifyItemRemoved(position);
        toggleEmptyNotes();
        someMethod();
    }
    private boolean keyboardShown(View rootView) {

        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    private void animateStrikeThrough1(final TextView tv) { }
    public static class NotesAdaptered extends RecyclerView.Adapter<minimal.todo.com.todo.view.CompletedActivity.NotesAdaptered.MyViewHolder>  {
        private Context context;
        private List<Noted> notesList;
        private CheckBox checkBox;
        private Toolbar toolbar;
        private TaskActivity.NotesAdapter mAdapter;
        private RecyclerView recyclerView;
        private TextView noNotesView;
        private MainActivity mainActivity;
        private DatabaseHelperb db;
        final private float PADDING = 45;
        public class MyViewHolder extends RecyclerView.ViewHolder  {
            public TextView note;
            public TextView time;
            public TextView timestamped;
            public CheckBox checkBox;
            public Toolbar toolbar;
            public Toolbar relativeLayout;
            public CardView cardView;
            public RelativeLayout l;
            public MyViewHolder(final View view) {
                super(view);
                note = view.findViewById(R.id.TextTask);
                timestamped = view.findViewById(R.id.time);
                checkBox = view.findViewById(R.id.checkBox);
                toolbar = view.findViewById(R.id.toolbar22);
                relativeLayout = view.findViewById(R.id.toolbar);
                cardView = view.findViewById(R.id.card);
                l = view.findViewById(R.id.line1); }}
        public NotesAdaptered(Context context, List<Noted> notesList) {
            this.context = context;
            this.notesList = notesList;
        }
        @Override
        public minimal.todo.com.todo.view.CompletedActivity.NotesAdaptered.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row_completed, parent, false);
            return new minimal.todo.com.todo.view.CompletedActivity.NotesAdaptered.MyViewHolder(itemView);        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onBindViewHolder(final minimal.todo.com.todo.view.CompletedActivity.NotesAdaptered.MyViewHolder holder, final int position) {
            final Noted noted = notesList.get(position);
            SharedPreferences preferences = this.context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
          boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
            if (useDarkTheme) {
                holder.cardView.setBackgroundColor(Color.parseColor("#2D3137"));
                holder.l.setBackgroundColor(Color.parseColor("#2D3137"));
                holder.note.setTextColor(Color.WHITE);
                holder.note.setBackgroundColor(Color.parseColor("#2D3137"));
                holder.timestamped.setBackgroundColor(Color.parseColor("#2D3137"));
            }
            holder.note.setText(noted.getNoted());
            holder.note.setPaintFlags(holder.note.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();
            java.text.DateFormat dateFormat = new  java.text.SimpleDateFormat("dd MMM yyyy");
            String todayAsString = dateFormat.format(today);
            holder.checkBox.setChecked(true);
            final String date = noted.getTimestamped();
            if (date.equals(todayAsString)){
                holder.timestamped.setText("Today");
            } else {
                holder.timestamped.setText(noted.getTimestamped());
            }
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                                TranslateAnimation moveLeft = new TranslateAnimation(0, 0,
                                        Animation.ABSOLUTE, -1100, Animation.ABSOLUTE,
                                        0.0f, Animation.ABSOLUTE, Animation.ABSOLUTE);
                                moveLeft.setDuration(300);
                                moveLeft.setFillAfter(true);
                                holder.cardView.startAnimation(moveLeft);
                                holder.cardView.setPadding(0, 0, 0, 0);
                            }
                    }, 550); } }); }
        @Override
        public int getItemCount() {
            return notesList.size();
        }}}
