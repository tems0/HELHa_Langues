package helha.tems.helha_langue.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String email, String sujet, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("HEHLA Langues <hehlalangues@gmail.com>");
        message.setTo(email);
        message.setText(body + "\n\nCeci est un mail automatique, veuillez ne pas y repondre");
        message.setSubject(sujet);

        mailSender.send(message);
    }
}
