package org.motechproject.OpenXCDataInterface.service;

import org.motechproject.OpenXCDataInterface.domain.VehicleRegistrationRecord;
import org.motechproject.OpenXCDataInterface.domain.VehicleUploadRecord;
import org.motechproject.OpenXCDataInterface.util.OpenXCDataInterfaceException;
import java.util.List;

/**
 * Interface Name : VehicleDetailsRecordService
 * Purpose    : Service level Interface.
 */

public interface VehicleDetailsRecordService {

    List<VehicleUploadRecord> getAllVehicleCurrentLocation() throws OpenXCDataInterfaceException;

    List<VehicleRegistrationRecord> getRegisteredVehicleList() throws OpenXCDataInterfaceException;

    VehicleUploadRecord getVehicleCurrentLocation(String vehicleId) throws OpenXCDataInterfaceException;

    List<VehicleUploadRecord> showRoute(String vehicleId) throws OpenXCDataInterfaceException;

    Integer getVehicleId(String vehRegnNo) throws OpenXCDataInterfaceException;

    String registerVehicle(VehicleRegistrationRecord vehicleRegistrationRecord) throws OpenXCDataInterfaceException;

    String uploadVehicleData(VehicleUploadRecord vehicleUploadRecord) throws OpenXCDataInterfaceException;

}

