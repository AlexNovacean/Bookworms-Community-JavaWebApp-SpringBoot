package ro.sci.bookwormscommunity.service;

import org.springframework.mail.MailException;
import ro.sci.bookwormscommunity.model.User;

/**
 * Service that sends an e-mail to the user when banned.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 */
public interface BanMailService {
    void sendAccountDisabledMail(User user);

    void sendAccountEnabledMail(User user);
}
