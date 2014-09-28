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
    String query = "SELECT DISTINCT a.patient_id, CONCAT( c.given_name, ' ', c.family_name ) AS name, f.address1, f.address2, g.value AS phone " +
            "FROM patient a " +
            "INNER JOIN person b ON a.patient_id = b.person_id " +
            "INNER JOIN person_name c ON b.person_id = c.person_id " +
            "INNER JOIN patient_identifier d ON a.patient_id = d.patient_id " +
            "INNER JOIN location e ON d.location_id = e.location_id " +
            "INNER JOIN person_address f ON b.person_id = f.person_id " +
            "INNER JOIN person_attribute g ON b.person_id = g.person_id " +
            "WHERE e.name = '";
    String msg = "object not found";

}
