package servidor.torcedor.digital;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import servidor.torcedor.digital.utils.SenderMailService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestVelocityTemplateForEmail {

	@Autowired
	SenderMailService senderMailService;
	
	@Autowired
	ComponentVelocityTemplate componentVelocityTemplate;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void test_email_com_template() throws Exception {
		/*
		 * first, get and initialize an engine
		 */
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();

		/*
		 * organize our data
		 */
		ArrayList list = new ArrayList();
		Map map = new HashMap();

		map.put("name", "Cow");
		map.put("price", "$100.00");
		list.add(map);

		map = new HashMap();
		map.put("name", "Eagle");
		map.put("price", "$59.99");
		list.add(map);

		map = new HashMap();
		map.put("name", "Shark");
		map.put("price", "$3.99");
		list.add(map);

		/*
		 * add that list to a VelocityContext
		 */
		VelocityContext context = new VelocityContext();
		context.put("petList", list);

		/*
		 * get the Template
		 */
		Template t = ve.getTemplate("/static/templates/email_tmpl.vm");

		/*
		 * now render the template into a Writer, here a StringWriter
		 */
		StringWriter writer = new StringWriter();
		t.merge(context, writer);

		/*
		 * use the output in the body of your emails
		 */
		senderMailService.sendHtmlTemplate("cleiton2281@gmail.com", "Teste Com template", writer.toString());

	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void test__() throws Exception {
		
		ArrayList list = new ArrayList();
			
		Map map = new HashMap();

		map.put("name", "Cow");
		map.put("price", "$100.00");
		list.add(map);

		map = new HashMap();
		map.put("name", "Eagle");
		map.put("price", "$59.99");
		list.add(map);

		map = new HashMap();
		map.put("name", "Shark");
		map.put("price", "$3.99");
		list.add(map);
		
		
		String conteudo = componentVelocityTemplate.configMsgTemplate(list);
		senderMailService.sendHtmlTemplate("cleiton2281@gmail.com", "Teste Com template HTML e Imagem no corpo do Email", conteudo);
		
	}
	

}
