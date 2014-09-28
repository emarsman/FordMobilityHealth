
package org.motechproject.mHealthDataInterface.bean;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Custom class for health workers
 */
public class Provider extends BaseMetadata {
	
	private transient final Log log = LogFactory.getLog(getClass());
	
	private Integer providerId;
	
	private Person person;
	
	private String identifier;
	
	public Provider() {
	}
	
	public Provider(Integer providerId) {
		this.providerId = providerId;
	}



	public Integer getId() {
		return getProviderId();
	}



	public void setId(Integer id) {
		setProviderId(id);
	}


	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}


	public Integer getProviderId() {
		return providerId;
	}

	/**
	 * @param person the person to set
	 *
	 */
	public void setPerson(Person person) {
		this.person = person;

		//blank out name so that there isn't double data sitting in the provider table.
		if (person != null) {
			setName(null);
		}
	}

	/**
	 * @return the person
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(providerId);
		sb.append(" providerName:");
		sb.append((person != null) ? person.getNames() : "");
		return "[Provider: providerId:" + sb.toString() + " ]";
	}
	

	@Override
	public String getName() {
		if (getPerson() != null && getPerson().getPreferredName() != null && getPerson().getPreferredName().getGivenName() != null && getPerson().getPreferredName().getGivenName().length() > 0) {
			return getPerson().getPreferredName().getGivenName();
		} else {
			return super.getName();
		}
	}
	

	@Override
	public void setName(String name) {
		super.setName(name);
		
		if (getPerson() != null && super.getName().length() > 0) {
		log.trace("Setting name for a provider ");
		}
	}
}
