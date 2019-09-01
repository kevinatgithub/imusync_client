package imu.nvbsp.doh.imusync.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Donor extends RealmObject {

    @PrimaryKey
    private String id;
    private String seqno;
    private String donor_photo;
    private String donor_id;
    private String name_suffix;
    private String lname;
    private String fname;
    private String mname;
    private String bdate;
    private String gender;
    private String civil_stat;
    private String tel_no;
    private String mobile_no;
    private String email;
    private String nationality;
    private String occupation;
    private String home_no_st_blk;
    private String home_brgy;
    private String home_citymun;
    private String home_prov;
    private String home_region;
    private String home_zip;
    private String office_no_st_blk;
    private String office_brgy;
    private String office_citymun;
    private String office_prov;
    private String office_region;
    private String office_zip;
    private String donation_stat;
    private String donor_stat;
    private String deferral_basis;
    private String facility_cd;
    private String lfinger;
    private String rfinger;
    private String created_by;
    private String created_dt;
    private String updated_by;
    private String updated_dt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public String getDonor_photo() {
        return donor_photo;
    }

    public void setDonor_photo(String donor_photo) {
        this.donor_photo = donor_photo;
    }

    public String getDonor_id() {
        return donor_id;
    }

    public void setDonor_id(String donor_id) {
        this.donor_id = donor_id;
    }

    public String getName_suffix() {
        return name_suffix;
    }

    public void setName_suffix(String name_suffix) {
        this.name_suffix = name_suffix;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCivil_stat() {
        return civil_stat;
    }

    public void setCivil_stat(String civil_stat) {
        this.civil_stat = civil_stat;
    }

    public String getTel_no() {
        return tel_no;
    }

    public void setTel_no(String tel_no) {
        this.tel_no = tel_no;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getHome_no_st_blk() {
        return home_no_st_blk;
    }

    public void setHome_no_st_blk(String home_no_st_blk) {
        this.home_no_st_blk = home_no_st_blk;
    }

    public String getHome_brgy() {
        return home_brgy;
    }

    public void setHome_brgy(String home_brgy) {
        this.home_brgy = home_brgy;
    }

    public String getHome_citymun() {
        return home_citymun;
    }

    public void setHome_citymun(String home_citymun) {
        this.home_citymun = home_citymun;
    }

    public String getHome_prov() {
        return home_prov;
    }

    public void setHome_prov(String home_prov) {
        this.home_prov = home_prov;
    }

    public String getHome_region() {
        return home_region;
    }

    public void setHome_region(String home_region) {
        this.home_region = home_region;
    }

    public String getHome_zip() {
        return home_zip;
    }

    public void setHome_zip(String home_zip) {
        this.home_zip = home_zip;
    }

    public String getOffice_no_st_blk() {
        return office_no_st_blk;
    }

    public void setOffice_no_st_blk(String office_no_st_blk) {
        this.office_no_st_blk = office_no_st_blk;
    }

    public String getOffice_brgy() {
        return office_brgy;
    }

    public void setOffice_brgy(String office_brgy) {
        this.office_brgy = office_brgy;
    }

    public String getOffice_citymun() {
        return office_citymun;
    }

    public void setOffice_citymun(String office_citymun) {
        this.office_citymun = office_citymun;
    }

    public String getOffice_prov() {
        return office_prov;
    }

    public void setOffice_prov(String office_prov) {
        this.office_prov = office_prov;
    }

    public String getOffice_region() {
        return office_region;
    }

    public void setOffice_region(String office_region) {
        this.office_region = office_region;
    }

    public String getOffice_zip() {
        return office_zip;
    }

    public void setOffice_zip(String office_zip) {
        this.office_zip = office_zip;
    }

    public String getDonation_stat() {
        return donation_stat;
    }

    public void setDonation_stat(String donation_stat) {
        this.donation_stat = donation_stat;
    }

    public String getDonor_stat() {
        return donor_stat;
    }

    public void setDonor_stat(String donor_stat) {
        this.donor_stat = donor_stat;
    }

    public String getDeferral_basis() {
        return deferral_basis;
    }

    public void setDeferral_basis(String deferral_basis) {
        this.deferral_basis = deferral_basis;
    }

    public String getFacility_cd() {
        return facility_cd;
    }

    public void setFacility_cd(String facility_cd) {
        this.facility_cd = facility_cd;
    }

    public String getLfinger() {
        return lfinger;
    }

    public void setLfinger(String lfinger) {
        this.lfinger = lfinger;
    }

    public String getRfinger() {
        return rfinger;
    }

    public void setRfinger(String rfinger) {
        this.rfinger = rfinger;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_dt() {
        return created_dt;
    }

    public void setCreated_dt(String created_dt) {
        this.created_dt = created_dt;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getUpdated_dt() {
        return updated_dt;
    }

    public void setUpdated_dt(String updated_dt) {
        this.updated_dt = updated_dt;
    }
}
