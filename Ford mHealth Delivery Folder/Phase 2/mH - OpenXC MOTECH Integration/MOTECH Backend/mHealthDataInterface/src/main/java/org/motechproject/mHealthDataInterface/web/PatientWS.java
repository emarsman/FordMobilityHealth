package org.motechproject.mHealthDataInterface.web;

import java.util.List;
import org.motechproject.mHealthDataInterface.bean.Location;
import org.motechproject.mHealthDataInterface.bean.Patient;
import org.motechproject.mHealthDataInterface.bean.Encounter;
import org.motechproject.mHealthDataInterface.bean.PatientLocation;
import org.motechproject.mHealthDataInterface.service.PatientService;
import com.google.gson.Gson;
import org.motechproject.mHealthDataInterface.util.Constants;
import org.motechproject.mHealthDataInterface.utility.mHealthException;
import org.motechproject.mHealthDataInterface.bean.Person.PreferredAddress;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PatientWS {

	private PatientService patientService;
	
    public void setPatientService(PatientService patientService) {

        this.patientService = patientService;
    }

    /**
	 * 
	 * get patient details 
	 *
	 */
    
    @RequestMapping(value = "/motherDetail/{motherId}", produces = "application/json")
   	public @ResponseBody String getMothersDetail(@PathVariable String motherId) {

    	String returnVal = null;
        Gson gson = new Gson();
        String json = null;
        Patient mother = null;

        try {
            if (patientService != null && motherId != null && motherId.length() > 0) {
                mother = patientService.getMotherDetail(motherId);
            }

       		if (mother != null) {
       			json = gson.toJson(mother);

       			returnVal = json;
       		} else {
                String msg = Constants.msg;
                json = gson.toJson(msg);

       			returnVal = json;
       		}

    	} catch (mHealthException e) {
            String msg = Constants.msg;
            json = gson.toJson(msg);

            returnVal = json;
       	}

        return returnVal;
    		
   	}

    @RequestMapping(value = "/mothersDetailByName/{motherName}", produces = "application/json")
    public @ResponseBody String getMothersDetailByName(@PathVariable String motherName) {

        String returnVal = null;
        Gson gson = new Gson();
        String json = null;
        List<Patient> mother = null;

        try {
            if (patientService != null && motherName != null && motherName.length() > 0) {
                mother = patientService.getMothersDetailByName(motherName);
            }

            if (mother != null) {
                json = gson.toJson(mother);

                returnVal = json;
            } else {
                String msg = Constants.msg;
                json = gson.toJson(msg);

                returnVal = json;
            }

        } catch (mHealthException e) {
            String msg = Constants.msg;
            json = gson.toJson(msg);

            returnVal = json;
        }

        return returnVal;

    }
    

    /**
	 * 
	 * get patient's location details 
	 *
	 */
    
    @RequestMapping(value = "/motherVillage/{motherId}", produces = "application/json")
   	public @ResponseBody String getPatientVillage(@PathVariable String motherId) {
    	String returnVal = null;
        Gson gson = new Gson();
        String json = null;
        PreferredAddress address = null;
        String msg = null;

        try	{

            if (patientService != null && motherId != null && motherId.length() > 0) {
                address = patientService.getMotherVillage(motherId);
            }

	   		if (address != null) {
	   			json = gson.toJson(address);

	   			returnVal = json;
	   		} else {
                msg = Constants.msg;
                json = gson.toJson(msg);

                returnVal = json;
	   		}

		} catch (mHealthException e) {
            msg = Constants.msg;
            json = gson.toJson(msg);

            returnVal = json;
		}

        return returnVal;
   			
   	}
    
    
    /**
	 * 
	 * get visits details of healthworkers of a patient
	 *
	 */
    
    @RequestMapping(value = "/visitListByMotherId/{motherId}", produces = "application/json")
   	public @ResponseBody String getVisitListByPatientId(@PathVariable String motherId) {
    	String returnVal = null;
        Gson gson = new Gson();
        String json = null;
        String msg = null;
        List<Encounter> list = null;

        try {
            if (patientService != null && motherId != null && motherId.length() > 0) {
                list = patientService.getVisitListByMotherId(motherId);
            }

	   		if (list != null && list.size() > 0) {
	   			json = gson.toJson(list);

	   			returnVal = json;
	   		} else {
                msg = Constants.msg;
                json = gson.toJson(msg);

                returnVal = json;
	   		}

		} catch (mHealthException e) {
            msg = Constants.msg;
            json = gson.toJson(msg);

            returnVal = json;
        }

        return returnVal;
   			
   	}

    /**
     *
     * get details of mothers in a village
     *
     */

    @RequestMapping(value = "/mothersByVillage/{village}", produces = "application/json")
    public @ResponseBody String getMothersByVillage(@PathVariable String village) {
        String returnVal = null;
        Gson gson = new Gson();
        String json = null;
        String msg = null;
        List<PatientLocation> list = null;

        try {
            if (patientService != null && village != null && village.length() > 0) {
                list = patientService.getMothersByVillage(village);
            }

            if (list != null && list.size() > 0) {
                json = gson.toJson(list);

                returnVal = json;
            } else {
                msg = Constants.msg;
                json = gson.toJson(msg);

                returnVal = json;
            }

        } catch (mHealthException e) {
            msg = Constants.msg;
            json = gson.toJson(msg);

            returnVal = json;
        }

        return returnVal;

    }

    /**
     *
     * get details of mothers in a postal code .
     *
     */
    @RequestMapping(value = "/mothersByPostalCode/{postalCode}", produces = "application/json")
    public @ResponseBody String getMothersByPostalCode(@PathVariable String postalCode) {
        String returnVal = null;
        Gson gson = new Gson();
        String json = null;
        String msg = null;
        List<PatientLocation> list = null;

        try {
            if (patientService != null && postalCode != null && postalCode.length() > 0) {
                list = patientService.getMothersByPostalCode(postalCode);
            }

            if (list != null && list.size() > 0) {
                json = gson.toJson(list);

                returnVal = json;
            } else {
                msg = Constants.msg;
                json = gson.toJson(msg);

                returnVal = json;
            }

        } catch (mHealthException e) {
            msg = Constants.msg;
            json = gson.toJson(msg);

            returnVal = json;
        }

        return returnVal;

    }

}