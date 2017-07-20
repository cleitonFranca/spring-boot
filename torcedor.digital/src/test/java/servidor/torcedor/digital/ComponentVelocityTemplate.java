package servidor.torcedor.digital;

import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class ComponentVelocityTemplate {
		
	@SuppressWarnings({ "rawtypes" })
	public String configMsgTemplate(ArrayList list) throws Exception {
		
		/*
		 * first, get and initialize an engine
		 */
		VelocityEngine ve = new VelocityEngine();
		
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();

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
		return writer.toString();

	}

}
