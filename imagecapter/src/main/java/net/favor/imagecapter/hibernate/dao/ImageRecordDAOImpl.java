package net.favor.imagecapter.hibernate.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.favor.imagecapter.hibernate.reveng.dao.AbstractImageRecordDAOImpl;
import net.favor.imagecapter.hibernate.reveng.pojo.ImageRecord;
import net.favor.imagecapter.session.UserProfile;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.genericdao.search.Search;

public class ImageRecordDAOImpl extends AbstractImageRecordDAOImpl implements ImageRecordDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageRecordDAOImpl.class);

	public ImageRecordDAOImpl(UserProfile user, Session session) {
		super(user, session);
	}

	public List<ImageRecord> getRecordByDay(Date day) {
		Search search = new Search(ImageRecord.class);

		if (day != null) {
			search.addFilterEqual("imCaptureDate", day);
		} else {
			search.addFilterEmpty("imCaptureDate");
		}
		search.addSort("imCaptureTime", false);
		return search(search);

	}

	public long getTotalImageSize() {
		Query query = getSession().createQuery("select sum(imFileSize) from ImageRecord");
		List list = query.list();
		if (list == null || list.size() == 0 || list.get(0) == null)
			return 0;
		return (Long) list.get(0);
	}

	public List<ImageRecord> getByImKeys(Long[] keys) {
		List<ImageRecord> imageObjs = new ArrayList<ImageRecord>();

		if (keys == null || keys.length == 0)
			return imageObjs;

		List<Long> tempList = null;
		int count = 0;
		for (Long key : keys) {
			if (tempList == null) {
				tempList = new ArrayList<Long>();
				count = 0;
			}
			if (key != null)
				tempList.add(key);
			count++;
			if (count >= 300) {
				imageObjs.addAll(getBatchByImKeys(tempList.toArray(new Long[imageObjs.size()])));
				tempList = null;
			}
		}
		if (tempList != null)
			imageObjs.addAll(getBatchByImKeys(tempList.toArray(new Long[imageObjs.size()])));
		return imageObjs;
	}

	private List<ImageRecord> getBatchByImKeys(Long[] keys) {

		if (keys == null || keys.length == 0)
			return new ArrayList<ImageRecord>();

		Search search = new Search(ImageRecord.class);
		search.addFilterIn("imImageKey", keys);
		return search(search);
	}
}
