package org.motechproject.OpenXCDataInterface.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;


/**
 * Class Name : VehicleUploadRecord
 * Purpose    : Entity Class for saving and transporting every fields of the Database table.
 */

@Entity
public class VehicleUploadRecord {

    @Field (required = true)
    private Integer vehicleId;

    @Field
    private String vehSpeed;

    @Field (required = true)
    private String latitude;

    @Field (required = true)
    private String longitude;

    @Field (required = true)
    private String timeStamp;

    @Field
    private String address;


    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getVehSpeed() {
        return vehSpeed;
    }

    public void setVehSpeed(String vehSpeed) {
        this.vehSpeed = vehSpeed;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
