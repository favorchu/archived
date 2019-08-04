package demo.core.config;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import demo.core.ConfigRuntimeExcepion;
import demo.core.model.dynsql.Attribute;
import demo.core.model.dynsql.DynamicSqlTemplate;
import demo.core.model.dynsql.Parameter;

@Component
public class DynamicSqlTemplateProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicSqlTemplateProvider.class);

	/**
	 * Map to contain all loaded template
	 */
	protected Map<String, DynamicSqlTemplate> dynSqlTemplates = new ConcurrentHashMap<String, DynamicSqlTemplate>();

	/**
	 * XML convertor
	 */
	protected XStream xStream;

	public DynamicSqlTemplateProvider() {
		xStream = getXStream();
	}

	/**
	 * Clean all the loaded template
	 */
	public void flushTemplate() {
		dynSqlTemplates.clear();
	}

	/**
	 * Get the template by id (file name) with lazzy init
	 * 
	 * @param dynSqlTemplateId
	 * @return
	 */
	public DynamicSqlTemplate getDynamicSqlTemplate(String dynSqlTemplateId) {

		// TODO add more validation

		// Load the template from hash map
		DynamicSqlTemplate template = dynSqlTemplates.get(dynSqlTemplateId);
		// Found the template
		if (template != null)
			return template;

		// Template not inited
		try {
			// Load the template
			template = load(dynSqlTemplateId);
			// Gave exception return null
			template.getMainBody();
			// Save to map
			dynSqlTemplates.put(dynSqlTemplateId, template);
		} catch (IOException e) {
			ConfigRuntimeExcepion ioe = new ConfigRuntimeExcepion(
					String.format("Can not init DynamicSqlTemplate: %", dynSqlTemplateId));
			ioe.initCause(e);
			throw ioe;
		}
		return template;
	}

	// FIXME Favor, switch to dynamic source
	protected DynamicSqlTemplate load(String dynSqlTemplateId) throws IOException {
		File file = ResourceUtils.getFile("classpath:sqltemplate/" + dynSqlTemplateId);
		DynamicSqlTemplate template = (DynamicSqlTemplate) xStream
				.fromXML(new String(FileUtils.readFileToByteArray(file)));

		if (StringUtils.isBlank(template.getTemplateId()))
			template.setTemplateId(dynSqlTemplateId);
		return template;
	}

	protected XStream getXStream() {
		XStream xstream = new XStream(new DomDriver());

		Class<?>[] classes = new Class[] { DynamicSqlTemplate.class, Attribute.class, Parameter.class };
		XStream.setupDefaultSecurity(xstream);
		xstream.allowTypes(classes);

		xstream.ignoreUnknownElements();
		xstream.processAnnotations(DynamicSqlTemplate.class);

		return xstream;
	}

}
