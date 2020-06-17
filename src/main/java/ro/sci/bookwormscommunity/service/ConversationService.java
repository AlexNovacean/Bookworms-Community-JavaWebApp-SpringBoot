package ro.sci.bookwormscommunity.service;

import ro.sci.bookwormscommunity.model.Conversation;

import java.util.List;

/**
 * Service with all the required methods to handle the user's conversation.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 */
public interface ConversationService {
    Conversation startConversation(long toUserId, long fromUserId, String bookName);

    List<Conversation> getUserConversations(long userId);

    Conversation findById(long conversationId);

    void deleteById(long conversationId);
}
