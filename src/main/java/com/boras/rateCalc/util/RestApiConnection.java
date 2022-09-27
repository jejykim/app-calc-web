package com.boras.rateCalc.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import okhttp3.OkHttpClient;

public class RestApiConnection {
	
	private static final Logger logger = LoggerFactory.getLogger(RestApiConnection.class);
	
	private static final String HEADER_ACCEPT_JSON = "application/json";
	private static final String HEADER_ACCEPT_ALL = "*/*";
	private static final String HEADERJSON = "application/json; charset=utf-8";
	private static final String HEADERFORM = "multipart/form-data";
	private static final String USER_AGENT_VALUE = "Mozilla/5.0";
	private static final String AUTHORIZATION_PREFIX = "Bearer ";
	private static final String KO_KR = "ko-KR";
	
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String ACCEPT = "Accept";
	private static final String USER_AGENT = "User-Agent";
	private static final String ACCEPT_LANGUAGE = "Accept-Language";
	
	/**
	 * http connection 설정
	 * @param strUrl url
	 * @return 
	 * @throws Exception
	 */
	protected HttpURLConnection getHttpConnection(String strUrl, HttpServletRequest req, boolean bGet) {
		URL url = null;
		HttpURLConnection con = null;
		try {
			url = new URL(strUrl);
		} catch (MalformedURLException e) {
			logger.error("ERROR : MalformedURLException ]");
			logger.error(e.getMessage());
		}
		try {
			con = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			logger.error("ERROR : HttpURLConnection ]");
			logger.error(e.getMessage());
		}
		con.setRequestProperty(ACCEPT, HEADER_ACCEPT_ALL);
		con.setRequestProperty(CONTENT_TYPE, HEADER_ACCEPT_JSON);
		con.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);
		con.setReadTimeout(100000);
		con.setDoOutput(true);

		if (bGet) {
			try {
				con.setRequestMethod("GET");
			} catch (ProtocolException e) {
				logger.error("ERROR : ProtocolException ]");
				logger.error(e.getMessage());
			}
		} else {
			try {
				con.setRequestMethod("POST");
			} catch (ProtocolException e) {
				logger.error("ERROR : ProtocolException ]");
				logger.error(e.getMessage());
			}
			con.setDoOutput(true);
		}

		return con;
	}
	
	/**
	 * http connection 설정
	 * @param strUrl url
	 * @return 
	 * @throws Exception
	 */
	protected HttpsURLConnection getHttpsConnection(String strUrl, HttpServletRequest req, boolean bGet) {
		URL url;
		HttpsURLConnection con = null;
		try {
			url = new URL(strUrl);
			con = (HttpsURLConnection) url.openConnection();
			con.setRequestProperty(ACCEPT, HEADER_ACCEPT_ALL);
			con.setRequestProperty(CONTENT_TYPE, HEADER_ACCEPT_JSON);
			con.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);
			con.setReadTimeout(50000);

			if(bGet) {
				con.setRequestMethod("GET");
			} else {
				con.setRequestMethod("POST");
				con.setDoOutput(true);
			}

		} catch (Exception e) {
			logger.error("[ ERROR : getHttpConnection ]");
			logger.error(e.getMessage());
		}

		return con;
	}
	
	/**
	 * http 통신 (POST)
	 * @param URL - GW URL
	 * @param param - 요청 파라미터
	 * @return String - 응답 response
	 * @exception Exception
	 */
	public String requestHttpPOST(String strUrl, JSONObject reqParam, Map<String, Object> headers, HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		HttpURLConnection con = null;
		int responseCode = -1;
		BufferedReader rd = null;
		
		try {
			trustAllHttpsCertificates();

			con = this.getHttpConnection(strUrl, req, false);
			
			if(headers != null) {
				Set<String> keys = headers.keySet(); 
				for(String key : keys) {
					String item = (String) headers.get(key);
					con.setRequestProperty(key, item);
					//logger.info("[header : "+key+"="+item+"]");
				}
			}

			if(reqParam != null) {
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.write(reqParam.toString().getBytes());
				wr.flush();
				wr.close();
				
				logger.info("[reqParam : "+reqParam.toString()+"]");
			}
			
			responseCode = con.getResponseCode();
			
		//	logger.info("[responseCode : "+responseCode+"]");
			
			// 8. 전달받은 데이터를 BufferedReader 객체로 저장.
			
			try {
				if (con.getResponseCode() >= 200 && con.getResponseCode() <= 300) {
					rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
				} else {
					rd = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				}
			} catch (IOException e) {
				logger.error("BufferedReader Error");
				logger.error(e.getMessage());
			}
			// 9. 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
			String line;
			try {
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				logger.error("sb.append Error");
				logger.error(e.getMessage());
			}
			// 10. 객체 해제.
			try {
				rd.close();
			} catch (IOException e) {
				logger.error("rd.close() Error");
				logger.error(e.getMessage());
			}
			con.disconnect();
			// 11. 전달받은 데이터 확인.
			//logger.info("[Data: "+sb.toString()+"]");
			
			JSONObject jobj = new JSONObject();
			jobj.put("responseCode", responseCode);
			jobj.put("respParam", sb);
			
			return jobj.toString();
		} catch (Exception e) {
			if(rd != null) {
				try {
					rd.close();
				} catch (IOException e1) {
					logger.error("[ ERROR : rd.close ]");
					logger.error(e1.getMessage());
				}
			}
			
			logger.error("[ ERROR : requestHttpPOST ]");
			logger.error(e.getMessage());
			
			return sb.toString();
		}
	}
	
	/**
	 * http 통신 (GET)
	 * @param URL - GW URL
	 * @param param - 요청 파라미터
	 * @return String - 응답 response
	 * @exception Exception
	 */
	public String requestHttpGET(String strUrl,Map<String, Object> headers, HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		HttpURLConnection con = null;
		int responseCode = -1;
		BufferedReader rd = null;
		
		try {
			trustAllHttpsCertificates();
			con = this.getHttpConnection(strUrl, req, true);
			
			if(headers != null) {
				Set<String> keys = headers.keySet(); 
				for(String key : keys) {
					String item = (String) headers.get(key);
					con.setRequestProperty(key, item);
					con.addRequestProperty("Content-Type", "application/json");
					con.addRequestProperty("Accept", "application/json");
					//logger.info("[header : "+key+"="+item+"]");
				}
			}
			
			responseCode = con.getResponseCode();
			
			//logger.info("[responseCode : "+responseCode+"]");
			
			try {
				if (con.getResponseCode() >= 200 && con.getResponseCode() <= 300) {
					rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
				} else {
					rd = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				}
			} catch (IOException e) {
				logger.error("BufferedReader Error");
				logger.error(e.getMessage());
			}
			// 9. 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
			String line;
			try {
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				logger.error("sb.append Error");
				logger.error(e.getMessage());
			}
			// 10. 객체 해제.
			try {
				rd.close();
			} catch (IOException e) {
				logger.error("rd.close Error");
				logger.error(e.getMessage());
			}
			con.disconnect();
			// 11. 전달받은 데이터 확인.
			//logger.info("[sb.toString() : "+sb.toString()+"]");
			
			JSONObject jobj = new JSONObject();
			jobj.put("responseCode", responseCode);
			jobj.put("respParam", sb);
			
			return jobj.toString();
		} catch (Exception e) {
			if(rd != null) {
				try {
					rd.close();
				} catch (IOException e1) {
					logger.error("[ ERROR : rd.close ]");
					logger.error(e1.getMessage());
				}
			}
			
			logger.error(e.getMessage());
			logger.error("[ ERROR : requestHttpGET ]");
			return sb.toString();
		}
		
	}
	
	/**
	 * http 통신 (DELETE)
	 * @param URL - GW URL
	 * @param param - 요청 파라미터
	 * @return String - 응답 response
	 * @exception Exception
	 */
	public String requestHttpDELETE(String strUrl,Map<String, Object> headers, HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		HttpURLConnection con = null;
		int responseCode = -1;
		BufferedReader rd = null;
		
		try {
			trustAllHttpsCertificates();
			
			URL url;
			try {
				url = new URL(strUrl);
				con = (HttpURLConnection) url.openConnection();
				con.setRequestProperty(ACCEPT, HEADER_ACCEPT_ALL);
				con.setRequestProperty(CONTENT_TYPE, HEADER_ACCEPT_JSON);
				con.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);
				con.setReadTimeout(50000);
				con.setDoOutput(true);
				con.setRequestMethod("DELETE");

			} catch (Exception e) {
				logger.error("[ ERROR : getHttpConnection ]");
				logger.error(e.getMessage());
			}
			
			if(headers != null) {
				Set<String> keys = headers.keySet(); 
				for(String key : keys) {
					String item = (String) headers.get(key);
					con.setRequestProperty(key, item);
					logger.info("[header : "+key+"="+item+"]");
				}
			}
			
			responseCode = con.getResponseCode();
			
			logger.info("[responseCode : "+responseCode+"]");
			
			try {
				if (con.getResponseCode() >= 200 && con.getResponseCode() <= 300) {
					rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
				} else {
					rd = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				}
			} catch (IOException e) {
				logger.error("BufferedReader Error");
				logger.error(e.getMessage());
			}
			// 9. 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
			String line;
			try {
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				logger.error("sb.append Error");
				logger.error(e.getMessage());
			}
			// 10. 객체 해제.
			try {
				rd.close();
			} catch (IOException e) {
				logger.error("rd.close Error");
				logger.error(e.getMessage());
			}
			con.disconnect();
			
			JSONObject jobj = new JSONObject();
			jobj.put("responseCode", responseCode);
			
			return jobj.toString();
		} catch (Exception e) {
			if(rd != null) {
				try {
					rd.close();
				} catch (IOException e1) {
					logger.error("[ ERROR : rd.close ]");
					logger.error(e1.getMessage());
				}
			}
			
			logger.error(e.getMessage());
			logger.error("[ ERROR : requestHttpGET ]");
			return sb.toString();
		}
		
	}
	
	/**
	 * https 통신 (POST)
	 * @param URL - GW URL
	 * @param param - 요청 파라미터
	 * @return String - 응답 response
	 * @exception Exception
	 */
	public String requestHttpsPOST(String strUrl, JSONObject reqParam, Map<String, Object> headers, HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		HttpURLConnection con = null;
		int responseCode = -1;
		BufferedReader rd = null;
		
		try {
			trustAllHttpsCertificates();

			con = this.getHttpsConnection(strUrl, req, false);
			
			if(headers != null) {
				Set<String> keys = headers.keySet(); 
				for(String key : keys) {
					String item = (String) headers.get(key);
					con.setRequestProperty(key, item);
					//logger.info("[header : "+key+"="+item+"]");
				}
			}

			if(reqParam != null) {
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.write(reqParam.toString().getBytes());
				wr.flush();
				wr.close();
				
				//logger.info("[reqParam : "+reqParam.toString()+"]");
			}
			
			responseCode = con.getResponseCode();
			
		//	logger.info("[responseCode : "+responseCode+"]");
			
			// 8. 전달받은 데이터를 BufferedReader 객체로 저장.
			
			try {
				if (con.getResponseCode() >= 200 && con.getResponseCode() <= 300) {
					rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
				} else {
					rd = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				}
			} catch (IOException e) {
				logger.error("BufferedReader Error");
				logger.error(e.getMessage());
			}
			// 9. 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
			String line;
			try {
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				logger.error("sb.append Error");
				logger.error(e.getMessage());
			}
			// 10. 객체 해제.
			try {
				rd.close();
			} catch (IOException e) {
				logger.error("rd.close Error");
				logger.error(e.getMessage());
			}
			con.disconnect();
			// 11. 전달받은 데이터 확인.
			logger.info("[Data: "+sb.toString()+"]");
			
			JSONObject jobj = new JSONObject();
			jobj.put("responseCode", responseCode);
			jobj.put("respParam", sb);
			
			return jobj.toString();
		} catch (Exception e) {
			if(rd != null) {
				try {
					rd.close();
				} catch (IOException e1) {
					logger.error("[ ERROR : rd.close ]");
					logger.error(e1.getMessage());
				}
			}
			
			logger.error("[ ERROR : requestHttpsPOST ]");
			logger.error(e.getMessage());
			
			return sb.toString();
		}
	}
	
	/**
	 * https 통신 (GET)
	 * @param URL - GW URL
	 * @param param - 요청 파라미터
	 * @return String - 응답 response
	 * @exception Exception
	 */
	public String requestHttpsGET(String strUrl, Map<String, Object> headers, HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		HttpURLConnection con = null;
		int responseCode = -1;
		BufferedReader rd = null;
		
		try {
			trustAllHttpsCertificates();
			con = this.getHttpsConnection(strUrl, req, true);
			
			if(headers != null) {
				Set<String> keys = headers.keySet(); 
				for(String key : keys) {
					String item = (String) headers.get(key);
					con.setRequestProperty(key, item);
					//logger.info("[header : "+key+"="+item+"]");
				}
			}
			
			responseCode = con.getResponseCode();
			
			//logger.info("[responseCode : "+responseCode+"]");
			
			try {
				if (con.getResponseCode() >= 200 && con.getResponseCode() <= 300) {
					rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
				} else {
					rd = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				}
			} catch (IOException e) {
				logger.error("BufferedReader Error");
				logger.error(e.getMessage());
			}
			// 9. 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
			String line;
			try {
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				logger.error("sb.append Error");
				logger.error(e.getMessage());
			}
			// 10. 객체 해제.
			try {
				rd.close();
			} catch (IOException e) {
				logger.error("rd.close Error");
				logger.error(e.getMessage());
			}
			con.disconnect();
			// 11. 전달받은 데이터 확인.
			//logger.info("[sb.toString() : "+sb.toString()+"]");
			
			JSONObject jobj = new JSONObject();
			jobj.put("responseCode", responseCode);
			jobj.put("respParam", sb);
			
			return jobj.toString();
		} catch (Exception e) {
			if(rd != null) {
				try {
					rd.close();
				} catch (IOException e1) {
					logger.error("[ ERROR : rd.close ]");
					logger.error(e1.getMessage());
				}
			}
			
			logger.error(e.getMessage());
			logger.error("[ ERROR : requestHttpsGET ]");
			return sb.toString();
		}
		
	}
	
	public static void setHeaderAccept(HttpURLConnection con, String value) {
		con.setRequestProperty(ACCEPT, value);
	}
	
	public static void setHeaderContentType(HttpURLConnection con, String value) {
		con.setRequestProperty(CONTENT_TYPE, value);
	}

	private void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
 
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
 
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
     }
	
	public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    }).build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
}