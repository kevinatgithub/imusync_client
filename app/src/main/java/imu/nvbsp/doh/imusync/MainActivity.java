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

import com.google.gson.Gson;

import java.util.ArrayList;

import imu.nvbsp.doh.imusync.libs.ApiRequest;
import imu.nvbsp.doh.imusync.libs.Session;
import imu.nvbsp.doh.imusync.models.AttributeSelection;
import imu.nvbsp.doh.imusync.models.Conflict;
import imu.nvbsp.doh.imusync.models.Donor;
import imu.nvbsp.doh.imusync.models.PullResponse;
import imu.nvbsp.doh.imusync.models.PushBody;
import imu.nvbsp.doh.imusync.models.PushResponse;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
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
        findViewById(R.id.review).setOnClickListener(this);



    }

    @Override
    protected void onResume() {
        super.onResume();
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
            int remaining = realm.where(Conflict.class).isNull("done").findAll().size();
            if(remaining == 0){
                findViewById(R.id.work).setEnabled(false);
                findViewById(R.id.push).setEnabled(true);
            }
            if(Session.get(this,"push",null) != null){
                findViewById(R.id.push).setEnabled(false);
            }
        }else{
            findViewById(R.id.work).setEnabled(false);
            findViewById(R.id.push).setEnabled(false);
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
                findViewById(R.id.push).setEnabled(false);
                pushData();
                break;
            case R.id.review:
                Intent review = new Intent(this,ReviewResolved.class);
                startActivity(review);
                break;
        }
    }

    private void pushData() {
        RealmResults<Conflict> toSubmit = realm.where(Conflict.class).isNotNull("done").findAll();
        ArrayList<Donor> finalList = new ArrayList<>();
        for(Conflict c:toSubmit){
            Donor d = realm.copyFromRealm(c.getDone());
            d.setSeqno(c.getSeqno());
            d = applyAttributeSelections(c.getAttributeSelections(),d);
            finalList.add(d);
        }

        Toast.makeText(this, "Uploading..", Toast.LENGTH_LONG).show();
        String user_id = Session.get(this,"user_id",null);
        Gson gson = new Gson();
        Call<PushResponse> response = api.push(user_id,new PushBody(finalList));
        response.enqueue(new Callback<PushResponse>() {
            @Override
            public void onResponse(Call<PushResponse> call, Response<PushResponse> response) {
//                Gson gson = new Gson();
//                userName.setText(gson.toJson(response.body()));
                try{
                    PushResponse pushResponse = response.body();
                    if(pushResponse.getStatus().equals("ok")){
                        findViewById(R.id.push).setEnabled(false);
                        Session.set(MainActivity.this,"push","Y");
                        Toast.makeText(MainActivity.this, "Upload succesful..", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Upload failed, consult kevin for reason!", Toast.LENGTH_LONG).show();
                }
                
            }

            @Override
            public void onFailure(Call<PushResponse> call, Throwable t) {
                Log.e("apierror",t.getMessage());
            }
        });
    }

    private Donor applyAttributeSelections(RealmList<AttributeSelection> attributeSelections, Donor d) {
        for(AttributeSelection s: attributeSelections){
            switch(s.getAttr()){
                case "donor_photo": d.setDonor_photo(s.getValue()); break;
                case "donor_id": d.setDonor_id(s.getValue()); break;
                case "name_suffix": d.setName_suffix(s.getValue()); break;
                case "lname": d.setLname(s.getValue()); break;
                case "fname": d.setFname(s.getValue()); break;
                case "mname": d.setMname(s.getValue()); break;
                case "gender": d.setGender(s.getValue()); break;
                case "civil_stat": d.setCivil_stat(s.getValue()); break;
                case "tel_no": d.setTel_no(s.getValue()); break;
                case "mobile_no": d.setMobile_no(s.getValue()); break;
                case "email": d.setEmail(s.getValue()); break;
                case "nationality": d.setNationality(s.getValue()); break;
                case "occupation": d.setOccupation(s.getValue()); break;
                case "home_no_st_blk": d.setHome_no_st_blk(s.getValue()); break;
                case "home_brgy": d.setHome_brgy(s.getValue()); break;
                case "home_citymun": d.setHome_citymun(s.getValue()); break;
                case "home_prov": d.setHome_prov(s.getValue()); break;
                case "home_region": d.setHome_region(s.getValue()); break;
                case "home_zip": d.setHome_zip(s.getValue()); break;
                case "office_no_st_blk": d.setOffice_no_st_blk(s.getValue()); break;
                case "office_brgy": d.setOffice_brgy(s.getValue()); break;
                case "office_citymun": d.setOffice_citymun(s.getValue()); break;
                case "office_prov": d.setOffice_prov(s.getValue()); break;
                case "office_region": d.setOffice_region(s.getValue()); break;
                case "office_zip": d.setOffice_zip(s.getValue()); break;
                case "donation_stat": d.setDonation_stat(s.getValue()); break;
                case "donor_stat": d.setDonor_stat(s.getValue()); break;
                case "deferral_basis": d.setDeferral_basis(s.getValue()); break;
                case "facility_cd": d.setFacility_cd(s.getValue()); break;
                case "lfinger": d.setLfinger(s.getValue()); break;
                case "rfinger": d.setRfinger(s.getValue()); break;
                case "created_by": d.setCreated_by(s.getValue()); break;
                case "created_dt": d.setCreated_dt(s.getValue()); break;
                case "updated_by": d.setUpdated_by(s.getValue()); break;
                case "updated_dt": d.setUpdated_dt(s.getValue()); break;
            }
        }
        return d;
    }

    private void pullData() {
        Toast.makeText(this, "Downloading data..", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void savePullData(ArrayList<PullResponse.PullData> data) {
        realm.beginTransaction();
        for(PullResponse.PullData pd: data){
            Conflict conflict = new Conflict();
            conflict.setSeqno(pd.getLeft().getSeqno());
            conflict.setLeft(pd.getLeft());
            conflict.setRight(pd.getRight());
            realm.copyToRealmOrUpdate(conflict);
        }
        realm.commitTransaction();
        Toast.makeText(this, "Data downloaded succesfully", Toast.LENGTH_LONG).show();
        findViewById(R.id.pull).setEnabled(false);
        findViewById(R.id.work).setEnabled(true);
    }

    private void asignToDevice(int i) {
        Session.set(this,"user_id",String.valueOf(i));
        selectUser.setVisibility(View.GONE);
        selectMode.setVisibility(View.VISIBLE);
    }
}
