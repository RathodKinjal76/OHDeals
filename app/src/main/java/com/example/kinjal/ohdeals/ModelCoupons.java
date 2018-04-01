package com.example.kinjal.ohdeals;

/**
 * Created by Kinjal on 27-10-2017.
 */

public class ModelCoupons {

    private String vendorname;
    private String shortdescription;
    private String image;
    private String vendor_id;
    private String exp_date;
    private String Valid;
    private String Terms;

    public String getValid() {
        return Valid;
    }

    public void setValid(String valid) {
        Valid = valid;
    }

    public String getTerms() {
        return Terms;
    }

    public void setTerms(String terms) {
        Terms = terms;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }

    public String getShortdescription() {
        return shortdescription;
    }

    public void setShortdescription(String shortdescription) {
        this.shortdescription = shortdescription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }
}
