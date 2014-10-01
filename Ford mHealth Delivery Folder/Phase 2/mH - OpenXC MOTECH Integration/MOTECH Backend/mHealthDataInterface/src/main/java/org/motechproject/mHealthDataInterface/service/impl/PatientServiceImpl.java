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
	 * get mother details
	 *
	 */
	@Override
	public Patient getMotherDetail(String motherId) throws mHealthException {

		Utility utility = new Utility();
        Patient mother = utility.getPatientDetail(motherId);
		
		return mother;
	}

    /**
     *
     * get mothers detail by Name
     *
     */
    @Override
    public List<Patient> getMothersDetailByName(String motherName) throws mHealthException {

        Utility utility = new Utility();
        List<Patient> mother = utility.getPatientsDetailByName(motherName);

        return mother;
    }

	/**
	 * 
	 * get mother's village details
	 *
	 */
	
	@Override
	public PreferredAddress getMotherVillage(String motherId) throws mHealthException {

		Utility utility = new Utility();
        PreferredAddress address = utility.getPatientVillage(motherId);

		return address;
	}
	
	/**
	 * 
	 * get visits details of health workers of a mother
	 *
	 */
	
	@Override
	public List<Encounter> getVisitListByMotherId(String motherId) throws mHealthException {

		Utility utility = new Utility();
		List<Encounter> visits = utility.getVisitListByPatientId(motherId);

		return visits;
	}

    /**
     *
     * get mothers living in particular village
     *
     */

    @Override
    public List<PatientLocation> getMothersByVillage(String village) throws mHealthException {

        Utility utility = new Utility();
        List<PatientLocation> details = utility.getPatientsByVillage(village);

        return details;
    }

    /**
     *
     * get mothers living in particular postal code
     *
     */

    @Override
    public List<PatientLocation> getMothersByPostalCode(String postalCode) throws mHealthException {

        Utility utility = new Utility();
        List<PatientLocation> details = utility.getPatientsByPostalCode(postalCode);

        return details;
    }

}
