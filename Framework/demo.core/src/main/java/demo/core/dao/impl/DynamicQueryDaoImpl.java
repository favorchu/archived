package demo.core.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import demo.core.ApplicationRuntimeExcepion;
import demo.core.ConfigRuntimeExcepion;
import demo.core.config.DynamicSqlTemplateProvider;
import demo.core.model.dynsql.Attribute;
import demo.core.model.dynsql.DynamicSqlTemplate;

@Repository
public class DynamicQueryDaoImpl {
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicQueryDaoImpl.class);

	/**
	 * Max. number of the nested SQL content depth levels
	 */
	private static final int MAX_NESTED_DEPTH = 5;
	private static final String ATT_NOTE_CONSTANT = "###";
	private static final String ATT_NOTE_DYNAMIC = "@@@";
	private static final String KEY_BODY = "[^\\s]{1,100}";

	private static final String CONS_ATTRIBUTE_REGEX = "(" + ATT_NOTE_CONSTANT + KEY_BODY + ATT_NOTE_CONSTANT + ")";
	private static final String DYNA_ATTRIBUTE_REGEX = "(" + ATT_NOTE_DYNAMIC + KEY_BODY + ATT_NOTE_DYNAMIC + ")";

	private static final Pattern CONSTANT_ATTRIBUTE_PATTERN = Pattern.compile(CONS_ATTRIBUTE_REGEX);
//	private static final Pattern DYNAMIC_ATTRIBUTE_PATTERN = Pattern.compile(DYNA_ATTRIBUTE_REGEX);

	@Autowired
	public DynamicSqlTemplateProvider dynamicSqlTemplateProvider;

	public void executeDynamicQuery(String dynSqlTemplateId, Map<String, String> paramters, List<String> attributes) {

		DynamicSqlTemplate dynTemplate = dynamicSqlTemplateProvider.getDynamicSqlTemplate(dynSqlTemplateId);
		// Get the main SQL
		if (StringUtils.isBlank(dynTemplate.getMainBody()))
			throw new ConfigRuntimeExcepion(String.format("Blank SQL template: %s", dynSqlTemplateId));

		// Get the formmatted SQL
		String mainSql = getTransformedSql(attributes, dynTemplate);

		mainSql.length();

	}

	public String getTransformedSql(List<String> attributes, DynamicSqlTemplate dynTemplate) {
		String mainSql = dynTemplate.getMainBody();
		/** Replace collect all attributes **/
		// Replace dynamic Attributes

		for (String key : attributes) {
			boolean found = false;
			for (Attribute possibleAttribute : dynTemplate.getAttributes()) {
				if (StringUtils.equalsAnyIgnoreCase(StringUtils.trimToEmpty(possibleAttribute.getKey()),
						StringUtils.trimToEmpty(key))) {
					found = true;
					String locatorKey = StringUtils.trimToEmpty(possibleAttribute.getKey());
					// e.g. replace %%%KEY-01%%% to SQL
					mainSql = mainSql.replaceAll(ATT_NOTE_DYNAMIC + locatorKey + ATT_NOTE_DYNAMIC,
							possibleAttribute.getSql());
					break;
				}
			}

			if (!found) {
				throw new ApplicationRuntimeExcepion(
						String.format("SQL Template %s has no attribut: %s", dynTemplate.getTemplateId(), key));
			}
		}

		// Clean up no used attributes
		mainSql = mainSql.replaceAll(DYNA_ATTRIBUTE_REGEX, "");

		// Replace constants attributes
		int nestedDepth = 0;
		boolean found = false;
		do {
			found = false;
			// Checking the attributes
			Matcher matcher = CONSTANT_ATTRIBUTE_PATTERN.matcher(mainSql);
			List<String> matched = new ArrayList<String>();
			while (matcher.find()) {
				found = true;
				matched.add(matcher.group());
			}
			// Distinct the list
			matched = new ArrayList<String>(new HashSet<String>(matched));

			for (String m : matched) {
				// Replace one by one
				String sql = getSqlByKey(dynTemplate, m);
				mainSql = mainSql.replaceAll(m, sql);
			}
		}
		// Continue if found the group , and still within the max depth
		while (++nestedDepth < MAX_NESTED_DEPTH && found);

		// Return
		return mainSql;
	}

	private String getSqlByKey(DynamicSqlTemplate dynTemplate, String key) {
		key = extractKey(key);

		for (Attribute attribute : dynTemplate.getAttributes())
			if (StringUtils.equalsAnyIgnoreCase(StringUtils.trimToEmpty(attribute.getKey()), key))
				return attribute.getSql();

		throw new ApplicationRuntimeExcepion(
				String.format("SQL Template %s has no attribut: %s", dynTemplate.getTemplateId(), key));

	}

	private String extractKey(String key) {
		key = StringUtils.replaceEach(key, new String[] { ATT_NOTE_CONSTANT, ATT_NOTE_DYNAMIC },
				new String[] { "", "" });
		key = StringUtils.trimToEmpty(key);
		return key;
	}

}
