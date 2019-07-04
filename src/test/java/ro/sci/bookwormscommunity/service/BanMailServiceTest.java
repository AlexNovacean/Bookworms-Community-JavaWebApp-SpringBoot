package ro.sci.bookwormscommunity.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import ro.sci.bookwormscommunity.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BanMailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private BanMailService banMailService = new BanMailServiceImpl();

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sendAccountDisabledMail() {

        List<SimpleMailMessage> mailMessages = new ArrayList<>();

        User user = new User();
        user.setEmail("test@mail.com");
        user.setNickname("nickname");

        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();
            mailMessages.add((SimpleMailMessage)arguments[0]);
            return null;
        }).when(javaMailSender).send(any(SimpleMailMessage.class));

        banMailService.sendAccountDisabledMail(user);

        assertNotNull(mailMessages);
        assertEquals(1,mailMessages.size());
        assertTrue(mailMessages.stream().anyMatch(m -> m.getFrom().equals("bookworms.community.airs@gmail.com")));
        assertTrue(mailMessages.stream().anyMatch(m -> m.getTo()[0].equals("test@mail.com")));
        assertTrue(mailMessages.stream().anyMatch(m->m.getSubject().equals("Account Disabled! - Bookworms Community")));
        assertTrue(mailMessages.stream().anyMatch(m->m.getText().equals("Hello " + user.getNickname() + ",\n\n Your account has been disabled due to inappropriate behaviour and/or violation of Bookworms Community Rules.\n\nHave a nice day, \nBookworms Community Team.")));
        verify(javaMailSender,times(1)).send(any(SimpleMailMessage.class));

    }
}