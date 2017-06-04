package servidor.torcedor.digital;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate.HttpClientOption;
import org.springframework.format.datetime.joda.JodaTimeContext;
import org.springframework.format.datetime.joda.LocalDateParser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import servidor.torcedor.digital.models.Calendario;
import servidor.torcedor.digital.repositories.CalendarioRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootTest
public class CalendarioTest {
	
	private final String USER_AGENT = "Mozilla/5.0";
	
	@Autowired
	private CalendarioRepository repo;
	
	
	@Test
	public void test_update_data() throws Exception {
		Calendario calendario = repo.findOne(1L);
		
		//.160645-03
		Date data = DateUtils.addDays(new Date(), 20);
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pt", "br"));
		String strData = sd.format(data);
		
		//Date date = new Date(sd.parse(strData).getTime());
		//NTPclient();
		
		//sendGet();
		
		
		
		
		
		
		Timestamp tmp = Timestamp.valueOf(strData);
		
		
		System.out.println(tmp);
		
//		
		
		/*
		
		Set<String> allZones = ZoneId.getAvailableZoneIds();
		LocalDateTime dt = LocalDateTime.now();

		// Create a List using the set of zones and sort it.
		List<String> zoneList = new ArrayList<String>(allZones);
		Collections.sort(zoneList);

		

		for (String s : zoneList) {
		    ZoneId zone = ZoneId.of(s);
		    ZonedDateTime zdt = dt.atZone(zone);
		    ZoneOffset offset = zdt.getOffset();
		    int secondsOfHour = offset.getTotalSeconds() % (60 * 60);
		    String out = String.format("%35s %10s%n", zone, offset);

		    // Write only time zones that do not have a whole hour offset
		    // to standard out.
		    if (secondsOfHour != 0) {
		        System.out.printf(out);
		    }
		  
		}
*/
//		
//		calendario.setDataInicio(v);
//		
//		
//		repo.save(calendario);
//		
//		Calendario calendario2 = repo.findOne(1L);
//		
//		System.out.println(calendario2);
		
		
	}
	
	// HTTP GET request
		private void sendGet() throws Exception {

			String url = "http://www.horariodebrasilia.org/";

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			System.out.println(response.toString().substring(0, 500));

		}
	
	@Test
	public void test_repo_calendario() {
		
		List<Calendario> list = repo.findAll();
		
		System.out.println(list);
		
		for (Calendario calendario : list) {
			System.out.println(calendario.getTimeCasa());
			System.out.println(calendario.getTimeVisitante());
			System.out.println(calendario.getDataInicio());
			System.out.println(calendario.getDataFim());
		}
		
		
	}
	
	private void NTPclient() throws IOException {

		NTPUDPClient client = new NTPUDPClient();
		client.open();
		InetAddress hostAddr = InetAddress.getByName("a.st1.ntp.br");
		TimeInfo info = client.getTime(hostAddr);
		info.computeDetails(); // compute offset/delay if not already done
		Long offsetValue = info.getOffset();
		Long delayValue = info.getDelay();

		Date date = new Date(info.getReturnTime());
		System.err.println(date);

		String delay = (delayValue == null) ? "N/A" : delayValue.toString();
		String offset = (offsetValue == null) ? "N/A" : offsetValue.toString();

		System.out.println(" Roundtrip delay(ms)=" + delay + ", clock offset(ms)=" + offset); // offset
																								// in
																								// ms
		client.close();

	}
	
	@Test
	public void test_repo_calendario_json() {
		
		List<Calendario> list = repo.findAll();
		
		Date data = DateUtils.addDays(new Date(), 20);
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("pt", "br"));
		String strData = sd.format(data);
		
		System.err.println(strData);
		
		Gson gson = new GsonBuilder()
				   .setDateFormat("dd/MM/YYYY HH:mm")
				   .create();
		
		System.out.println(gson.toJson(list));
		
		
	}

}
