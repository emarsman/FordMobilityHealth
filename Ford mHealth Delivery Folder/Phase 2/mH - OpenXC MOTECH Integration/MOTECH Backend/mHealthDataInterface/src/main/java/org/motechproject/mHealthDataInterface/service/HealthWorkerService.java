package org.motechproject.mHealthDataInterface.service;


import org.motechproject.mHealthDataInterface.bean.Provider;
import org.motechproject.mHealthDataInterface.utility.mHealthException;

public interface HealthWorkerService {

    /**
     *
     * verify healthworker details
     *
     */
     boolean verifyHealthWorker(String healthWorkerId) throws mHealthException;


    /**
     *
     * get healthworker details
     *
     */
     Provider getHealthWorkerDetail(String healthWorkerId) throws mHealthException;
	

}
