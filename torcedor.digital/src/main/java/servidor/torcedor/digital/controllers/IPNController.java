package servidor.torcedor.digital.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/ipn")
public class IPNController {
	
	private static final Logger logger = LoggerFactory.getLogger(IPNController.class);
	
	@RequestMapping(value = "/notification", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String apiLogin(@RequestBody String data) {
		
		logger.info(data);
		return data;
		
	}

}
