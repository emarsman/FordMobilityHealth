package org.motechproject.mHealthDataInterface.service.impl;

import java.util.List;

import org.motechproject.mHealthDataInterface.bean.*;
import org.motechproject.mHealthDataInterface.service.PatientService;
import org.motechproject.mHealthDataInterface.utility.Utility;
import org.motechproject.mHealthDataInterface.utility.mHealthException;
import org.motechproject.mHealthDataInterface.bean.Person.PreferredAddress;

public class PatientServiceImpl implements PatientService {

	/**
	 * 
	 * get patient details
	 *
	 */
	@Override
	public Patient getPatientDetail(String patientId) throws mHealthException {

		Utility utility = new Utility();
        Patient patient = utility.getPatientDetail(patientId);
		
		return patient;
	}

    /**
     *
     * get patients detail by Name
     *
     */
    @Override
    public List<Patient> getPatientsDetailByName(String patientName) throws mHealthException {

        Utility utility = new Utility();
        List<Patient> patient = utility.getPatientsDetailByName(patientName);

        return patient;
    }

	/**
	 * 
	 * get patients village details
	 *
	 */
	
	@Override
	public PreferredAddress getPatientVillage(String patientId) throws mHealthException {

		Utility utility = new Utility();
        PreferredAddress address = utility.getPatientVillage(patientId);

		return address;
	}
	
	/**
	 * 
	 * get visits details of health workers of a patient
	 *
	 */
	
	@Override
	public List<Encounter> getVisitListByPatientId(String patientId) throws mHealthException {

		Utility utility = new Utility();
		List<Encounter> visits = utility.getVisitListByPatientId(patientId);

		return visits;
	}

    /**
     *
     * get patients living in particular village
     *
     */

    @Override
    public List<PatientLocation> getPatientsByVillage(String village) throws mHealthException {

        Utility utility = new Utility();
        List<PatientLocation> details = utility.getPatientsByVillage(village);

        return details;
    }

    /**
     *
     * get patients living in particular postal code
     *
     */

    @Override
    public List<PatientLocation> getPatientsByPostalCode(String postalCode) throws mHealthException {

        Utility utility = new Utility();
        List<PatientLocation> details = utility.getPatientsByPostalCode(postalCode);

        return details;
    }

}
