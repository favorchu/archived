package net.favor.imagecapter.mvc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.favor.imagecapter.hibernate.HibernateUtil;
import net.favor.imagecapter.hibernate.dao.ImageRecordDAO;
import net.favor.imagecapter.hibernate.dao.ImageRecordDAOImpl;
import net.favor.imagecapter.hibernate.reveng.pojo.ImageRecord;
import net.favor.imagecapter.hibernate.service.AlbumService;
import net.favor.imagecapter.hibernate.service.bean.AlbumDayBean;
import net.favor.imagecapter.mvc.controller.bean.AlbumDayDetail;
import net.favor.imagecapter.mvc.controller.bean.ImageItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/app/album")
public class AlbumController extends AbstractCartController {

	private static final Logger logger = LoggerFactory.getLogger(AlbumController.class);

	@RequestMapping(value = "/{album}/", method = RequestMethod.GET)
	public ModelAndView ablumDays(@PathVariable String album) {
		logger.info("ablumDays() album:{} ", album);
		ModelAndView modelAndView = new ModelAndView("album_day_pick");
		try {
			List<AlbumDayDetail> dayObjects = new ArrayList<AlbumDayDetail>();
			AlbumService service = new AlbumService(HibernateUtil.currentSession(), getUserProfile());
			List<AlbumDayBean> daybeans = service.getAlbumDayList(album);
			for (AlbumDayBean albumBean : daybeans) {
				AlbumDayDetail detail = new AlbumDayDetail();
				if (albumBean.getDay() == null) {
					detail.setDay("");
					detail.setDayToken("NO-DAY");
				} else {
					detail.setDay(SIMPLE_DAY_TOKEN.format(albumBean.getDay()));
					detail.setDayToken(SIMPLE_DAY_TOKEN.format(albumBean.getDay()));
				}
				detail.setThumbKey(albumBean.getThumbKey());
				dayObjects.add(detail);
			}

			modelAndView.addObject("days", dayObjects);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			HibernateUtil.closeSession();
		}

		return modelAndView;
	}

	@RequestMapping(value = "/{album}/{dateVar}/", method = RequestMethod.GET)
	public ModelAndView album(@PathVariable String album, @PathVariable String dateVar) {
		logger.info("album() album:{} dateVar:{}", album, dateVar);
		ModelAndView modelAndView = new ModelAndView("album");
		try {

			Date day = null;
			try {
				day = SIMPLE_DAY_TOKEN.parse(dateVar);
			} catch (Exception e) {
				;
			}

			ImageRecordDAO imageDao = new ImageRecordDAOImpl(getUserProfile(), HibernateUtil.currentSession());
			List<ImageRecord> imagePojos = imageDao.getRecordByDay(day);
			List<ImageItem> imageObjects = new ArrayList<ImageItem>();
			List<String> donwloadingList = downloadCart.getToDownloadImageKeys();

			for (ImageRecord record : imagePojos) {
				ImageItem item = new ImageItem();
				item.setImCaptureTime(record.getImCaptureTime());
				item.setImComment(record.getImComment());
				item.setImCreatedBy(record.getImCreatedBy());
				item.setImCreatedDate(record.getImCreatedDate());
				item.setImFileName(record.getImFileName());
				item.setImFileSize(record.getImFileSize());
				item.setImImageKey(record.getImImageKey());
				if (record.getImCameraMake() != null)
					item.setCameraMake("-" + record.getImCameraMake());

				// check if this image is selected
				if (donwloadingList.contains(String.valueOf(record.getImImageKey()))) {
					item.setSelectedClass("selected");
				}

				imageObjects.add(item);

			}

			modelAndView.addObject("images", imageObjects);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			HibernateUtil.closeSession();
		}

		return modelAndView;
	}
}
