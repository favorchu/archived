package net.favor.imagecapter.config;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConfigurationManager {

	private static final String TAG_PASSWORD = "password";
	private static final String TAG_MAX_VOL = "max_vol";
	private static final String TAG_CONTENT_DIR = "content_folder";
	private static Logger log = LoggerFactory.getLogger(ConfigurationManager.class);
	private static final String CONFIG_FILE = "config.xml";

	private static final String TAG_APP_NAME = "app_name";
	private static final String TAG_APP_VERSION = "app_version";
	private static final String TAG_LANDING_URL = "landing_url";

	private static final String TAG_USER = "user";

	private static String appName;
	private static String appVersion;
	private static String appLandingUrl;
	private static String contentDirectory;
	private static String password;
	private static long maxContentSize = 1024 * 1024 * 100;

	public static String getAppName() {
		return appName;
	}

	public static String getAppVersion() {
		return appVersion;
	}

	public static String getAppLandingUrl() {
		return appLandingUrl;
	}

	static {
		InputStream is = null;
		try {
			// Prepare XML file
			is = getConfigFileInputStream();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();

			// Prepare basic config
			prepareBasicConfig(doc);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	private static void prepareBasicConfig(Document doc) {
		appName = getTagValue(TAG_APP_NAME, doc);
		appVersion = getTagValue(TAG_APP_VERSION, doc);
		appLandingUrl = getTagValue(TAG_LANDING_URL, doc);
		password = getTagValue(TAG_PASSWORD, doc);
		contentDirectory = getTagValue(TAG_CONTENT_DIR, doc);
		maxContentSize = Long.parseLong(getTagValue(TAG_MAX_VOL, doc));

	}

	public static String getPassword() {
		return password;
	}

	public static long getMaxContentSize() {
		return maxContentSize;
	}

	public static String getContentDirectory() {
		return contentDirectory;
	}

	private static InputStream getConfigFileInputStream() {
		InputStream is;
		is = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);

		if (is == null) {
			log.info("templates.xml not found. find in package.");
			is = ConfigurationManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
		}
		return is;
	}

	private static String getTagValue(String sTag, Document doc) {
		return doc.getElementsByTagName(sTag).item(0).getTextContent();
	}

	private static String getTagValue(String sTag, Node node) {
		return getTagValue(sTag, (Element) node);
	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);

		return nValue == null ? null : nValue.getNodeValue();
	}

}
