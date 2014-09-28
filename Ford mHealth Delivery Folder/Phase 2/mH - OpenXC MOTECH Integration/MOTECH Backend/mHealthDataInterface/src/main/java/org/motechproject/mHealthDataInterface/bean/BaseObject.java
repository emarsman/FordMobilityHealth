
package org.motechproject.mHealthDataInterface.bean;

import java.util.UUID;



public abstract class BaseObject {
	
	private String uuid = UUID.randomUUID().toString();
	

	public String getUuid() {
		return uuid;
	}
	

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public int hashCode() {
		if (getUuid() == null) {
			return super.hashCode();
		}
		return getUuid().hashCode();
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BaseObject)) {
			return false;
		}
        BaseObject other = (BaseObject) obj;

		if (getUuid() == null) {
			return false;
		}
		return getUuid().equals(other.getUuid());
	}
	


}
