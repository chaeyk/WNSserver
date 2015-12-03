package net.chaeyk.wns.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.chaeyk.wns.model.ChannelUri;

@Component
public class ChannelUriDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public List<ChannelUri> list() {
		Session session = sessionFactory.openSession();
		
		@SuppressWarnings("unchecked")
		List<ChannelUri> list = session.createQuery("from ChannelUri").list();
		
		session.close();
		return list;
	}
	
	public List<ChannelUri> listByAppName(String appName) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from ChannelUri where appName = :appName");
		query.setString("appName", appName);
		
		@SuppressWarnings("unchecked")
		List<ChannelUri> list = query.list();
		
		session.close();
		return list;
	}
	
	public void save(ChannelUri ChannelUri) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.saveOrUpdate(ChannelUri);
		session.getTransaction().commit();
		session.close();
	}

}
