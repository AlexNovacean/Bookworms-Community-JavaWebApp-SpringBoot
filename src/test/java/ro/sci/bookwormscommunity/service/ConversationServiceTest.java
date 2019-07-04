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
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.ConversationRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ConversationServiceTest {

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ConversationService conversationService = new ConversationServiceImpl();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startConversation_existingConversation() {
        Conversation conversation = new Conversation();
        when(conversationRepository.getConversation(anyLong(), anyLong(), anyString())).thenReturn(conversation);

        Conversation result = conversationService.startConversation(1, 2, "Topic");

        assertNotNull(result);
        assertEquals(conversation, result);
        verify(conversationRepository, times(1)).getConversation(anyLong(), anyLong(), anyString());
    }

    @Test
    public void startConversation_non_existingConversation() {
        List<Conversation> conversations = new ArrayList<>();
        User toUser = new User();
        User fromUser = new User();

        when(userService.findById(1L)).thenReturn(toUser);
        when(userService.findById(2L)).thenReturn(fromUser);
        when(conversationRepository.getConversation(anyLong(), anyLong(), anyString())).thenReturn(null);
        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();
            Conversation conversation = (Conversation) arguments[0];
            conversations.add(conversation);
            return null;
        }).when(conversationRepository).save(any(Conversation.class));

        conversationService.startConversation(1, 2, "Topic");

        assertNotNull(conversations);
        assertEquals(1, conversations.size());
        assertTrue(conversations.stream().anyMatch(c -> c.getToUser().equals(toUser)));
        assertTrue(conversations.stream().anyMatch(c -> c.getFromUser().equals(fromUser)));
        assertTrue(conversations.stream().anyMatch(c -> c.getConversationTopic().equals("Topic")));
        verify(conversationRepository, times(1)).getConversation(anyLong(), anyLong(), anyString());
        verify(conversationRepository, times(1)).save(any(Conversation.class));
        verify(userService, times(2)).findById(anyLong());
    }

    @Test
    public void getUserConversations() {
        List<Conversation> conversations = new ArrayList<>(Arrays.asList(new Conversation(), new Conversation(), new Conversation()));

        when(conversationRepository.getConversations(anyLong())).thenReturn(conversations);

        List<Conversation> result = conversationService.getUserConversations(1);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(conversations, result);
        verify(conversationRepository, times(1)).getConversations(anyLong());
    }

    @Test
    public void findById() {
        Optional<Conversation> conversation = Optional.of(new Conversation());

        when(conversationRepository.findById(anyLong())).thenReturn(conversation);

        Conversation result = conversationService.findById(1);

        assertNotNull(result);
        assertEquals(conversation.get(), result);
        verify(conversationRepository, times(1)).findById(anyLong());
    }

    @Test
    public void deleteById() {
        List<Conversation> conversations = new ArrayList<>(Arrays.asList(new Conversation(1), new Conversation(2)));

        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();

            long conversationId = (Long) arguments[0];
            conversations.removeIf(c -> c.getId() == conversationId);
            return null;
        }).when(conversationRepository).deleteById(anyLong());

        conversationService.deleteById(1);

        assertEquals(1, conversations.size());
        assertFalse(conversations.stream().anyMatch(c -> c.getId() == 1));
        verify(conversationRepository, times(1)).deleteById(anyLong());
    }
}