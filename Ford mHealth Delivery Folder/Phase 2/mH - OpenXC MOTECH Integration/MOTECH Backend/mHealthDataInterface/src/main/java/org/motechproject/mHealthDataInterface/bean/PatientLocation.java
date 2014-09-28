package org.motechproject.mHealthDataInterface.bean;

/**
 * Custom class for mothers locations
 */

public class PatientLocation {

    private String uUid;
    private String name;
    private String address1;
    private String address2;
    private String phone;

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }


    public String getuUid() {
        return uUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setuUid(String uUid) {
        this.uUid = uUid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
