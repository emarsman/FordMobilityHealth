package org.motechproject.OpenXCDataInterface.repository;

import org.motechproject.OpenXCDataInterface.domain.VehicleRegistrationRecord;
import org.motechproject.OpenXCDataInterface.domain.VehicleUploadRecord;
import org.motechproject.OpenXCDataInterface.util.OpenXCDataInterfaceException;
import java.util.List;

/**
 * Interface Name : VehicleDetailsRecordDao
 * Purpose    : DAO level Interface.
 */

public interface VehicleDetailsRecordDao {

    String registerVehicle(VehicleRegistrationRecord vehicleRegistrationRecord) throws OpenXCDataInterfaceException;

    String uploadVehicleData(VehicleUploadRecord vehicleUploadRecord) throws OpenXCDataInterfaceException;

    List<VehicleRegistrationRecord> getRegisteredVehicleList() throws OpenXCDataInterfaceException;

    List<VehicleUploadRecord> getAllVehicleCurrentLocation() throws OpenXCDataInterfaceException;

    VehicleUploadRecord getVehicleCurrentLocation(String vehicleId) throws OpenXCDataInterfaceException;

    List<VehicleUploadRecord> showRoute(String vehicleId) throws OpenXCDataInterfaceException;

    Integer getVehicleId(String vehRegnNo) throws OpenXCDataInterfaceException;

}
