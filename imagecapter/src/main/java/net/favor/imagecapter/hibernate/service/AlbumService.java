package net.favor.imagecapter.hibernate.service;

import java.util.List;

import net.favor.imagecapter.hibernate.service.bean.AlbumDayBean;
import net.favor.imagecapter.session.UserProfile;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

public class AlbumService extends AbstractService {

	public AlbumService(Session session, UserProfile user) {
		super(session, user);
	}

	@SuppressWarnings("unchecked")
	public List<AlbumDayBean> getAlbumDayList(String ablum) {
		Query query = getSession().createQuery(
				"SELECT imCaptureDate as day, min(imImageKey) as thumbKey FROM ImageRecord   group by imCaptureDate");
		query.setResultTransformer(Transformers.aliasToBean(AlbumDayBean.class));

		return query.list();
	}

	// public static void main(String[] args) throws NoEnoughVolumeException {
	//
	// UserProfile user = new UserProfile();
	// user.setValid(true);
	// try {
	//
	// AlbumService serv = new AlbumService(HibernateUtil.currentSession(),
	// user);
	// List<AlbumDayBean> list = serv.getAlbumDayList("");
	// System.out.println(list.size());
	// System.out.println(list.get(0).getThumbKey());
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// HibernateUtil.closeSession();
	// }
	//
	// }
}
