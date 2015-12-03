package net.chaeyk.wns.receipt;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

import lombok.Getter;
import lombok.ToString;

@ToString
public class ProductReceipt {

	@XmlAttribute(name="Id")
	@Getter
	private String id;

	@XmlAttribute(name="AppId")
	@Getter
	private String appId;

	@XmlAttribute(name="ProductId")
	@Getter
	private String ProductId;

	@XmlAttribute(name="PurchaseDate")
	@Getter
	private Date purchaseDate;

	@XmlAttribute(name="ProductType")
	@Getter
	private String productType;

}
