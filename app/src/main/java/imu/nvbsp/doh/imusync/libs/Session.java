package imu.nvbsp.doh.imusync.libs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Session {

    public static String URL = "http://192.168.43.98/imusync/public/api/";

    public static ApiRequest getApiInstance(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiRequest.class);
    }

    private static SharedPreferences prefs;
    private static Gson gson;

    private static void init(Context context){
        if(prefs == null){
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
        }

        if(gson == null){
            gson = new Gson();
        }
    }

    public static void set(Context context, String key, String value){
        init(context);
        prefs.edit().putString(key,value).commit();
    }

    public static String get(Context context,String key, String defValue){
        init(context);
        String value = prefs.getString(key,"");
        if(value.equals("")){
            return defValue;
        }
        return value;
    }

    public static void delete(Context context,String key){
        init(context);
        prefs.edit().remove(key).commit();
    }

}
