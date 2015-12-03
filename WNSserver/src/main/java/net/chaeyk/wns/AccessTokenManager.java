package net.chaeyk.wns;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import lombok.Data;
import net.chaeyk.wns.dao.AccessTokenDao;
import net.chaeyk.wns.model.AccessToken;

@Component
public class AccessTokenManager {
	
	@Data
	public static class WnsJson {

		private String tokenType;
		private String accessToken;
		private int expiresIn;
		
	}

	@Autowired
	private AccessTokenDao accessTokenDao;

	public String getAccessToken(String appName) throws Exception {
		AccessToken accessToken = accessTokenDao.get(appName);
		if (accessToken == null)
			throw new Exception("unknown appName: " + appName);

		if (!accessToken.expired())
			return accessToken.getAccessToken();
		
		getAccessTokenFromWnsServer(accessToken);
		accessTokenDao.save(accessToken);
		return accessToken.getAccessToken();
	}

	private void getAccessTokenFromWnsServer(AccessToken accessToken) throws IOException
	{
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("grant_type", "client_credentials");
		params.put("client_id", accessToken.getClientId());
		params.put("client_secret", accessToken.getClientSecret());
		params.put("scope", "notify.windows.com");
		
		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String, String> param : params.entrySet()) {
			if (postData.length() != 0)
				postData.append('&');
			postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		URL url = new URL("https://login.live.com/accesstoken.srf");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));		
		con.setDoOutput(true);
		con.getOutputStream().write(postDataBytes);
		
		String json = Util.streamToString(con.getInputStream(), "UTF-8");
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(
			    PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
		WnsJson wnsJson = mapper.readValue(json, WnsJson.class);
		
		Calendar expire = Calendar.getInstance();
		expire.add(Calendar.SECOND, wnsJson.getExpiresIn());
		
		accessToken.setAccessToken(wnsJson.getAccessToken());
		accessToken.setAccessTokenExpire(expire.getTime());
	}

}
