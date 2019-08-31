package imu.nvbsp.doh.imusync;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import imu.nvbsp.doh.imusync.libs.ApiRequest;
import imu.nvbsp.doh.imusync.libs.Session;
import imu.nvbsp.doh.imusync.models.Conflict;
import imu.nvbsp.doh.imusync.models.Donor;
import imu.nvbsp.doh.imusync.models.PullResponse;
import io.realm.Realm;
import io.realm.RealmObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView selectUser, selectMode;
    TextView userName;
    ApiRequest api;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        api = Session.getApiInstance();

        selectUser = findViewById(R.id.selectUser);
        selectMode = findViewById(R.id.selectMode);
        userName = findViewById(R.id.userName);

        findViewById(R.id.kevin).setOnClickListener(this);
        findViewById(R.id.bing).setOnClickListener(this);
        findViewById(R.id.lester).setOnClickListener(this);
        findViewById(R.id.mj).setOnClickListener(this);
        findViewById(R.id.mac).setOnClickListener(this);
        findViewById(R.id.krissy).setOnClickListener(this);
        findViewById(R.id.jester).setOnClickListener(this);
        findViewById(R.id.extra).setOnClickListener(this);
        findViewById(R.id.pull).setOnClickListener(this);
        findViewById(R.id.work).setOnClickListener(this);
        findViewById(R.id.push).setOnClickListener(this);

        String user_id = Session.get(this,"user_id",null);
        if(user_id != null){

            selectUser.setVisibility(View.GONE);
            selectMode.setVisibility(View.VISIBLE);
            switch (Integer.parseInt(user_id)){
                case 0: userName.setText("kevs"); break;
                case 1: userName.setText("bing"); break;
                case 2: userName.setText("lester"); break;
                case 3: userName.setText("mj"); break;
                case 4: userName.setText("mac"); break;
                case 5: userName.setText("krissy"); break;
                case 6: userName.setText("jester"); break;
            }
        }

        if(realm.where(Conflict.class).findAll().size() > 0){
            findViewById(R.id.pull).setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.kevin:
                asignToDevice(0);
                break;
            case R.id.bing:
                asignToDevice(1);
                break;
            case R.id.lester:
                asignToDevice(2);
                break;
            case R.id.mj:
                asignToDevice(3);
                break;
            case R.id.mac:
                asignToDevice(4);
                break;
            case R.id.krissy:
                asignToDevice(5);
                break;
            case R.id.jester:
                asignToDevice(6);
                break;
            case R.id.extra:
                asignToDevice(7);
                break;
            case R.id.pull:
                pullData();
                break;
            case R.id.work:
                Intent work = new Intent(this,Work.class);
                startActivity(work);
                break;
            case R.id.push:
                Intent push = new Intent(this,Pull.class);
                startActivity(push);
                break;
        }
    }

    private void pullData() {
        String user_id = Session.get(this,"user_id",null);
        Call<PullResponse> response = api.pull(user_id);
        response.enqueue(new Callback<PullResponse>() {
            @Override
            public void onResponse(Call<PullResponse> call, Response<PullResponse> response) {
                int size = response.body().getData().size();
                savePullData(response.body().getData());
            }

            @Override
            public void onFailure(Call<PullResponse> call, Throwable t) {
                Log.e("apierror",t.getMessage());
            }
        });

    }

    private void savePullData(ArrayList<PullResponse.PullData> data) {
        realm.beginTransaction();
        for(PullResponse.PullData pd: data){
//            Donor left = realm.createObject(Donor.class,pd.getLeft());
            Donor left = pd.getLeft();
//            Donor right = realm.createObject(Donor.class,pd.getRight());
            Donor right = pd.getRight();
            Conflict conflict = realm.createObject(Conflict.class);
            conflict.setSeqno(pd.getLeft().getSeqno());
            conflict.setLeft(left);
            conflict.setRight(right);
            realm.copyToRealmOrUpdate(conflict);
        }
        realm.commitTransaction();
        Toast.makeText(this, "Data downloaded succesfully", Toast.LENGTH_LONG).show();
        findViewById(R.id.pull).setEnabled(false);
    }

    private void asignToDevice(int i) {
        Session.set(this,"user_id",String.valueOf(i));
        selectUser.setVisibility(View.GONE);
        selectMode.setVisibility(View.VISIBLE);
    }
}
