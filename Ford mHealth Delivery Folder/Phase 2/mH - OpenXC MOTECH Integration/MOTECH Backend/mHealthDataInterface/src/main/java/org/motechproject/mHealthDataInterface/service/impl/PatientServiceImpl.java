package org.motechproject.mHealthDataInterface.service.impl;

import java.util.List;
import org.motechproject.mHealthDataInterface.bean.Encounter;
import org.motechproject.mHealthDataInterface.bean.Location;
import org.motechproject.mHealthDataInterface.bean.Patient;
import org.motechproject.mHealthDataInterface.bean.PatientLocation;
import org.motechproject.mHealthDataInterface.service.PatientService;
import org.motechproject.mHealthDataInterface.utility.Utility;
import org.motechproject.mHealthDataInterface.utility.mHealthException;

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
	 * get mother's village details
	 *
	 */
	
	@Override
	public Location getMotherVillage(String motherId) throws mHealthException {

		Utility utility = new Utility();
		Location location = utility.getPatientVillage(motherId);

		return location;
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

}
