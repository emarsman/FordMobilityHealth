package org.motechproject.mHealthDataInterface.bean;

import java.util.List;

/**
 * Custom class for Visit list
 */
public class EncounterListResult {

    private List<Encounter> results;

    public List<Encounter> getResults() {
        return results;
    }

    public void setResults(List<Encounter> results) {
        this.results = results;
    }
}
