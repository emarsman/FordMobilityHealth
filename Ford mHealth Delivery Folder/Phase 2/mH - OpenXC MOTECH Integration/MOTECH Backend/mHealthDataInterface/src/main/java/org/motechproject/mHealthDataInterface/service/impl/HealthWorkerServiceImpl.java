package org.motechproject.mHealthDataInterface.service.impl;

import org.motechproject.mHealthDataInterface.bean.Provider;
import org.motechproject.mHealthDataInterface.service.HealthWorkerService;
import org.motechproject.mHealthDataInterface.utility.Utility;
import org.motechproject.mHealthDataInterface.utility.mHealthException;


public class HealthWorkerServiceImpl implements HealthWorkerService {

	/**
	 *
	 * verify healthworker details
	 *
	 */
	@Override
	public boolean verifyHealthWorker(String healthWorkerId) throws mHealthException {
	
		Utility utility = new Utility();
		if (utility.verifyHealthWorker(healthWorkerId))
			return true;

		return false;
	}


	/**
	 *
	 * get healthworker details
	 *
	 */
	@Override
	public Provider getHealthWorkerDetail(String healthWorkerId) throws mHealthException {

		Utility utility = new Utility();
        Provider healthWorker = utility.getHealthWorkerDetail(healthWorkerId);

		return healthWorker;
	}

	
	

}
