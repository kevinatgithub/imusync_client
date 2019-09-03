package imu.nvbsp.doh.imusync;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;

import imu.nvbsp.doh.imusync.libs.ResolvedConflictClick;
import imu.nvbsp.doh.imusync.libs.ResolvedConflictsAdapter;
import imu.nvbsp.doh.imusync.models.Conflict;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class ReviewResolved extends AppCompatActivity {

    Realm realm;
    RealmResults<Conflict> conflicts;
    EditText txtSearch;
    RecyclerView resolvedConflicts;
    ResolvedConflictsAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_resolved);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        txtSearch = findViewById(R.id.txtSearch);
        resolvedConflicts = findViewById(R.id.resolvedConflicts);
        layoutManager = new LinearLayoutManager(this);
        resolvedConflicts.setLayoutManager(layoutManager);

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshList();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        RealmQuery query = realm.where(Conflict.class).isNotNull("done");

        if(txtSearch.getText().length() > 0){
            conflicts = query.contains("done.lname",txtSearch.getText().toString(),Case.INSENSITIVE).sort("seqno",Sort.DESCENDING).findAll();
        }else{
            conflicts = query.sort("seqno",Sort.DESCENDING).findAll();
        }

        ArrayList<Conflict> result = new ArrayList<>();
        result.addAll(conflicts);

        adapter = new ResolvedConflictsAdapter(this, result, new ResolvedConflictClick() {
            @Override
            public void onClick(Conflict conflict) {
                confirmDelete(conflict);
            }
        });
        resolvedConflicts.setAdapter(adapter);
    }

    private void confirmDelete(final Conflict conflict) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Return to unresolved list?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realm.beginTransaction();
                conflict.setDone(null);
                conflict.getAttributeSelections().deleteAllFromRealm();
                realm.commitTransaction();
                refreshList();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
