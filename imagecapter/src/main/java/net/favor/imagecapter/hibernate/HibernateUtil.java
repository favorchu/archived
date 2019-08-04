package net.favor.imagecapter.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HibernateUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);

	private static final SessionFactory sessionFactory = initSessionFactory();
	private static final ThreadLocal<Session> session = new ThreadLocal<Session>();
	private static Configuration config;

	public static Configuration getConfig() {
		return config;
	}

	private static final SessionFactory initSessionFactory() {
		SessionFactory localFactory = null;
		config = null;

		try {
			config = new Configuration().configure("hibernate.cfg.xml");
			localFactory = config.buildSessionFactory();
		} catch (HibernateException e) {
			LOGGER.error("Hibernate Exception caught when reading hibernate configuration file.", e);
		}
		return localFactory;
	}

	public static Session currentSession() throws HibernateException {
		Session s = (Session) session.get();
		if (s == null) {
			s = sessionFactory.openSession();
			session.set(s);
		}

		return s;
	}

	public static void rollback() {
		Session s = (Session) session.get();
		if (s != null) {
			try {
				s.getTransaction().rollback();
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}

		}
	}

	public static void closeSession() throws HibernateException {
		Session s = (Session) session.get();
		session.set(null);
		if (s != null && s.isOpen()) {
			s.close();
		}
	}

}
