package servidor.torcedor.digital.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import servidor.torcedor.digital.DAO.FaturamentoDAO;
import servidor.torcedor.digital.DAO.EnderecoDAO;
import servidor.torcedor.digital.models.Faturamento;
import servidor.torcedor.digital.models.Endereco;
import servidor.torcedor.digital.models.ResponseNotification;

@Controller
@RequestMapping("/api/ipn")
public class IPNController {
	@Autowired
	private EnderecoDAO enderecoDAO;
	
	@Autowired
	private FaturamentoDAO cartaFaturaDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(IPNController.class);
	
	@RequestMapping(value = "/notification", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String apiLogin(@ModelAttribute("ResponseNotification") ResponseNotification response) throws Exception {
		Gson json = new Gson();
		logger.info(json.toJson(response));
		
		salvarOuAtualizarEndereco(response);
		novoFaturamento(response);
		
		return json.toJson(response);
		
	}

	private Faturamento novoFaturamento(ResponseNotification response) throws Exception {
		Faturamento cartaFatura =  cartaFaturaDAO.salvarOuAtualizarFaturamento(response);
		return cartaFatura;
		
	}

	private Endereco salvarOuAtualizarEndereco(ResponseNotification response) {
		Endereco endereco = enderecoDAO.salvarOuAtualizarEndereco(response);
		return endereco;
		
	}

}