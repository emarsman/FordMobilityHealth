package org.motechproject.mHealthDataInterface.service;


import java.util.List;

import org.motechproject.mHealthDataInterface.bean.*;
import org.motechproject.mHealthDataInterface.utility.mHealthException;
import org.motechproject.mHealthDataInterface.bean.Person.PreferredAddress;

public interface PatientService {

    /**
     *
     * get patient details
     *
     */
     Patient getPatientDetail(String patientId) throws mHealthException;

    /**
     *
     * get patients detail by Name
     *
     */
    List<Patient> getPatientsDetailByName(String patientName) throws mHealthException;

    /**
     *
     * get patients village details
     *
     */
    PreferredAddress getPatientVillage(String patientId) throws mHealthException;

    /**
     *
     * get visits details of health workers of a patient
     *
     */
     List<Encounter> getVisitListByPatientId(String patientId) throws mHealthException;

    /**
     *
     * get patients living in particular village
     *
     */
     List<PatientLocation> getPatientsByVillage(String village) throws mHealthException;

    /**
     *
     * get patients living in particular postal code area
     *
     */
    List<PatientLocation> getPatientsByPostalCode(String postalCode) throws mHealthException;

}
