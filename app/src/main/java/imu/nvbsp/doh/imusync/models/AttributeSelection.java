package imu.nvbsp.doh.imusync.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AttributeSelection extends RealmObject {
    @PrimaryKey
    private int id;
    private String seqno;
    private String attr;
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
