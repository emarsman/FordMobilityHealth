package org.motechproject.mHealthDataInterface.bean;

import java.util.List;

/**
 * Custom class for Patients list
 */
public class PatientListResult {

    private List<Patient> results;

    public List<Patient> getResults() {
        return results;
    }

    public void setResults(List<Patient> results) {
        this.results = results;
    }
}
