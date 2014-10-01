package org.motechproject.mHealthDataInterface.service.impl;

import org.motechproject.mHealthDataInterface.bean.Provider;
import org.motechproject.mHealthDataInterface.service.HealthWorkerService;
import org.motechproject.mHealthDataInterface.utility.Utility;
import org.motechproject.mHealthDataInterface.utility.mHealthException;
import java.util.List;


public class HealthWorkerServiceImpl implements HealthWorkerService {

	/**
	 *
	 * verify health worker details
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
	 * get health worker details
	 *
	 */
	@Override
	public Provider getHealthWorkerDetail(String healthWorkerId) throws mHealthException {

		Utility utility = new Utility();
        Provider healthWorker = utility.getHealthWorkerDetail(healthWorkerId);

		return healthWorker;
	}

    /**
     *
     * get health workers details by name
     *
     */
    @Override
    public List<Provider> getHealthWorkersDetailByName(String healthWorkerName) throws mHealthException {

        Utility utility = new Utility();
        List<Provider> healthWorker = utility.getHealthWorkersDetailByName(healthWorkerName);

        return healthWorker;
    }
	
	

}
