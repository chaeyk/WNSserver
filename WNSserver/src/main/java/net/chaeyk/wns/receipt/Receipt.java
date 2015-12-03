package net.chaeyk.wns.receipt;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Receipt {

	@XmlAttribute(name="Version")
	@Getter
	private String version;

	@XmlAttribute(name="ReceiptDate")
	@Getter
	private Date receiptDate;
	
	@XmlAttribute(name="ReceiptDeviceId")
	@Getter
	private String receiptDeviceId;

	@XmlAttribute(name="PublisherDeviceId")
	@Getter
	private String publisherDeviceId;

	
	@XmlElement(name="AppReceipt")
	@Getter
	private AppReceipt appReceipt;
	
	@XmlElement(name="ProductReceipt")
	@Getter
	private List<ProductReceipt> productReceipt;
	
	
	@Getter @Setter
	private boolean isTest;
	
}
