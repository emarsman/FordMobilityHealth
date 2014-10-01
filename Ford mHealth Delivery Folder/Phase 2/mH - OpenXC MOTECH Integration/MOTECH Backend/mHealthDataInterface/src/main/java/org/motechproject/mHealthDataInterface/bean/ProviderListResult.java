package org.motechproject.mHealthDataInterface.bean;

import java.util.List;

/**
 * Custom class for Providers/Health Workers list
 */
public class ProviderListResult {

    private List<Provider> results;

    public List<Provider> getResults() {
        return results;
    }

    public void setResults(List<Provider> results) {
        this.results = results;
    }
}
