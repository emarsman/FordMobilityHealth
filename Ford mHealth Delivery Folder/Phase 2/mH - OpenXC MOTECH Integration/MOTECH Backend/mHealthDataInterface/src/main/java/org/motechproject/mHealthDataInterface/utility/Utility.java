package org.motechproject.mHealthDataInterface.utility;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.Gson;
import java.lang.reflect.Type;

import org.motechproject.mHealthDataInterface.util.Constants;
import org.springframework.jdbc.core.JdbcTemplate;


import org.motechproject.mHealthDataInterface.bean.*;
import org.motechproject.mHealthDataInterface.bean.Observation.ObservationValue;
import org.motechproject.mHealthDataInterface.bean.Observation.ObservationValueDeserializer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.motechproject.mHealthDataInterface.bean.PatientLocation;
import org.motechproject.mHealthDataInterface.utility.mHealthException;

/**
 * Custom class behaving as DAO
 */
public class Utility {

   /**
	 * get patient details
	 * 
	 *
	 */
	public Patient getPatientDetail( String patientId) throws mHealthException {
        String path = "/patient/" + patientId + "?v=full";
        Patient patient = null;

        String json = GenericUtility.getJsonObject(path);
        Gson gson = GenericUtility.gsonDateFormat();

        if (gson !=null && json != null) {
            patient = gson.fromJson(json, Patient.class);
        }

        return patient;
		}
	
	
	/**
	 * 
	 * get patient village
	 *
	 */
	 
	public Location getPatientVillage( String patientId) throws mHealthException {
		String path = "/patient/" + patientId + "?v=full";
        Patient patient = null;
        Location location = null;
        String locationUid = null;

    	String json = GenericUtility.getJsonObject(path);
       	Gson gson = GenericUtility.gsonDateFormat();

        if (gson != null && json != null) {
            patient = gson.fromJson(json, Patient.class);
        }

        if (patient != null) {
            if (patient.getIdentifiers() != null && patient.getIdentifiers().size() > 0) {
                locationUid = patient.getIdentifiers().get(0).getLocation().getUuid();
            }
        }

        String locPath = "/location/" + locationUid;
        String locJson = GenericUtility.getJsonObject(locPath);

        if (gson != null && locJson != null) {
            location = gson.fromJson(locJson, Location.class);
        }

        return location;
	}
	
	/**
	 * 
	 * get all visits by patient
	 *
	 */
	public List<Encounter> getVisitListByPatientId( String patientId) throws mHealthException {
		String path = "/encounter?patient=" + patientId + "&v=full";
        String json = GenericUtility.getJsonObject(path);

        Map<Type, Object> adapters = new HashMap<Type, Object>();
        adapters.put(ObservationValue.class, new ObservationValueDeserializer());
        EncounterListResult result = (EncounterListResult) JsonUtils.readJsonWithAdapters(json,
                EncounterListResult.class, adapters);

		return result.getResults();
	}
	
	
	
	/**
	 * 
	 * verify healthworker
	 *
	 */
	public boolean verifyHealthWorker( String healthWorkerId) throws mHealthException {
		
		String path = "/provider/" + healthWorkerId;
        Provider healthWorker = null;

    	String json = GenericUtility.getJsonObject(path);
    	Gson gson = GenericUtility.gsonDateFormat();

        if (gson != null && json != null) {
            healthWorker = gson.fromJson(json, Provider.class);
        }
        
        if (healthWorker != null) {
        	return true;
        }

		return false;
	}

	
	/**
	 * 
	 * get healthWorker details
	 *
	 */
    public Provider getHealthWorkerDetail( String healthWorkerId) throws mHealthException {

    	String path = "/provider/" + healthWorkerId;
        Provider healthWorker = null;

         	String json = GenericUtility.getJsonObject(path);
    	Gson gson = GenericUtility.gsonDateFormat();

        if (gson != null && json != null) {
           healthWorker = gson.fromJson(json, Provider.class);
        }

        return healthWorker;
    }

    /**
     *
     * get mother details in a particular village
     *
     */
    public List<PatientLocation> getPatientsByVillage( String villageName) throws mHealthException {

        List<PatientLocation> patientLocationsList = new ArrayList<PatientLocation>();

        JdbcTemplate jdbcTemplate = getDatabaseConnection();

        String query = Constants.query +villageName + "'";
        List<Map<String, Object>> patientList = jdbcTemplate.queryForList(query);

        if (patientList != null && !patientList.isEmpty()) {
            for (Map<String, Object> patientMap : patientList) {
                PatientLocation patientLocation = new PatientLocation();
                for (Iterator<Map.Entry<String, Object>> it = patientMap.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String, Object> entry = it.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (key.equalsIgnoreCase("patient_id")) {
                        if (value != null) {
                            patientLocation.setuUid(value.toString());
                        }
                    }
                    if (key.equalsIgnoreCase("name")) {
                        if (value != null) {
                            patientLocation.setName(value.toString());
                        }
                    }
                    if (key.equalsIgnoreCase("address1")) {
                        if (value != null) {
                            patientLocation.setAddress1(value.toString());
                        }
                    }
                    if (key.equalsIgnoreCase("address2")) {
                        if (value != null) {
                            patientLocation.setAddress2(value.toString());
                        }
                    }
                    if (key.equalsIgnoreCase("phone")) {
                        if (value != null) {
                            patientLocation.setPhone(value.toString());
                        }
                    }
                }
                patientLocationsList.add(patientLocation);
            }
        }
        return patientLocationsList;
    }

    /**
     *
     * Database connection for OpenMRS
     *
     */
    public JdbcTemplate getDatabaseConnection() {
        DriverManagerDataSource managerDataSource = new DriverManagerDataSource();

        managerDataSource.setDriverClassName(Constants.dbDriver);
        managerDataSource.setUrl(Constants.dbUrl);
        managerDataSource.setUsername(Constants.dbUsername);
        managerDataSource.setPassword(Constants.dbPassword);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(managerDataSource);

        return jdbcTemplate;

    }

}
