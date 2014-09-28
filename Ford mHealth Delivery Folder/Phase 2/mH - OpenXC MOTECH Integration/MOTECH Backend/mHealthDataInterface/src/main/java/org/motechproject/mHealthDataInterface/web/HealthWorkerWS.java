package org.motechproject.mHealthDataInterface.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.motechproject.mHealthDataInterface.bean.Provider;
import org.motechproject.mHealthDataInterface.service.HealthWorkerService;

import org.motechproject.mHealthDataInterface.util.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.motechproject.mHealthDataInterface.utility.mHealthException;
import com.google.gson.Gson;


@Controller
public class HealthWorkerWS {

    private HealthWorkerService healthWorkerService;

    public void setHealthWorkerService(HealthWorkerService healthWorkerService) {

        this.healthWorkerService = healthWorkerService;
    }

    
    /**
	 * 
	 * verify healthworker details
	 *
	 */
    
    @RequestMapping(value = "/verifyHealthWorker/{healthWorkerId}", produces = "application/json")
	public @ResponseBody String verifyHealthWorker(@PathVariable String healthWorkerId) {

       	Gson gson = new Gson();
        String json = null;
        String msg = null;

    	try {
            if (healthWorkerService != null && healthWorkerId != null && healthWorkerId.length() > 0) {

                if (healthWorkerService.verifyHealthWorker(healthWorkerId)) {
                   msg = "valid health worker";
                } else {
                    msg = "invalid health worker";
                }

            } else {
                msg = Constants.msg;
                json = gson.toJson(msg);

                return json;
            }

    	} catch (mHealthException e) {
    		msg = Constants.msg;
    	}

        json = gson.toJson(msg);
        return json;
	}
    

    /**
	 * 
	 * get healthworker details
	 *
	 */

    @RequestMapping(value = "/healthWorkerDetail/{healthWorkerId}", produces = "application/json")
	public @ResponseBody String getHealthWorkerDetail(@PathVariable String healthWorkerId)
	{
        Gson gson = new Gson();

        String json = null;
        String returnVal = null;
        Provider healthworker = null;
        String msg = null;

        try {

            if (healthWorkerService != null && healthWorkerId != null && healthWorkerId.length() > 0) {
                healthworker = healthWorkerService.getHealthWorkerDetail(healthWorkerId);
            }

            if (healthworker != null){
                json = gson.toJson(healthworker);

                returnVal = json;
            } else {
                msg = Constants.msg;
                json = gson.toJson(msg);

                returnVal = json;
            }
        } catch(Exception e) {
            msg = Constants.msg;
            json = gson.toJson(msg);
            returnVal = json;
        }
         return returnVal;
	}

}