package imu.nvbsp.doh.imusync.libs;

import android.support.v7.widget.CardView;

public interface ConflictClick {
    void onAttributeClick(CardView card, String attr, String left, String right);
}
