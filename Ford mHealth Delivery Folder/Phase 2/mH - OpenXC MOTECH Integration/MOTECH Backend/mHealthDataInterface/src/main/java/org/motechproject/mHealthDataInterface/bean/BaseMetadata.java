
package org.motechproject.mHealthDataInterface.bean;

import java.util.Date;




public abstract class BaseMetadata extends BaseObject {
	


	private String name;
	
	private String description;
	
	private User creator;
	
	private Date dateCreated;
	
	private User changedBy;
	
	private Date dateChanged;
	

	private Boolean retired = Boolean.FALSE;
	
	private Date dateRetired;
	
	private User retiredBy;
	
	private String retireReason;
	

	public BaseMetadata() {
	}
	

	

	public String getName() {
		return name;
	}
	

	public void setName(String name) {
		this.name = name;
	}
	

	public String getDescription() {
		return description;
	}
	

	public void setDescription(String description) {
		this.description = description;
	}
	

	public User getCreator() {
		return creator;
	}
	

	public void setCreator(User creator) {
		this.creator = creator;
	}
	

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	

	public User getChangedBy() {
		return changedBy;
	}
	

	public void setChangedBy(User changedBy) {
		this.changedBy = changedBy;
	}
	

	public Date getDateChanged() {
		return dateChanged;
	}
	

	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}
	

	public Boolean isRetired() {
		return retired;
	}

	public Boolean getRetired() {
		return isRetired();
	}
	

	public void setRetired(Boolean retired) {
		this.retired = retired;
	}
	

	public Date getDateRetired() {
		return dateRetired;
	}
	

	public void setDateRetired(Date dateRetired) {
		this.dateRetired = dateRetired;
	}

	public User getRetiredBy() {
		return retiredBy;
	}
	

	public void setRetiredBy(User retiredBy) {
		this.retiredBy = retiredBy;
	}
	

	public String getRetireReason() {
		return retireReason;
	}
	

	public void setRetireReason(String retireReason) {
		this.retireReason = retireReason;
	}
	
}
