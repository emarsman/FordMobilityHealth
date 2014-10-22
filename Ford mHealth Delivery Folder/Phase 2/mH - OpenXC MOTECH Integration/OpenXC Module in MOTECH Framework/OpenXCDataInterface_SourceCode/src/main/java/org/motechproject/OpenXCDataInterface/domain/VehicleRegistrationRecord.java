package org.motechproject.OpenXCDataInterface.domain;


import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import javax.jdo.annotations.Unique;


/**
 * Class Name : VehicleRegistrationRecord
 * Purpose    : Entity Class for saving and transporting every fields of the Database table.
 */

@Entity
public class VehicleRegistrationRecord {

    @Field (required = true)
    @Unique
    private Integer vehicleId;

    @Field (required = true)
    @Unique
    private String vehRegnNo;

    @Field (required = true)
    private String vehChasisNo;

    @Field (required = true)
    private String vehMake;

    @Field (required = true)
    private String vehModel;

    private String contactNo;

    private String emailId;

    @Field (required = true)
    private String registeredOn;


    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehRegnNo() {
        return vehRegnNo;
    }

    public void setVehRegnNo(String vehRegnNo) {
        this.vehRegnNo = vehRegnNo;
    }

    public String getVehChasisNo() {
        return vehChasisNo;
    }

    public void setVehChasisNo(String vehChasisNo) {
        this.vehChasisNo = vehChasisNo;
    }

    public String getVehMake() {
        return vehMake;
    }

    public void setVehMake(String vehMake) {
        this.vehMake = vehMake;
    }

    public String getVehModel() {
        return vehModel;
    }

    public void setVehModel(String vehModel) {
        this.vehModel = vehModel;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }



}
