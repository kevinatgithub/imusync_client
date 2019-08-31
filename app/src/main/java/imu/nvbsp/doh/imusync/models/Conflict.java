package imu.nvbsp.doh.imusync.models;

import io.realm.RealmObject;

public class Conflict extends RealmObject {
    private String seqno;
    private Donor left;
    private Donor right;
    private Donor done;

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public Donor getLeft() {
        return left;
    }

    public void setLeft(Donor left) {
        this.left = left;
    }

    public Donor getRight() {
        return right;
    }

    public void setRight(Donor right) {
        this.right = right;
    }

    public Donor getDone() {
        return done;
    }

    public void setDone(Donor done) {
        this.done = done;
    }
}
