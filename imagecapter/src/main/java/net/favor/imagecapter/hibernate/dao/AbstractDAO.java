package net.favor.imagecapter.hibernate.dao;

import java.io.Serializable;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;


public interface AbstractDAO <T, ID extends Serializable> extends GenericDAO<T, ID>  {

}
