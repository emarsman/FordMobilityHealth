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
import org.motechproject.mHealthDataInterface.bean.Person;
import org.motechproject.mHealthDataInterface.bean.Person.PreferredAddress;
import org.motechproject.mHealthDataInterface.utility.mHealthException;

/**
 * Custom class behaving as DAO
 */
public class Utility {

    /**
     * get spring data source connection
     *
     *
     */
    JdbcTemplate jdbcTemplate = getDatabaseConnection();

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
     * get patients detail by name
     *
     *
     */
    public List<Patient> getPatientsDetailByName( String patientName) throws mHealthException {
        String path = "/patient?q=" + patientName + "&v=full";
        List<Patient> patient = null;

        String json = GenericUtility.getJsonObject(path);
        Gson gson = GenericUtility.gsonDateFormat();

        Map<Type, Object> adapters = new HashMap<Type, Object>();
        PatientListResult result = (PatientListResult) JsonUtils.readJsonWithAdapters(json,
                PatientListResult.class, adapters);

        return result.getResults();
    }


    /**
	 * 
	 * get patient village
	 *
	 */
	 
	public PreferredAddress getPatientVillage( String patientId) throws mHealthException {
		String path = "/person/" + patientId + "?v=full";
        PreferredAddress address = null;
        Person person = null;
      //  String locationUid = null;

    	String json = GenericUtility.getJsonObject(path);
       	Gson gson = GenericUtility.gsonDateFormat();

        if (gson != null && json != null) {
            person = gson.fromJson(json, Person.class);
        }

        if (person != null) {
            if (person.getPreferredAddress() != null) {
                address = person.getPreferredAddress();
            }
        }

        return address;
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
	 * verify health worker
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
     * get health Workers detail by name
     *
     */
    public List<Provider> getHealthWorkersDetailByName( String healthWorkerName) throws mHealthException {
        String path = "/provider?q=" + healthWorkerName + "&v=full";
        List<Provider> healthWorker = null;

        String json = GenericUtility.getJsonObject(path);
        Gson gson = GenericUtility.gsonDateFormat();

        Map<Type, Object> adapters = new HashMap<Type, Object>();
        ProviderListResult result = (ProviderListResult) JsonUtils.readJsonWithAdapters(json,
                ProviderListResult.class, adapters);

        return result.getResults();
    }

    /**
     *
     * get patient details in a particular village
     *
     */
    public List<PatientLocation> getPatientsByVillage( String villageName) throws mHealthException {

        List<PatientLocation> patientLocationsList = new ArrayList<PatientLocation>();

        String query = Constants.query_village +villageName + "%'";
        return getPatientLocations(patientLocationsList, query);
    }

    /**
     *
     * get patient details in a particular postal code
     *
     */
    public List<PatientLocation> getPatientsByPostalCode( String postalCode) throws mHealthException {

        List<PatientLocation> patientLocationsList = new ArrayList<PatientLocation>();

        String query = Constants.query_postalCode +postalCode + "'";
        return getPatientLocations(patientLocationsList, query);
    }

    private List<PatientLocation> getPatientLocations(List<PatientLocation> patientLocationsList, String query) {
        List<Map<String, Object>> patientList = jdbcTemplate.queryForList(query);

        if (patientList != null && !patientList.isEmpty()) {
            for (Map<String, Object> patientMap : patientList) {
                PatientLocation patientLocation = new PatientLocation();
                for (Iterator<Map.Entry<String, Object>> it = patientMap.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String, Object> entry = it.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (key.equalsIgnoreCase("uuid")) {
                        if (value != null) {
                            patientLocation.setuUid(value.toString());
                        }
                    }
                    if (key.equalsIgnoreCase("name")) {
                        if (value != null) {
                            patientLocation.setName(value.toString());
                        }
                    }
                    if (key.equalsIgnoreCase("village")) {
                        if (value != null) {
                            patientLocation.setVillage(value.toString());
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
                    if (key.equalsIgnoreCase("state_province")) {
                        if (value != null) {
                            patientLocation.setState(value.toString());
                        }
                    }
                    if (key.equalsIgnoreCase("postal_code")) {
                        if (value != null) {
                            patientLocation.setPostalCode(value.toString());
                        }
                    }
                    if (key.equalsIgnoreCase("country")) {
                        if (value != null) {
                            patientLocation.setCountry(value.toString());
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
