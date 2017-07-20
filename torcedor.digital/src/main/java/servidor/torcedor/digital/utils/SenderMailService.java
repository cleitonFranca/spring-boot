package servidor.torcedor.digital.utils;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class SenderMailService {

	private static final String CONTA = "torcedordigitaladm@gmail.com";
	
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
     * @param emailPara
     * : e-mail do destinatário
     * @param titulo
     * : título do e-mail
     * @param conteudo
     * : conteúdo html do e-mail
     * @throws MessagingException
     * @throws UnsupportedEncodingException 
     */
    public void sendHtmlTemplate(String emailPara, String titulo, String conteudo) throws MessagingException, UnsupportedEncodingException {
  
    	MimeMessage mimeMessage = mailSender.createMimeMessage();
    	MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
    	String htmlMsg = conteudo;
    	mimeMessage.setContent(htmlMsg, "text/html");
    	
    	helper.setTo(emailPara);
    	helper.setSubject(titulo);
    	helper.setFrom(CONTA, "Torcedor Digital");
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
