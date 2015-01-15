package com.openxc.ford.mHealth.demo.web;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.openxc.ford.mHealth.demo.AppLog;

public class WebService {

	private final String TAG = AppLog.getClassName();

	private static WebService sWebService = null;

	private WebService() {

	}

	public static WebService getInstance() {
		if (null == sWebService) {
			sWebService = new WebService();
		}

		return sWebService;
	}

	public String request(String url) {
		AppLog.enter(TAG, AppLog.getMethodName());
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		try {
			response = httpclient.execute(new HttpGet(url));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();

				AppLog.info(TAG, "Server response : " + responseString);
			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		AppLog.exit(TAG, AppLog.getMethodName());
		return responseString;
	}

	public String request(String url, JSONObject json) {
		AppLog.enter(TAG, AppLog.getMethodName());
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		String dataText = "";

		try {

			JSONArray postjson = new JSONArray();
			postjson.put(json);

			AppLog.info(TAG, "json array : " + json);

			StringEntity se = new StringEntity(json.toString());

			// 6. set httpPost Entity
			httppost.setEntity(se);

			// 7. Set some headers to inform server about the type of the
		
			httppost.setHeader("Content-type", "application/json");

			// Execute HTTP Post Request
			System.out.print(json);
			HttpResponse response = httpclient.execute(httppost);

			// for JSON:
			if (response != null) {
				InputStream is = response.getEntity().getContent();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				dataText = sb.toString();
			}

		} catch (ClientProtocolException e) {
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		AppLog.exit(TAG, AppLog.getMethodName());
		return dataText;
	}

	public String requestGet(String url) {
		AppLog.enter(TAG, AppLog.getMethodName());
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		String dataText = "";

		try {

			// 7. Set some headers to inform server about the type of the
		
			 httpGet.setHeader("Content-type", "application/json");

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httpGet);

			// for JSON:
			if (response != null) {
				InputStream is = response.getEntity().getContent();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				dataText = sb.toString();
			}

		} catch (ClientProtocolException e) {
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		AppLog.exit(TAG, AppLog.getMethodName());
		return dataText;
	}

}
