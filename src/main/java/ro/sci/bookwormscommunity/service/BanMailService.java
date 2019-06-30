package ro.sci.bookwormscommunity.service;

import org.springframework.mail.MailException;
import ro.sci.bookwormscommunity.model.User;

public interface BanMailService {
    void sendAccountDisabledMail(User user) throws MailException;
}
