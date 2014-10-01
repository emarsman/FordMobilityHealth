package org.motechproject.mHealthDataInterface.service;


import java.util.List;

import org.motechproject.mHealthDataInterface.bean.*;
import org.motechproject.mHealthDataInterface.utility.mHealthException;
import org.motechproject.mHealthDataInterface.bean.Person.PreferredAddress;

public interface PatientService {

    /**
     *
     * get mother details
     *
     */
     Patient getMotherDetail(String motherId) throws mHealthException;

    /**
     *
     * get mothers detail by Name
     *
     */
    List<Patient> getMothersDetailByName(String motherName) throws mHealthException;

    /**
     *
     * get mother's village details
     *
     */
    PreferredAddress getMotherVillage(String motherId) throws mHealthException;

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

    /**
     *
     * get mothers living in particular postal code area
     *
     */
    List<PatientLocation> getMothersByPostalCode(String postalCode) throws mHealthException;

}
