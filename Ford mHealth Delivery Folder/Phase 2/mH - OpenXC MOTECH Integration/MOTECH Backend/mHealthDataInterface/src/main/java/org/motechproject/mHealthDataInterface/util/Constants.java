package org.motechproject.mHealthDataInterface.util;


/**
 * Custom class for constants used.
 */
public interface Constants {

    String baseUrl = "http://localhost:8080/openmrs/ws/rest/v1";
    String username = "admin";
    String password = "admin";
    String dbUrl = "jdbc:mysql://localhost:3306/openmrs";
    String dbDriver = "com.mysql.jdbc.Driver";
    String dbUsername = "root";
    String dbPassword = "hcl123";
    String query_village = "SELECT DISTINCT b.uuid, CONCAT( c.given_name, ' ', c.family_name ) AS name, e.city_village as village, e.address1, e.address2,e.state_province, e.postal_code,e.country, f.value AS phone " +
            "FROM patient a " +
            "INNER JOIN person b ON a.patient_id = b.person_id " +
            "INNER JOIN person_name c ON b.person_id = c.person_id " +
            "INNER JOIN patient_identifier d ON a.patient_id = d.patient_id " +
            "INNER JOIN person_address e ON b.person_id = e.person_id " +
            "INNER JOIN person_attribute f ON b.person_id = f.person_id " +
            "WHERE e.city_village like '%";
    String query_postalCode = "SELECT DISTINCT b.uuid, CONCAT( c.given_name, ' ', c.family_name ) AS name, e.city_village as village, e.address1, e.address2,e.state_province, e.postal_code,e.country, f.value AS phone " +
            "FROM patient a " +
            "INNER JOIN person b ON a.patient_id = b.person_id " +
            "INNER JOIN person_name c ON b.person_id = c.person_id " +
            "INNER JOIN patient_identifier d ON a.patient_id = d.patient_id " +
            "INNER JOIN person_address e ON b.person_id = e.person_id " +
            "INNER JOIN person_attribute f ON b.person_id = f.person_id " +
            "WHERE e.postal_code ='";
    String msg = "object not found";

}
