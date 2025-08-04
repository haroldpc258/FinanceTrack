package edu.sena.finance.track.services;

import edu.sena.finance.track.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class InvitationService {

    @Value("${app.url}")
    private String appUrl;

    private final JavaMailSender emailSender;

    public InvitationService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendInvitationEmail(User user) {

        String loginUrl = appUrl + "/oauth2/authorization/okta" ;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Invitación a " + user.getCompany().getName());
        message.setText(
                "Hola " + user.getName() + ",\n\n" +
                        "Has sido invitado a unirte a " + user.getCompany().getName() + " en nuestro sistema de gestión financiera.\n\n" +
                        "Para acceder al sistema, haz clic en el siguiente enlace:\n\n" +
                        loginUrl + "\n\n" +
                        "Si es la primera vez que ingresas, deberás registrarte con este mismo correo electrónico.\n\n" +
                        "Saludos,\n" +
                        "El equipo de " + user.getCompany().getName()
        );

        emailSender.send(message);
    }
}
