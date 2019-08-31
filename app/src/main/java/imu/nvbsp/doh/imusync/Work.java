package imu.nvbsp.doh.imusync;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import imu.nvbsp.doh.imusync.libs.ConflictAdapter;
import imu.nvbsp.doh.imusync.models.Conflict;
import imu.nvbsp.doh.imusync.models.ConflictRow;
import imu.nvbsp.doh.imusync.models.Donor;
import io.realm.Realm;
import io.realm.Sort;

public class Work extends AppCompatActivity {

    private Conflict conflict;
    Realm realm;
    TextView seqno, fullname;
    ListView conflicts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        seqno = findViewById(R.id.seqno);
        fullname = findViewById(R.id.fullname);
        conflicts = findViewById(R.id.conflicts);

        conflict = realm.where(Conflict.class).isNull("done").sort("seqno",Sort.ASCENDING).findFirst();
//        Log.d("conflict", conflict.getLeft().getHome_brgy() + " " + conflict.getRight().getHome_brgy());

        if(conflict != null){
            seqno.setText(conflict.getSeqno());
            fullname.setText(conflict.getLeft().getFname() + " " + conflict.getLeft().getLname());
            ConflictAdapter adapter = new ConflictAdapter(this,getConflictRows());
            conflicts.setAdapter(adapter);
        }
    }

    private ArrayList<ConflictRow> getConflictRows() {
        Donor left = conflict.getLeft();
        Donor right = conflict.getRight();
        ArrayList<ConflictRow> conflictRows = new ArrayList<>();

        conflictRows = analyz(conflictRows,"donor_photo",left.getDonor_photo(),right.getDonor_photo());
        conflictRows = analyz(conflictRows,"donor_id",left.getDonor_id(),right.getDonor_id());
        conflictRows = analyz(conflictRows,"name_suffix",left.getName_suffix(),right.getName_suffix());
        conflictRows = analyz(conflictRows,"name_suffix",left.getName_suffix(),right.getName_suffix());
        conflictRows = analyz(conflictRows,"lname",left.getLname(),right.getLname());
        conflictRows = analyz(conflictRows,"fname",left.getFname(),right.getFname());
        conflictRows = analyz(conflictRows,"mname",left.getMname(),right.getMname());
        conflictRows = analyz(conflictRows,"gender",left.getGender(),right.getGender());
        conflictRows = analyz(conflictRows,"civil_stat",left.getCivil_stat(),right.getCivil_stat());
        conflictRows = analyz(conflictRows,"tel_no",left.getTel_no(),right.getTel_no());
        conflictRows = analyz(conflictRows,"mobile_no",left.getMobile_no(),right.getMobile_no());
        conflictRows = analyz(conflictRows,"email",left.getEmail(),right.getEmail());
        conflictRows = analyz(conflictRows,"nationality",left.getNationality(),right.getNationality());
        conflictRows = analyz(conflictRows,"occupation",left.getOccupation(),right.getOccupation());
        conflictRows = analyz(conflictRows,"home_no_st_blk",left.getHome_no_st_blk(),right.getHome_no_st_blk());
        conflictRows = analyz(conflictRows,"home_brgy",left.getHome_brgy(),right.getHome_brgy());
        conflictRows = analyz(conflictRows,"home_citymun",left.getHome_citymun(),right.getHome_citymun());
        conflictRows = analyz(conflictRows,"home_prov",left.getHome_prov(),right.getHome_prov());
        conflictRows = analyz(conflictRows,"home_region",left.getHome_region(),right.getHome_region());
        conflictRows = analyz(conflictRows,"home_zip",left.getHome_zip(),right.getHome_zip());
        conflictRows = analyz(conflictRows,"office_no_st_blk",left.getOffice_no_st_blk(),right.getOffice_no_st_blk());
        conflictRows = analyz(conflictRows,"office_brgy",left.getOffice_brgy(),right.getOffice_brgy());
        conflictRows = analyz(conflictRows,"office_citymun",left.getOffice_citymun(),right.getOffice_citymun());
        conflictRows = analyz(conflictRows,"office_prov",left.getOffice_prov(),right.getOffice_prov());
        conflictRows = analyz(conflictRows,"office_region",left.getOffice_region(),right.getOffice_region());
        conflictRows = analyz(conflictRows,"office_zip",left.getOffice_zip(),right.getOffice_zip());
        conflictRows = analyz(conflictRows,"donation_stat",left.getDonation_stat(),right.getDonation_stat());
        conflictRows = analyz(conflictRows,"donor_stat",left.getDonor_stat(),right.getDonor_stat());
        conflictRows = analyz(conflictRows,"deferral_basis",left.getDeferral_basis(),right.getDeferral_basis());
        conflictRows = analyz(conflictRows,"facility_cd",left.getFacility_cd(),right.getFacility_cd());
        conflictRows = analyz(conflictRows,"lfinger",left.getLfinger(),right.getLfinger());
        conflictRows = analyz(conflictRows,"rfinger",left.getRfinger(),right.getRfinger());
        conflictRows = analyz(conflictRows,"created_by",left.getCreated_by(),right.getCreated_by());
        conflictRows = analyz(conflictRows,"created_dt",left.getCreated_dt(),right.getCreated_dt());
        conflictRows = analyz(conflictRows,"updated_by",left.getUpdated_by(),right.getUpdated_by());
        conflictRows = analyz(conflictRows,"updated_dt",left.getUpdated_dt(),right.getUpdated_dt());

        return conflictRows;
    }

    private ArrayList<ConflictRow> analyz(ArrayList<ConflictRow> conflictRows,String attr, String left, String right){
        if(left != null){
            if(!left.equals(right)){
                conflictRows.add(new ConflictRow(attr,left,right));
            }
        }else if(right != null){
            conflictRows.add(new ConflictRow(attr,left,right));
        }
        return conflictRows;
    }
}
