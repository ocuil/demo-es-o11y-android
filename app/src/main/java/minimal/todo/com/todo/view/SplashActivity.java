package minimal.todo.com.todo.view;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.airbnb.lottie.LottieAnimationView;
import minimal.todo.com.todo.R;
/**
 * Created by Michal Štrba on 01/08/18.
 */
public class SplashActivity extends Activity {
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        }
        setContentView(R.layout.activity_splash);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view);
        lottieAnimationView.setImageAssetsFolder("images");
        lottieAnimationView.setAnimation("data.json");
        lottieAnimationView.enableMergePathsForKitKatAndAbove(true);
        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                    Bundle bundle=new Bundle();
                    Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Catch block", Log.getStackTraceString(e));     }
            }};
        background.start();
    }}

