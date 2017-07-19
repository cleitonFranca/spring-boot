package servidor.torcedor.digital.utils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class SenderMailService {
	
	@Autowired
    private JavaMailSender mailSender;
	
	
    public void enviar() {
    	SimpleMailMessage email = new SimpleMailMessage();
        
    	email.setFrom("cleiton2281@gmail.com");
        email.setTo("profissional.cleiton@gmail.com");
        email.setSubject("Teste envio de e-mail");
        email.setText("Enviei este e-mail usando o servidor, Torcedor Digital.");
        
        mailSender.send(email);
    }
    
    public void send(String emailPara, String titulo, String conteudo) {
    	SimpleMailMessage email = new SimpleMailMessage();
    	
    	email.setFrom("cleiton2281@gmail.com");
        email.setTo(emailPara); 
        email.setSubject(titulo);
        email.setText(conteudo);
     
        mailSender.send(email);
    	
    }
    
    /**
     * Método para envio de e-mail com template em html
     * @param emailPara: email do destinatário
     * @param titulo: título do e-mail
     * @param conteudo: conteúdo html do e-mail
     * @throws MessagingException
     */
    public void sendHtmlTemplate(String emailPara, String titulo, String conteudo) throws MessagingException {
  
    	MimeMessage mimeMessage = mailSender.createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
    	String htmlMsg = conteudo;
    	mimeMessage.setContent(htmlMsg, "text/html");
    	
    	helper.setTo(emailPara);
    	helper.setSubject(titulo);
    	helper.setFrom("cleiton2281@gmail.com");

        mailSender.send(mimeMessage);
    	
    }
    
    public boolean sendConfirm(String emailPara, String titulo, String conteudo) {
    	SimpleMailMessage email = new SimpleMailMessage();
    	try {
    		email.setFrom("cleiton2281@gmail.com");
    		email.setTo(emailPara); 
    		email.setSubject(titulo);
    		email.setText(conteudo);
    		
    		mailSender.send(email);
    		
    		return true;

    	} catch (Exception e) {
			return false;
		}
    	
    	
    }

}
