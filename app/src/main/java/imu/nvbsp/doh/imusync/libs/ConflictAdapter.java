package imu.nvbsp.doh.imusync.libs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import imu.nvbsp.doh.imusync.R;
import imu.nvbsp.doh.imusync.models.ConflictRow;

public class ConflictAdapter extends ArrayAdapter<ConflictRow> {

    private Context context;

    public ConflictAdapter(@NonNull Context context, ArrayList<ConflictRow> conflictRows) {
        super(context, 0, conflictRows);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ConflictRow row = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_conflict,parent,false);
        }

        TextView attr = convertView.findViewById(R.id.attrName);
        TextView leftValue = convertView.findViewById(R.id.leftValue);
        TextView rightValue = convertView.findViewById(R.id.rightValue);
        attr.setText(row.getAttr());
        if(!row.getAttr().equals("donor_photo") && !row.getAttr().equals("lfinger") && !row.getAttr().equals("rfinger")){
            leftValue.setText(row.getLeftValue());
            rightValue.setText(row.getRightValue());
        }else{
            leftValue.setText("[BLOB]");
            rightValue.setText("[BLOB]");
        }

        return convertView;
    }
}
