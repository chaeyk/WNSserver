package net.chaeyk.wns.receipt;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.Provider;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import net.chaeyk.wns.Util;

@Component
public class ReceiptValidator {
	
	// 이거는 매번하면 안되고 캐싱해야 한다.
	private String getCertString(String certId) throws IOException {
		URL url = new URL("https://go.microsoft.com/fwlink/?LinkId=246509&cid=" + certId);
		URLConnection con = url.openConnection();
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		return Util.streamToString(in, encoding);
	}
	
	public Receipt validate(String xml) throws Exception {

		// Parser XML
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().parse(Util.stringToStream(xml, "iso-8859-1"));

		// XML DOM to Receipt class instance
		Element varElement = doc.getDocumentElement();
		JAXBContext context = JAXBContext.newInstance(Receipt.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		JAXBElement<Receipt> loader = unmarshaller.unmarshal(varElement, Receipt.class);
		Receipt receipt = loader.getValue();
		
		// Validate XML signature
		NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
		if (nl.getLength() != 0) {
			String base64CertString = getCertString(doc.getDocumentElement().getAttribute("CertificateId"));
			CertificateFactory f = CertificateFactory.getInstance("X.509");
			X509Certificate certificate = (X509Certificate) f.generateCertificate(Util.stringToStream(base64CertString, "iso-8859-1"));

			certificate.checkValidity(); // Validates OK!

			PublicKey pk = certificate.getPublicKey();          

			// document containing the XMLSignature
			DOMValidateContext valContext = new DOMValidateContext(pk, nl.item(0));

			String providerName = System.getProperty("jsr105Provider", "org.jcp.xml.dsig.internal.dom.XMLDSigRI");
			XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM", (Provider) Class.forName(providerName).newInstance());

			XMLSignature signature = fac.unmarshalXMLSignature(valContext);
			if (!signature.validate(valContext)) {
				throw new Exception("receipt validation failed: " + xml);
			}
			receipt.setTest(false);
		} else {
			// has no signature
			receipt.setTest(true);
		}

		return receipt;
	}
}
