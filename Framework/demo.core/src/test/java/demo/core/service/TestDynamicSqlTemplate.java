package demo.core.service;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import demo.core.TestBase;
import demo.core.config.DynamicSqlTemplateProvider;
import demo.core.dao.impl.DynamicQueryDaoImpl;
import demo.core.model.dynsql.DynamicSqlTemplate;

//@Transactional(transactionManager="txManager")
public class TestDynamicSqlTemplate extends TestBase {

	@Autowired
	private DynamicSqlTemplateProvider dynamicSqlTemplateProvider;

	@Autowired
	DynamicQueryDaoImpl dynamicQueryDao;

	@Test
	public void testLoadtemplate() {
		DynamicSqlTemplate template = dynamicSqlTemplateProvider.getDynamicSqlTemplate("hello.dynsql.xml");
		Assert.assertTrue("Load testing template", StringUtils.trimToEmpty(template.getMainBody()).length() > 0);
	}

	@Test
	public void testFormatSQLtemplate() {
		String sql = dynamicQueryDao.getTransformedSql(Arrays.asList(new String[] { "ATT.-_/01" }),
				dynamicSqlTemplateProvider.getDynamicSqlTemplate("002.mapping_test.dynsql.xml"));

		Assert.assertFalse(StringUtils.contains(sql, "@@"));
		Assert.assertFalse(StringUtils.contains(sql, "##"));
	}

}
