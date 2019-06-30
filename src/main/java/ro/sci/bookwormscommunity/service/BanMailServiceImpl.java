package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ro.sci.bookwormscommunity.model.User;

@Service
public class BanMailServiceImpl implements BanMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendAccountDisabledMail(User user) throws MailException {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("bookworms.community.airs@gmail.com");
        mailMessage.setSubject("Account Disabled! - Bookworms Community");
        mailMessage.setText("Hello " + user.getNickname() + ",\n\n Your account has been disabled due to inappropriate behaviour and/or violation of Bookworms Community Rules.\n\nHave a nice day, \nBookworms Community Team.");

        javaMailSender.send(mailMessage);
    }
}
