package competition.onedata.hibernate.reveng;

import java.util.Iterator;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.hibernate.HibernateUtil;

public class HibernateEntityService {

	private static Logger logger = LoggerFactory.getLogger(HibernateEntityService.class);

	public HibernateEntityService() {
		Configuration config = null;
		try {
			for (Iterator it = HibernateUtil.getConfig().getClassMappings(); it.hasNext();) {
				PersistentClass pc = (PersistentClass) it.next();
				logger.debug(pc.getEntityName() + "\t" + pc.getTable().getName());

				logger.debug("primary key " + pc.getTable().getPrimaryKey());

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}
}
