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
    
    @RequestMapping(value = "/patientDetail/{patientId}", produces = "application/json")
   	public @ResponseBody String getPatientDetail(@PathVariable String patientId) {

    	String returnVal = null;
        Gson gson = new Gson();
        String json = null;
        Patient patient = null;

        try {
            if (patientService != null && patientId != null && patientId.length() > 0) {
                patient = patientService.getPatientDetail(patientId);
            }

       		if (patient != null) {
       			json = gson.toJson(patient);

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
     * get patients details by name
     *
     */
    @RequestMapping(value = "/patientsDetailByName/{patientName}", produces = "application/json")
    public @ResponseBody String getPatientsDetailByName(@PathVariable String patientName) {

        String returnVal = null;
        Gson gson = new Gson();
        String json = null;
        List<Patient> patient = null;

        try {
            if (patientService != null && patientName != null && patientName.length() > 0) {
                patient = patientService.getPatientsDetailByName(patientName);
            }

            if (patient != null) {
                json = gson.toJson(patient);

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
	 * get patients location details
	 *
	 */
    
    @RequestMapping(value = "/patientVillage/{patientId}", produces = "application/json")
   	public @ResponseBody String getPatientVillage(@PathVariable String patientId) {
    	String returnVal = null;
        Gson gson = new Gson();
        String json = null;
        PreferredAddress address = null;
        String msg = null;

        try	{

            if (patientService != null && patientId != null && patientId.length() > 0) {
                address = patientService.getPatientVillage(patientId);
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
	 * get visits details of health workers of a patient
	 *
	 */
    
    @RequestMapping(value = "/visitListByPatientId/{patientId}", produces = "application/json")
   	public @ResponseBody String getVisitListByPatientId(@PathVariable String patientId) {
    	String returnVal = null;
        Gson gson = new Gson();
        String json = null;
        String msg = null;
        List<Encounter> list = null;

        try {
            if (patientService != null && patientId != null && patientId.length() > 0) {
                list = patientService.getVisitListByPatientId(patientId);
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
     * get details of patient in a village
     *
     */

    @RequestMapping(value = "/patientsByVillage/{village}", produces = "application/json")
    public @ResponseBody String getPatientsByVillage(@PathVariable String village) {
        String returnVal = null;
        Gson gson = new Gson();
        String json = null;
        String msg = null;
        List<PatientLocation> list = null;

        try {
            if (patientService != null && village != null && village.length() > 0) {
                list = patientService.getPatientsByVillage(village);
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
     * get details of patient in a postal code .
     *
     */
    @RequestMapping(value = "/patientsByPostalCode/{postalCode}", produces = "application/json")
    public @ResponseBody String getPatientsByPostalCode(@PathVariable String postalCode) {
        String returnVal = null;
        Gson gson = new Gson();
        String json = null;
        String msg = null;
        List<PatientLocation> list = null;

        try {
            if (patientService != null && postalCode != null && postalCode.length() > 0) {
                list = patientService.getPatientsByPostalCode(postalCode);
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