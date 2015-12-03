package net.chaeyk.wns.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.chaeyk.wns.model.AccessToken;

@Component
public class AccessTokenDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public List<AccessToken> list() {
		Session session = sessionFactory.openSession();
		
		@SuppressWarnings("unchecked")
		List<AccessToken> list = (List<AccessToken>) session.createQuery("from AccessToken").list();
		
		session.close();
		return list;
	}
	
	public AccessToken get(String appName) {
		Session session = sessionFactory.openSession();
		AccessToken accessToken = (AccessToken) session.get(AccessToken.class, appName);
		session.close();
		return accessToken;
	}
	
	public void save(AccessToken accessToken) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.saveOrUpdate(accessToken);
		session.getTransaction().commit();
		session.close();
	}

}
