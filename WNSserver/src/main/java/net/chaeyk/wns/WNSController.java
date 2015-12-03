package net.chaeyk.wns;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.chaeyk.wns.dao.ChannelUriDao;
import net.chaeyk.wns.model.ChannelUri;
import net.chaeyk.wns.receipt.Receipt;
import net.chaeyk.wns.receipt.ReceiptValidator;

@Controller
public class WNSController {

	@Autowired
	private ChannelUriDao channelUriDao;
	
	@Autowired
	private AccessTokenManager accessTokenManager;
	
	@Autowired
	private ReceiptValidator receiptValidator;
	
	@RequestMapping("/putUri")
	public void putUri(@RequestParam String uri, @RequestParam String appName) throws Exception {
		ChannelUri channelUri = new ChannelUri();
		channelUri.setUri(uri);
		channelUri.setAppName(appName);
		channelUri.setUpdateDt(new Date());
		channelUriDao.save(channelUri);
	}
	
	@RequestMapping("/send")
	public void send(@RequestParam(required = false) String appName,
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String message) throws Exception {
		if (appName == null || type == null || message == null)
			return;
		
		System.out.println("sending message: " + message);
		
		String accessToken = accessTokenManager.getAccessToken(appName);
		String postBody = "";
		if (type.equals("wns/toast")) {
			postBody = "<toast launch=''><visual lang='ko-KR'><binding template='ToastImageAndText01'><text id='1'>" + message + "</text></binding></visual></toast>";
		} else if (type.equals("wns/raw")) {
			postBody = message;
		}
		byte[] postDataBytes = postBody.getBytes("UTF-8");
		
		List<ChannelUri> list = channelUriDao.listByAppName(appName);
		for (ChannelUri channelUri : list) {
			try {
				URL url = new URL(channelUri.getUri());
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
				con.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));		
				con.setRequestProperty("X-WNS-Type", type);
				con.setRequestProperty("Authorization", "Bearer " + accessToken);
				con.setDoOutput(true);
				con.getOutputStream().write(postDataBytes);
				
				String returnedBody = Util.streamToString(con.getInputStream(), "UTF-8");
				System.out.println(returnedBody);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
	}
	
	@RequestMapping("/validateReceipt")
	public void validateReceipt(@RequestParam String receiptXml) throws Exception {
		Receipt receipt = receiptValidator.validate(receiptXml);
		System.out.println(receipt.toString());
	}
}
