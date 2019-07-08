package ro.sci.bookwormscommunity.web.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ro.sci.bookwormscommunity.config.WebSecurityConfig;
import ro.sci.bookwormscommunity.model.Conversation;
import ro.sci.bookwormscommunity.model.Message;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.service.ConversationService;
import ro.sci.bookwormscommunity.service.MessageService;
import ro.sci.bookwormscommunity.service.UserService;
import ro.sci.bookwormscommunity.web.dto.MessageDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(MessagesController.class)
@Import(WebSecurityConfig.class)
public class MessagesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConversationService conversationService;

    @MockBean
    private MessageService messageService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    public void seeMessages() throws Exception {
        User user = new User(1L, "test@mail.com");
        user.setNickname("nickname");
        List<Conversation> conversations = new ArrayList<>(Arrays.asList(new Conversation(user, user, "topic"), new Conversation(user, user, "topic")));

        when(userService.findByEmail(anyString())).thenReturn(user);
        when(conversationService.getUserConversations(anyLong())).thenReturn(conversations);

        mockMvc.perform(get("/messages"))
                .andExpect(status().isOk())
                .andExpect(view().name("messages"))
                .andExpect(model().attribute("messages", hasSize(2)))
                .andExpect(model().attribute("messages", conversations))
                .andExpect(model().attribute("user", user));

        verify(userService, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userService);
        verify(conversationService, times(1)).getUserConversations(anyLong());
        verifyNoMoreInteractions(conversationService);
        verifyNoMoreInteractions(messageService);
    }

    @Test
    @WithMockUser
    public void seeSpecific() throws Exception {
        User user = new User(1L, "test@mail.com");
        user.setNickname("nickname");
        Conversation conversation = new Conversation(user, user, "topic");
        conversation.setId(1L);
        List<Message> messages = new ArrayList<>(Arrays.asList(new Message("content", user, user), new Message("content", user, user)));

        when(userService.findById(anyLong())).thenReturn(user);
        when(userService.findByEmail(anyString())).thenReturn(user);
        when(conversationService.startConversation(anyLong(), anyLong(), anyString())).thenReturn(conversation);
        when(conversationService.findById(anyLong())).thenReturn(conversation);
        when(messageService.getUserMessages(anyLong())).thenReturn(messages);

        mockMvc.perform(get("/messages/{id}", 1L).param("bookName", "Book"))
                .andExpect(status().isOk())
                .andExpect(view().name("texts"))
                .andExpect(model().attribute("texts", hasSize(2)))
                .andExpect(model().attribute("texts", messages))
                .andExpect(model().attribute("conv", conversation));

        verify(userService, times(1)).findByEmail(anyString());
        verify(userService, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userService);
        verify(conversationService, times(1)).startConversation(anyLong(), anyLong(), anyString());
        verify(conversationService, times(1)).findById(anyLong());
        verifyNoMoreInteractions(conversationService);
        verify(messageService, times(1)).getUserMessages(anyLong());
        verifyNoMoreInteractions(messageService);
    }

    @Test
    @WithMockUser
    public void openConversation() throws Exception {
        User user = new User(1L, "test@mail.com");
        user.setNickname("nickname");
        Conversation conversation = new Conversation(user, user, "topic");
        conversation.setId(1L);
        List<Message> messages = new ArrayList<>(Arrays.asList(new Message("content", user, user), new Message("content", user, user)));

        when(messageService.getUserMessages(anyLong())).thenReturn(messages);
        when(conversationService.findById(anyLong())).thenReturn(conversation);

        mockMvc.perform(get("/messages/conversation/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("texts"))
                .andExpect(model().attribute("texts", hasSize(2)))
                .andExpect(model().attribute("texts", messages))
                .andExpect(model().attribute("conv", conversation));

        verify(messageService, times(1)).getUserMessages(anyLong());
        verifyNoMoreInteractions(messageService);
        verify(conversationService, times(1)).findById(anyLong());
        verifyNoMoreInteractions(conversationService);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @WithMockUser
    public void sendMessage() throws Exception {
        User user = new User(1L, "test@mail.com");
        user.setNickname("nickname");
        Conversation conversation = new Conversation(user, user, "topic");
        conversation.setId(1L);
        List<Message> messages = new ArrayList<>();

        when(userService.findByEmail(anyString())).thenReturn(user);
        when(conversationService.findById(anyLong())).thenReturn(conversation);
        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();
            MessageDto messageDto = (MessageDto) arguments[1];
            messages.add(new Message(messageDto.getContent(), user, user));
            return null;
        }).when(messageService).saveMessage(anyLong(), any(MessageDto.class), any(User.class));

        mockMvc.perform(post("/messages/{id}", 1L)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())
                .param("content", "Message Content")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/messages/1?bookName=topic#lastmsg"))
                .andExpect(view().name("redirect:/messages/1?bookName=topic#lastmsg"));

        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals("Message Content", messages.get(0).getContent());
        verify(userService, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userService);
        verify(messageService, times(1)).saveMessage(anyLong(), any(MessageDto.class), any(User.class));
        verifyNoMoreInteractions(messageService);
        verify(conversationService, times(1)).findById(anyLong());
        verifyNoMoreInteractions(conversationService);
    }

    @Test
    @WithMockUser
    public void deleteConversation() throws Exception {
        doNothing().when(conversationService).deleteById(anyLong());

        mockMvc.perform(get("/messages/{id}/delete", 1L))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/messages?deleted"))
                .andExpect(view().name("redirect:/messages?deleted"));

        verify(conversationService, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(conversationService);
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(messageService);
    }
}