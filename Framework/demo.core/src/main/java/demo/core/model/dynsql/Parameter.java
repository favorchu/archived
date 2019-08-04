package demo.core.model.dynsql;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Parameter")
public class Parameter {
	public static final String DATA_TYPE_DATE = "DATE";
	public static final String DATA_TYPE_STRING = "STRING";
	public static final String DATA_TYPE_INTEGER = "INTEGER";
	public static final String DATA_TYPE_LONG = "LONG";
	public static final String DATA_TYPE_DOUBLE = "DOUBLE";
	public static final String DATA_TYPE_BIGDECIMAL = "BIGDECIMAL";

	protected String key;
	protected String type;
	protected String value;
	protected String splitter;

	// TODO Add the validation rule here
	public Parameter(String key, String type, String value) {
		super();
		this.key = key;
		this.type = type;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSplitter() {
		return splitter;
	}

	public void setSplitter(String splitter) {
		this.splitter = splitter;
	}

}
