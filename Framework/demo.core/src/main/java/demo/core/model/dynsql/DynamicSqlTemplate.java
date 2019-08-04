package demo.core.model.dynsql;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("DynamicSqlTemplate")
public class DynamicSqlTemplate {

	private String templateId;
	private String mainBody;
	@XStreamAlias("Attributes")
	private List<Attribute> attributes = new ArrayList<Attribute>();
	@XStreamAlias("Parameters")
	private List<Parameter> parameters = new ArrayList<Parameter>();;
	private boolean wrapPaging;
	private boolean showDebugInfo;

	public String getMainBody() {
		return mainBody;
	}

	public void setMainBody(String mainBody) {
		this.mainBody = mainBody;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public boolean getWrapPaging() {
		return wrapPaging;
	}

	public void setWrapPaging(boolean wrapPaging) {
		this.wrapPaging = wrapPaging;
	}

	public boolean getShowDebugInfo() {
		return showDebugInfo;
	}

	public void setShowDebugInfo(boolean showDebugInfo) {
		this.showDebugInfo = showDebugInfo;
	}

	public void addParameter(Parameter parameter) {
		parameters.add(parameter);
	}

	public void addAttribute(Attribute attribute) {
		attributes.add(attribute);
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

}
