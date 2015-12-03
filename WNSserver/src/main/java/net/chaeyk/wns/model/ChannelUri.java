package net.chaeyk.wns.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="channel_uri")
public class ChannelUri {

	@Id
	private String uri;
	private String appName;
	private Date updateDt;
	
}
