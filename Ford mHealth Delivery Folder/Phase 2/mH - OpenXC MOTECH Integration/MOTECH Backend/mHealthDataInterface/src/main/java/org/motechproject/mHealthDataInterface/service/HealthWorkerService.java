package org.motechproject.mHealthDataInterface.service;


import org.motechproject.mHealthDataInterface.bean.Provider;
import org.motechproject.mHealthDataInterface.utility.mHealthException;
import java.util.List;

public interface HealthWorkerService {

    /**
     *
     * verify health worker details
     *
     */
     boolean verifyHealthWorker(String healthWorkerId) throws mHealthException;


    /**
     *
     * get health worker details
     *
     */
     Provider getHealthWorkerDetail(String healthWorkerId) throws mHealthException;

    /**
     *
     * get health workers detail by name
     *
     */
    List<Provider> getHealthWorkersDetailByName(String healthWorkerName) throws mHealthException;
	

}
