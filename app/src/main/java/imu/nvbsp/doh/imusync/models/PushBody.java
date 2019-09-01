package imu.nvbsp.doh.imusync.models;

import java.util.List;

public class PushBody {

    public List<Donor> changes;

    public PushBody(List<Donor> changes) {
        this.changes = changes;
    }
}
