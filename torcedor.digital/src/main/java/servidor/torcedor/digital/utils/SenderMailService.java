package servidor.torcedor.digital.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SenderMailService {
	
	@Autowired
    private JavaMailSender mailSender;
	
	
    public void enviar() {
        
    	SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("cleiton2281@gmail.com");
        email.setTo("cleiton2281@gmail.com");
        //email.setTo("rochelle.sara@hotmail.com");
        //email.setBcc("cleiton2281@gmail.com");
        email.setSubject("Teste envio de e-mail");
        email.setText("Enviei este e-mail usando o servidor, Torcedor Digital.");
        
        mailSender.send(email);
    }
    
    public void send(String emailPara, String titulo, String conteudo) {
    	
    	SimpleMailMessage email = new SimpleMailMessage();
    	email.setFrom("cleiton2281@gmail.com");
        email.setTo(emailPara);
        //email.setBcc("cleiton2281@gmail.com");
        email.setSubject(titulo);
        email.setText(conteudo);
     
        mailSender.send(email);
    	
    }

}
