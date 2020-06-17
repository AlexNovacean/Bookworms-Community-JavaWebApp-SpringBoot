package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ro.sci.bookwormscommunity.model.User;

/**
 * Implementation for the {@link BanMailService}.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 * @see BanMailService
 * @see JavaMailSender
 */
@Service
public class BanMailServiceImpl implements BanMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * Method used to send an e-mail to a given user when the user is banned.
     *
     * @param user {@link User} instance, the user which will receive the email.
     * @throws MailException if the e-mail fails to be delivered.
     */
    @Override
    public void sendAccountDisabledMail(User user){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("bookworms.community.airs@gmail.com");
        mailMessage.setSubject("Account Disabled! - Bookworms Community");
        mailMessage.setText("Hello " + user.getNickname() + ",\n\n Your account has been disabled due to inappropriate behaviour and/or violation of Bookworms Community Rules.\n\nHave a nice day, \nBookworms Community Team.");

        javaMailSender.send(mailMessage);
    }

    @Override
    public void sendAccountEnabledMail(User user){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("bookworms.community.airs@gmail.com");
        mailMessage.setSubject("Account Enabled! - Bookworms Community");
        mailMessage.setText("Hello " + user.getNickname() + ",\n\nYour account has been enabled.\nCome on and join the community once more.\n\nHave a nice day,\nBookworms Community Team.");

        javaMailSender.send(mailMessage);
    }
}
