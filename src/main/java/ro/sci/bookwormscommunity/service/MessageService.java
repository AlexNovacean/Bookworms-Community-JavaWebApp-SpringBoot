package ro.sci.bookwormscommunity.service;

import ro.sci.bookwormscommunity.model.Message;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.web.dto.MessageDto;

import java.util.List;

/**
 * Service that handles the messages within a conversation.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 */
public interface MessageService {
    List<Message> getUserMessages(long conversationId);

    void saveMessage(long conversationId, MessageDto messageDto, User fromUser);
}
