package servidor.torcedor.digital;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.sun.mail.util.SharedByteArrayOutputStream;
class StructTokken {
	String access_token;
	String token_type;
	String expires_in;
	
	
	
	
	public String getAccess_token() {
		return access_token;
	}




	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}




	public String getToken_type() {
		return token_type;
	}




	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}




	public String getExpires_in() {
		return expires_in;
	}




	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}




	@Override
	public String toString() {
		return "StructTokken [access_token=" + access_token + ", token_type=" + token_type + ", expires_in="
				+ expires_in + "]";
	}
	
	
}
public class SchedulerTest {
	
	public static void main(String[] args) {

		new Thread(() -> {
			test1 t = new test1();
			t.start();
//			test2 t2 = new test2();
//			t2.start();
//			test3 t3 = new test3();
//			t3.start();
		}).start();

	}

}

class test1 {

	final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(6);

	public void start() {

		scheduler.scheduleAtFixedRate(new Runnable() {

			@SuppressWarnings("resource")
			@Override
			public void run() {
				String short_tokken = "EAASpcKnn2RQBAEOnid6oZAZCg0Y7s7NZAHY5AV6YWdbWEZBBE8jVHX3T44hDs3NymmGjRGMxI4AijGFv8OIDEdOKYN0nUwTTCZClRnq2ZB9J2G4k5xZAFxaPfx4FlFTSWu9s6lSfHGUgVrXKVWqNhXjwOCrTATL1EYjc7OLEZCmnW0nWCFyMNZAMwYwDZBnxPDNH0ZD";
				String resurce = "https://graph.facebook.com/oauth/access_token?"
						+ "client_id=1312201258817812&"
						+ "client_secret=f01dfe25d356bab41cd707697548c8f0&"
						+ "grant_type=fb_exchange_token&"
						+ "fb_exchange_token="+short_tokken; 
				Gson json = new Gson();
				try {

					@SuppressWarnings("deprecation")
					HttpClient client = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(resurce);
					HttpResponse resp = client.execute(httpGet);
					//resp.getEntity().toString()
					//String result = json.toJson(resp.getEntity().getContent());
					
					
					
					//int statusCode = resp.getStatusLine().getStatusCode();
					String responseBody = EntityUtils.toString(resp.getEntity());
					StructTokken struct = json.fromJson(responseBody, StructTokken.class);
					
					System.out.println(struct);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 5, TimeUnit.MINUTES);

	}

}

class test2 {

	final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(6);

	public void start() {

		scheduler.scheduleAtFixedRate(new Runnable() {


			@Override
			public void run() {
				try {
					
					System.out.println("kkkk ...");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 1, TimeUnit.SECONDS);

	}

}

class test3 {

	final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(6);

	public void start() {

		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("CARACA!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 10, TimeUnit.SECONDS);

	}

}
