package ro.sci.bookwormscommunity.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.test.context.junit4.SpringRunner;
import ro.sci.bookwormscommunity.model.Conversation;
import ro.sci.bookwormscommunity.model.Message;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.MessageRepository;
import ro.sci.bookwormscommunity.web.dto.MessageDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class MessageServiceTest {

    @Mock
    private ConversationService conversationService;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService = new MessageServiceImpl();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getUserMessages() {
        User toUser = new User();
        User fromUser = new User();
        when(messageRepository.getUserMessages(anyLong())).thenReturn(new ArrayList<>(Arrays.asList(new Message("Message1", toUser, fromUser), new Message("Message2", toUser, fromUser), new Message("Message3", toUser, fromUser))));

        List<Message> messages = messageService.getUserMessages(1);

        assertNotNull(messages);
        assertEquals(3, messages.size());
        assertTrue(messages.stream().allMatch(m -> (m.getFromUser().equals(fromUser) && m.getToUser().equals(toUser))));
        verify(messageRepository, times(1)).getUserMessages(anyLong());

    }

    @Test
    public void saveAndRetrieve() {
        List<Message> messages = new ArrayList<>();
        User toUser = new User();
        toUser.setEmail("toUser@mail.com");
        User fromUser = new User();
        fromUser.setEmail("fromUser@mail.com");
        Conversation conversation = new Conversation(toUser, fromUser, "Topic");
        when(conversationService.findById(anyLong())).thenReturn(conversation);
        when(messageRepository.getUserMessages(anyLong())).thenReturn(messages);
        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();
            Message message = (Message) arguments[0];
            messages.add(message);
            return null;
        }).when(messageRepository).save(any(Message.class));

        List<Message> result = messageService.saveAndRetrieve(1, new MessageDto("Content"), fromUser);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(m -> m.getConversation().equals(conversation)));
        assertTrue(result.stream().anyMatch(m -> m.getContent().equals("Content")));
        assertTrue(result.stream().anyMatch(m -> m.getToUser().equals(toUser)));
        assertTrue(result.stream().anyMatch(m -> m.getFromUser().equals(fromUser)));
        verify(messageRepository, times(1)).getUserMessages(anyLong());
        verify(messageRepository, times(1)).save(any(Message.class));
        verify(conversationService, times(1)).findById(anyLong());
    }
}