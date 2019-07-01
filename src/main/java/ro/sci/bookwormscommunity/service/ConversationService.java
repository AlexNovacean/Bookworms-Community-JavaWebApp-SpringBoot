package ro.sci.bookwormscommunity.service;

import ro.sci.bookwormscommunity.model.Conversation;

import java.util.List;

public interface ConversationService {
    Conversation startConversation(long toUserId, long fromUserId, String bookName);

    List<Conversation> getUserConversations(long id);

    Conversation findById(long id);

    void deleteById(long id);
}
