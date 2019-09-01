package imu.nvbsp.doh.imusync.libs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import co.dift.ui.SwipeToAction;
import imu.nvbsp.doh.imusync.R;
import imu.nvbsp.doh.imusync.models.ConflictRow;

public class SwipeConflictAdapter extends RecyclerView.Adapter {
    private ArrayList<ConflictRow> rows;
    private Context context;

    public SwipeConflictAdapter(ArrayList<ConflictRow> rows, Context context) {
        this.rows = rows;
        this.context = context;
    }

    @NonNull
    @Override
    public ConflictRowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View container = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_conflict,viewGroup,false);
        return new ConflictRowViewHolder(container);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final ConflictRow row = rows.get(i);

        ConflictRowViewHolder crholder = (ConflictRowViewHolder) viewHolder;
        crholder.data = row;
        final View convertView = crholder.getContainer();

        final CardView card = convertView.findViewById(R.id.cardView);
        final TextView attr = convertView.findViewById(R.id.attrName);
        final TextView attr2 = convertView.findViewById(R.id.attrName2);
        final TextView leftValue = convertView.findViewById(R.id.leftValue);
        final TextView rightValue = convertView.findViewById(R.id.rightValue);
        attr.setText(row.getAttr());
        attr2.setText(row.getAttr());
        if(!row.getAttr().equals("donor_photo") && !row.getAttr().equals("lfinger") && !row.getAttr().equals("rfinger")){
            leftValue.setText(row.getLeftValue());
            rightValue.setText(row.getRightValue());
        }else{
            leftValue.setText("[BLOB]");
            rightValue.setText("[BLOB]");
        }
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public class ConflictRowViewHolder extends SwipeToAction.ViewHolder<ConflictRow>{

        private View container;

        public ConflictRowViewHolder(View v) {
            super(v);
            this.container = v;
        }

        public View getContainer() {
            return container;
        }
    }
}
