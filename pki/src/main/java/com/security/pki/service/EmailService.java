package com.security.pki.service;

import com.security.pki.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private Environment env;
    @Autowired
    private UserRepository userRepository;

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    public boolean sendAccountActivationMail(String token, String emailTo) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("PKI - Account activation");
            helper.setFrom(env.getProperty("spring.mail.username"));
            helper.setTo(emailTo);
            helper.setText(accountActivationMessage(token), true);
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String accountActivationMessage(String token) {
        String url = "http://localhost:" + env.getProperty("frontend.port") + "/activate-account/" + token;
        String msg = "<html><body><div style=\"background: linear-gradient(#007bff , #48c6ef); height: 320px; width: 500px; font-family: Montserrat, sans-serif; text-align: center; align-items: center; color: white; margin: 10px; padding: 4px; -webkit-box-shadow: 0px 7px 12px -6px rgba(0,0,0,0.62); box-shadow: 0px 7px 12px -6px rgba(0,0,0,0.62); border-radius: 10px;\">\n" +
                "\t\t\t<h1>Welcome</h1>\n" +
                "\t\t\t<h3 style=\"font-weight: normal;\">Verify account</h3>\n" +
                "\n" +
                "\t\t\t<a href=\"" + url + "\" style=\"box-shadow: -2px 7px 4px 0px #004cff18;\n" +
                "\t\t\tbackground-color:#ffffff;\n" +
                "\t\t\tborder-radius:10px;\n" +
                "\t\t\tborder-width: 0;\n" +
                "\t\t\tdisplay:inline-block;\n" +
                "\t\t\tcursor:pointer;\n" +
                "\t\t\tcolor:#48c5ef;\n" +
                "\t\t\tfont-family:Arial;\n" +
                "\t\t\tfont-size:20px;\n" +
                "\t\t\tfont-weight:bold;\n" +
                "\t\t\tpadding:14px 50px;\n" +
                "\t\t\ttext-decoration:none;\n" +
                "\t\t\ttext-shadow:0px 2px 10px #48c5ef;\">Verify</a>\n" +
                "\t\t</div></body></html> ";
        return  msg;
    }
}
