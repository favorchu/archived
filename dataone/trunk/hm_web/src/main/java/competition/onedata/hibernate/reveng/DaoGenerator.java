package competition.onedata.hibernate.reveng;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.hibernate.HibernateUtil;
import competition.onedata.hibernate.reveng.bean.UpdateAttributeBean;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class DaoGenerator {
	private static Logger logger = LoggerFactory.getLogger(DaoGenerator.class);

	private static final String SRC_DIR = "src/main/java/";
	private static final String OUTPUT_DIR = SRC_DIR + "competition/onedata/hibernate/reveng/dao/";
	private static final String TEMP_FOLDER = "dao_temp/";

	private static final int PREFIX_LENGTH = 2;
	private static final String[] HINTS_UPDATEDATE = new String[] { "UpdateDate", "UpdatedDate" };
	private static final String[] HINTS_UPDATEBY = new String[] { "UpdateBy", "UpdatedBy" };
	private static final String[] HINTS_CREATEDATE = new String[] { "CreateDate", "CreateDate" };
	private static final String[] HINTS_CREATEBY = new String[] { "CreateBy", "CreatedBy" };

	public static void main(String[] args) {
		logger.info("Start...");

		// Configuration

		try {

			// Prepare the directory
			{
				File outDir = new File(OUTPUT_DIR);
				if (!outDir.exists())
					outDir.mkdirs();
			}

			{
				File outDir = new File(TEMP_FOLDER);
				if (!outDir.exists())
					outDir.mkdirs();
			}

			// Set Directory for templates
			// cfg.setDirectoryForTemplateLoading(new File("templates/dao"));
			Configuration cfg = new Configuration();
			cfg.setClassForTemplateLoading(DaoGenerator.class, "/template/dao");
			// load template

			Template abstractDaoImplTemplate = cfg.getTemplate("AbstractDaoImpl.ftl");
			Template abstractDaoTemplate = cfg.getTemplate("AbstractDao.ftl");
			Template daoImplTemplate = cfg.getTemplate("DaoImpl.ftl");
			Template daoTemplate = cfg.getTemplate("Dao.ftl");

			TemplateService abstractDaoImplTemplateService = new TemplateService(abstractDaoImplTemplate);
			TemplateService abstractDaoTemplateService = new TemplateService(abstractDaoTemplate);
			TemplateService daoImplTemplateService = new TemplateService(daoImplTemplate);
			TemplateService daoTemplateService = new TemplateService(daoTemplate);

			for (Iterator it = HibernateUtil.getConfig().getClassMappings(); it.hasNext();) {
				PersistentClass pc = (PersistentClass) it.next();
				String className = getClassName(pc.getEntityName());
				String primaryKeyType = getPrimaryKeyType(pc);

				UpdateAttributeBean updateAttributes = getUpdateAttribute(pc);

				logger.debug("Entity : {}  primaryKeyType: {}", new String[] { className, primaryKeyType });

				Map<String, Object> input = new HashMap<String, Object>();
				input.put("classname", className);
				input.put("primarykey_type", primaryKeyType);
				input.put("updateAttributes", updateAttributes);

				abstractDaoImplTemplateService.process(input, new File(OUTPUT_DIR + "Abstract" + className
						+ "DAOImpl.java"));
				abstractDaoTemplateService.process(input, new File(OUTPUT_DIR + "Abstract" + className + "DAO.java"));
				daoTemplateService.process(input, new File(TEMP_FOLDER + className + "DAO.java"));
				daoImplTemplateService.process(input, new File(TEMP_FOLDER + className + "DAOImpl.java"));

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

		}
	}

	private static UpdateAttributeBean getUpdateAttribute(PersistentClass pc) {
		logger.debug("getUpdateAttribute()");
		Iterator columnItr = pc.getPropertyIterator();
		UpdateAttributeBean bean = new UpdateAttributeBean();
		Property column = null;
		while (columnItr.hasNext()) {
			column = (Property) columnItr.next();
			String columnFullName = column.getName();

			columnFullName = (columnFullName.charAt(0) + "").toUpperCase() + columnFullName.substring(1);
			String columnShortName = columnFullName.substring(PREFIX_LENGTH);
			if (contain(HINTS_CREATEBY, columnShortName))
				bean.setCreateByFieldName(columnFullName);
			else if (contain(HINTS_CREATEDATE, columnShortName))
				bean.setCreateDateFieldName(columnFullName);
			else if (contain(HINTS_UPDATEBY, columnShortName))
				bean.setUpdateByFieldName(columnFullName);
			else if (contain(HINTS_UPDATEDATE, columnShortName))
				bean.setUpdateDateFieldName(columnFullName);

		}

		if (StringUtils.isBlank(bean.getUpdateByFieldName()) && StringUtils.isBlank(bean.getUpdateDateFieldName())
				&& StringUtils.isBlank(bean.getCreateByFieldName())
				&& StringUtils.isBlank(bean.getCreateDateFieldName()))
			bean = null;
		return bean;
	}

	private static boolean contain(String[] list, String target) {
		for (String item : list)
			if (item.equalsIgnoreCase(target))
				return true;
		return false;
	}

	private static String getPrimaryKeyType(PersistentClass pc) {

		PrimaryKey primaryKey = pc.getTable().getPrimaryKey();
		if (primaryKey.getColumnSpan() > 1)
			return getClassName(pc.getEntityName()) + "Id";
		else
			return getClassName(primaryKey.getColumn(0).getValue().getType().getReturnedClass().getName());
	}

	private static String getClassName(String name) {
		return name.substring(name.lastIndexOf('.') + 1);
	}

}
