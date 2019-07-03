package ro.sci.bookwormscommunity.service;

import ro.sci.bookwormscommunity.model.Message;
import ro.sci.bookwormscommunity.web.dto.MessageDto;

import java.security.Principal;
import java.util.List;

/**
 * Service that handles the messages within a conversation.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 */
public interface MessageService {
    List<Message> getUserMessages(long conversationId);

    List<Message> saveAndRetrieve(long conversationId, MessageDto messageDto, Principal principal);
}
