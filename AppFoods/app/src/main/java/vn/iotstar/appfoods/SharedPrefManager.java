package vn.iotstar.appfoods;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "volleyregisterlogin";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_FULLNAME = "keyfullname";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_GENDER = "keygender";
    private static final String KEY_ID = "keyid";
    private static final String KEY_IMAGES = "keyimages";
    private static SharedPrefManager mInstance;
    private static Context ctx;

    public SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(User user) {
        SharedPreferences SharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getName());
        editor.putString(KEY_FULLNAME, user.getFname());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_GENDER, user.getGender());
        editor.putString(KEY_IMAGES, user.getImages());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences SharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  SharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    public User getUSer() {
        SharedPreferences SharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                SharedPreferences.getInt(KEY_ID, -1),
                SharedPreferences.getString(KEY_USERNAME, null),
                SharedPreferences.getString(KEY_FULLNAME, null),
                SharedPreferences.getString(KEY_EMAIL, null),
                SharedPreferences.getString(KEY_GENDER, null),
                SharedPreferences.getString(KEY_IMAGES, null)
        );
    }

    public void logout() {
        SharedPreferences SharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx, LoginActivity.class));
    }
}
