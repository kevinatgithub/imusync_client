package imu.nvbsp.doh.imusync.libs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import imu.nvbsp.doh.imusync.R;
import imu.nvbsp.doh.imusync.models.Conflict;

public class ResolvedConflictsAdapter extends RecyclerView.Adapter<ResolvedConflictsAdapter.ResolvedConflictViewHolder> {

    private Context context;
    private ArrayList<Conflict> conflicts;
    private ResolvedConflictClick click;

    public ResolvedConflictsAdapter(Context context, ArrayList<Conflict> conflicts, ResolvedConflictClick click) {
        this.context = context;
        this.conflicts = conflicts;
        this.click = click;
    }

    @NonNull
    @Override
    public ResolvedConflictViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View container = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_resolved_conflicts,viewGroup,false);
        return new ResolvedConflictViewHolder(container);
    }

    @Override
    public void onBindViewHolder(@NonNull ResolvedConflictViewHolder viewHolder, int i) {
        final Conflict conflict = conflicts.get(i);

        TextView seqno = viewHolder.convertView.findViewById(R.id.txtSequenceNo);
        seqno.setText(conflict.getSeqno());
        TextView fullname = viewHolder.convertView.findViewById(R.id.txtFullName);
        fullname.setText(conflict.getDone().getFname() + " " + conflict.getDone().getLname());

        viewHolder.convertView.findViewById(R.id.cardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onClick(conflict);
            }
        });

    }

    @Override
    public int getItemCount() {
        return conflicts.size();
    }

    public static class ResolvedConflictViewHolder extends RecyclerView.ViewHolder{

        View convertView;

        public ResolvedConflictViewHolder(@NonNull View itemView) {
            super(itemView);
            convertView = itemView;
//            seqno = itemView.findViewById(R.id.txtSequenceNo);
//            fullname = itemView.findViewById(R.id.fullname);
        }
    }
}
