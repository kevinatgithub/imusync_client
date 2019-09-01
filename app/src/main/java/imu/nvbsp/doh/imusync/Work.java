package imu.nvbsp.doh.imusync;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import co.dift.ui.SwipeToAction;
import imu.nvbsp.doh.imusync.libs.SwipeConflictAdapter;
import imu.nvbsp.doh.imusync.models.AttributeSelection;
import imu.nvbsp.doh.imusync.models.Conflict;
import imu.nvbsp.doh.imusync.models.ConflictRow;
import imu.nvbsp.doh.imusync.models.Donor;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;

public class Work extends AppCompatActivity {

    private Conflict conflict;
    Realm realm;
    TextView seqno, fullname, stat;
    RecyclerView conflicts;
    SwipeConflictAdapter adapter;
    ArrayList<ConflictRow> conflictRows;
    LinearLayoutManager layoutManager;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        seqno = findViewById(R.id.seqno);
        fullname = findViewById(R.id.fullname);
        stat = findViewById(R.id.stat);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(false);
        conflicts = findViewById(R.id.conflicts);
        layoutManager = new LinearLayoutManager(this);
        conflicts.setLayoutManager(layoutManager);
        conflicts.setHasFixedSize(true);

        findViewById(R.id.useLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmSide(0);
            }
        });

        findViewById(R.id.useRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmSide(1);
            }
        });

        doNextJob();
    }

    private void confirmSide(final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String side = i == 0 ? "left" : "right";
        builder.setTitle("Use data from " + side + "?");
        builder.setMessage("All data from "+ side+ " side will be used together with the values selected explicitly.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i2) {
                buildSolution(i);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void buildSolution(int side) {
        Donor donor;
        if(side == 0){
            donor = realm.copyFromRealm(conflict.getLeft());
        }else{
            donor = realm.copyFromRealm(conflict.getRight());
        }

        donor.setId(conflict.getSeqno() + "f");
        realm.beginTransaction();
        donor = realm.copyToRealmOrUpdate(donor);
//        RealmResults<AttributeSelection> attributeSelections = realm.where(AttributeSelection.class).equalTo("seqno",conflict.getSeqno()).findAll();
//        RealmList<AttributeSelection> selections = new RealmList<>();
//        selections.addAll(attributeSelections);
        conflict.setDone(donor);
//        conflict.setAttributeSelections(selections);
        RealmList<AttributeSelection> finalSelections = new RealmList<>();
        for(AttributeSelection selection: selections){
            selection.setId(nextAttributeSelectionID());
            selection = realm.copyToRealmOrUpdate(selection);
            finalSelections.add(selection);
        }
        conflict.setAttributeSelections(finalSelections);

        realm.copyToRealmOrUpdate(conflict);
        realm.commitTransaction();
//        Toast.makeText(this, "Moved next", Toast.LENGTH_SHORT).show();
        selections = new ArrayList<>();
        doNextJob();
    }

    private void doNextJob() {
        int total = realm.where(Conflict.class).findAll().size();
        int progress = realm.where(Conflict.class).isNotNull("done").findAll().size();
        progressBar.setMax(total);
        progressBar.setProgress(progress);
        stat.setText(String.valueOf(progress) + " / " + String.valueOf(total));

        conflict = realm.where(Conflict.class).isNull("done").sort("seqno",Sort.ASCENDING).findFirst();

        if(conflict != null){
            seqno.setText(conflict.getSeqno());
            fullname.setText(conflict.getLeft().getFname() + " , " + conflict.getLeft().getLname());
            conflictRows = getConflictRows();
            adapter = new SwipeConflictAdapter(conflictRows,this);
            conflicts.setAdapter(adapter);
            prepareSwipeListeners();
        }
    }

    private SwipeToAction swipe;
    private ArrayList<AttributeSelection> selections = new ArrayList<>();

    private void prepareSwipeListeners() {
        swipe = new SwipeToAction(conflicts,new SwipeToAction.SwipeListener<ConflictRow>(){

            @Override
            public boolean swipeLeft(ConflictRow itemData) {
                AttributeSelection left = buildAttributeSelection(itemData.getAttr(),itemData.getRightValue());
                AttributeSelection right = buildAttributeSelection(itemData.getAttr(),itemData.getLeftValue());
                selections.add(left);
                selections.remove(right);
                return false;
            }

            @Override
            public boolean swipeRight(ConflictRow itemData) {
                AttributeSelection left = buildAttributeSelection(itemData.getAttr(),itemData.getRightValue());
                AttributeSelection right = buildAttributeSelection(itemData.getAttr(),itemData.getLeftValue());
                selections.remove(left);
                selections.add(right);
                return false;
            }

            @Override
            public void onClick(final ConflictRow itemData) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Work.this);
                builder.setTitle("Merge value for " + itemData.getAttr() + "?");
                builder.setMessage("This action will merege both values, not applicable for lookup fields");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mergeAsAttributeSelection(itemData);

                    }
                });
                builder.show();
            }

            @Override
            public void onLongClick(final ConflictRow itemData) {

//                Toast.makeText(Work.this,  itemData.getAttr() + " set using left and right values", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mergeAsAttributeSelection(ConflictRow itemData) {
        int i = -1; int x = 0;
        for(ConflictRow row: conflictRows) {
            if (row.getAttr().equals(itemData.getAttr())) {
                i = x;
            }
            x++;
        }

        View view  = layoutManager.findViewByPosition(i);
        CardView cv = view.findViewById(R.id.cardView);
        cv.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));

        String value = itemData.getLeftValue() + " " + itemData.getRightValue();
        AttributeSelection merge = buildAttributeSelection(itemData.getAttr(),value);
        selections.add(merge);
    }


    private ArrayList<ConflictRow> getConflictRows() {
        Donor left = conflict.getLeft();
        Donor right = conflict.getRight();
        ArrayList<ConflictRow> conflictRows = new ArrayList<>();

        conflictRows = analyz(conflictRows,"donor_photo",left.getDonor_photo(),right.getDonor_photo());
        conflictRows = analyz(conflictRows,"donor_id",left.getDonor_id(),right.getDonor_id());
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

    public void saveAttributeSelection(int side, String attr, String value) {
        AttributeSelection selection = realm.where(AttributeSelection.class).equalTo("seqno",conflict.getSeqno()).equalTo("attr",attr).findFirst();
        if(selection == null){
            Number currentIdNum = realm.where(AttributeSelection.class).max("id");
            int nextId;
            if(currentIdNum == null) {
                nextId = 1;
            } else {
                nextId = currentIdNum.intValue() + 1;
            }
            selection = new AttributeSelection();
            selection.setId(nextId);
            selection.setSeqno(conflict.getSeqno());
            selection.setAttr(attr);
        }
        selection.setValue(value);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(selection);
        realm.commitTransaction();
    }

    private int nextAttributeSelectionID(){
        Number currentIdNum = realm.where(AttributeSelection.class).max("id");
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    public AttributeSelection buildAttributeSelection(String attr, String value) {
        AttributeSelection selection = realm.where(AttributeSelection.class).equalTo("seqno",conflict.getSeqno()).equalTo("attr",attr).findFirst();
        if(selection == null){
            selection = new AttributeSelection();
            selection.setSeqno(conflict.getSeqno());
            selection.setAttr(attr);
        }
        selection.setValue(value);
        return selection;
    }
}
