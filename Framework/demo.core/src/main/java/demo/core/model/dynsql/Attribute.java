package demo.core.model.dynsql;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Attribute is the dynamic part of the SQL template
 * 
 * @author favorchu
 *
 */
@XStreamAlias("Attribute")
public class Attribute {

	/**
	 * Key of the Attribute
	 */
	private String key;
	/**
	 * Attribute SQL
	 */
	private String sql;

	public Attribute(String key, String sql) {
		super();
		this.key = key;
		this.sql = sql;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
}
