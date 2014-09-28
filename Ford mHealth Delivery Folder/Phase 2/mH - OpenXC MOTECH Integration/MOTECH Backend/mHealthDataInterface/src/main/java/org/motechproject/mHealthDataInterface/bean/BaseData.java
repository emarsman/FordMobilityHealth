
package org.motechproject.mHealthDataInterface.bean;

import java.util.Date;


public abstract class BaseData extends BaseObject {
	
	//***** Properties *****
	
	protected User creator;
	
	private Date dateCreated;
	
	private User changedBy;
	
	private Date dateChanged;
	
	private Boolean voided = Boolean.FALSE;
	
	private Date dateVoided;
	
	private User voidedBy;
	
	private String voidReason;
	

	/**
	 * Default Constructor
	 */
	public BaseData() {

	}
	

	/**
	 *
	 */
	public User getCreator() {
		return creator;
	}
	
	/**
	 *
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	/**
	 *
	 */
	public Date getDateCreated() {
		return dateCreated;
	}
	
	/**
	 *
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	/**
	 *
	 */
	public User getChangedBy() {
		return changedBy;
	}
	
	/**
	 *
	 */
	public void setChangedBy(User changedBy) {
		this.changedBy = changedBy;
	}
	
	/**
	 *
	 */
	public Date getDateChanged() {
		return dateChanged;
	}
	
	/**
	 *
	 */
	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}
	
	/**
	 *
	 */
	public Boolean isVoided() {
		return voided;
	}
	

	public Boolean getVoided() {
		return isVoided();
	}
	
	/**
	 *
	 */
	public void setVoided(Boolean voided) {
		this.voided = voided;
	}
	
	/**
	 *
	 */
	public Date getDateVoided() {
		return dateVoided;
	}
	
	/**
	 *
	 */
	public void setDateVoided(Date dateVoided) {
		this.dateVoided = dateVoided;
	}
	
	/**
	 *
	 */
	public User getVoidedBy() {
		return voidedBy;
	}
	
	/**
	 *
	 */
	public void setVoidedBy(User voidedBy) {
		this.voidedBy = voidedBy;
	}
	
	/**
	 *
	 */
	public String getVoidReason() {
		return voidReason;
	}
	
	/**
	 *
	 */
	public void setVoidReason(String voidReason) {
		this.voidReason = voidReason;
	}
	
}
