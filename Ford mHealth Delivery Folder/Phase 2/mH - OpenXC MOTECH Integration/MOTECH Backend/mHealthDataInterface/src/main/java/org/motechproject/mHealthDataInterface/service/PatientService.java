package org.motechproject.mHealthDataInterface.service;


import java.util.List;
import org.motechproject.mHealthDataInterface.bean.Location;
import org.motechproject.mHealthDataInterface.bean.Patient;
import org.motechproject.mHealthDataInterface.bean.Encounter;
import org.motechproject.mHealthDataInterface.bean.PatientLocation;
import org.motechproject.mHealthDataInterface.utility.mHealthException;

public interface PatientService {

    /**
     *
     * get mother details
     *
     */
     Patient getMotherDetail(String motherId) throws mHealthException;

    /**
     *
     * get mother's village details
     *
     */
     Location getMotherVillage(String motherId) throws mHealthException;

    /**
     *
     * get visits details of health workers of a mother
     *
     */
     List<Encounter> getVisitListByMotherId(String motherId) throws mHealthException;

    /**
     *
     * get mothers living in particular village
     *
     */
     List<PatientLocation> getMothersByVillage(String village) throws mHealthException;

}
