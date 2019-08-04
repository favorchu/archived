package net.favor.imagecapter.hibernate.dao;

import java.util.Date;
import java.util.List;

import net.favor.imagecapter.hibernate.reveng.dao.AbstractImageRecordDAO;
import net.favor.imagecapter.hibernate.reveng.pojo.ImageRecord;

public interface ImageRecordDAO extends AbstractImageRecordDAO {
	public long getTotalImageSize();

	public List<ImageRecord> getByImKeys(Long[] keys);

	public List<ImageRecord> getRecordByDay(Date day);
}
