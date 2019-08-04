package competition.onedata.hibernate.reveng.pojo;

// Generated Jan 24, 2014 10:35:50 PM by Hibernate Tools 3.4.0.CR1

/**
 * RoadSectionId generated by hbm2java
 */
public class RoadSectionId implements java.io.Serializable {

	private int rsStartPointKey;
	private int rsEndPointKey;

	public RoadSectionId() {
	}

	public RoadSectionId(int rsStartPointKey, int rsEndPointKey) {
		this.rsStartPointKey = rsStartPointKey;
		this.rsEndPointKey = rsEndPointKey;
	}

	public int getRsStartPointKey() {
		return this.rsStartPointKey;
	}

	public void setRsStartPointKey(int rsStartPointKey) {
		this.rsStartPointKey = rsStartPointKey;
	}

	public int getRsEndPointKey() {
		return this.rsEndPointKey;
	}

	public void setRsEndPointKey(int rsEndPointKey) {
		this.rsEndPointKey = rsEndPointKey;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RoadSectionId))
			return false;
		RoadSectionId castOther = (RoadSectionId) other;

		return (this.getRsStartPointKey() == castOther.getRsStartPointKey())
				&& (this.getRsEndPointKey() == castOther.getRsEndPointKey());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getRsStartPointKey();
		result = 37 * result + this.getRsEndPointKey();
		return result;
	}

}