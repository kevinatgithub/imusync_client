package imu.nvbsp.doh.imusync.models;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Conflict extends RealmObject {
    @PrimaryKey
    private String seqno;
    private Donor left;
    private Donor right;
    private Donor done;
    private RealmList<AttributeSelection> attributeSelections;

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

    public RealmList<AttributeSelection> getAttributeSelections() {
        return attributeSelections;
    }

    public void setAttributeSelections(RealmList<AttributeSelection> attributeSelections) {
        this.attributeSelections = attributeSelections;
    }
}
