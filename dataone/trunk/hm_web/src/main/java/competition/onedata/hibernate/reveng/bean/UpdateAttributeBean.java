package competition.onedata.hibernate.reveng.bean;

public class UpdateAttributeBean {
	private String updateByFieldName;
	private String updateDateFieldName;
	private String createByFieldName;
	private String createDateFieldName;

	public String getUpdateByFieldName() {
		return updateByFieldName;
	}

	public void setUpdateByFieldName(String updateByFieldName) {
		this.updateByFieldName = updateByFieldName;
	}

	public String getUpdateDateFieldName() {
		return updateDateFieldName;
	}

	public void setUpdateDateFieldName(String updateDateFieldName) {
		this.updateDateFieldName = updateDateFieldName;
	}

	public String getCreateByFieldName() {
		return createByFieldName;
	}

	public void setCreateByFieldName(String createByFieldName) {
		this.createByFieldName = createByFieldName;
	}

	public String getCreateDateFieldName() {
		return createDateFieldName;
	}

	public void setCreateDateFieldName(String createDateFieldName) {
		this.createDateFieldName = createDateFieldName;
	}

}
