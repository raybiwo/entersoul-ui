package core.dto;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Id;

public class DeploymentDto {
	private int id;
	private String depName;
	private String devName;
	private String projectName;
	private Date deployDate;
	private String status;
	private String pushFixing;
	private String issue;
	
	
	/**
	 * Author Raybiwo
	 */
	public DeploymentDto() {
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDepName() {
		return depName;
	}
	
	public void setDepName(String depName) {
		this.depName = depName;
	}
	
	public String getDevName() {
		return devName;
	}
	
	public void setDevName(String devName) {
		this.devName = devName;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public Date getDeployDate() {
		return deployDate;
	}
	
	public void setDeployDate(Date deployDate) {
		this.deployDate = deployDate;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getPushFixing() {
		return pushFixing;
	}
	
	public void setPushFixing(String pushFixing) {
		this.pushFixing = pushFixing;
	}
	
	public String getIssue() {
		return issue;
	}
	
	public void setIssue(String issue) {
		this.issue = issue;
	}
}
