package competition.onedata.hibernate.reveng.pojo;

// Generated Jan 24, 2014 10:35:50 PM by Hibernate Tools 3.4.0.CR1

import competition.onedata.hibernate.type.RoadStatusChar;
import java.util.Date;

/**
 * RoadSectionSpeed generated by hbm2java
 */
public class RoadSectionSpeed implements java.io.Serializable {

	private RoadSectionSpeedId id;
	private double ssSpeed;
	private RoadStatusChar ssStatus;
	private Date ssCaptureDate;

	public RoadSectionSpeed() {
	}

	public RoadSectionSpeed(RoadSectionSpeedId id, double ssSpeed,
			RoadStatusChar ssStatus, Date ssCaptureDate) {
		this.id = id;
		this.ssSpeed = ssSpeed;
		this.ssStatus = ssStatus;
		this.ssCaptureDate = ssCaptureDate;
	}

	public RoadSectionSpeedId getId() {
		return this.id;
	}

	public void setId(RoadSectionSpeedId id) {
		this.id = id;
	}

	public double getSsSpeed() {
		return this.ssSpeed;
	}

	public void setSsSpeed(double ssSpeed) {
		this.ssSpeed = ssSpeed;
	}

	public RoadStatusChar getSsStatus() {
		return this.ssStatus;
	}

	public void setSsStatus(RoadStatusChar ssStatus) {
		this.ssStatus = ssStatus;
	}

	public Date getSsCaptureDate() {
		return this.ssCaptureDate;
	}

	public void setSsCaptureDate(Date ssCaptureDate) {
		this.ssCaptureDate = ssCaptureDate;
	}

}
