package net.chaeyk.wns.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="access_token")
public class AccessToken {
	
	@Id
	private String appName;
	private String clientId;
	private String clientSecret;
	private String accessToken;
	private Date accessTokenExpire;
	
	public boolean expired() {
		return (accessTokenExpire == null || accessTokenExpire.compareTo(new Date()) <= 0);
	}
}
