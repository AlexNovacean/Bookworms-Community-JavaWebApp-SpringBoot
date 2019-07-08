package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.sci.bookwormscommunity.model.Conversation;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.ConversationRepository;

import java.util.List;

/**
 * Implementation for the {@link ConversationService}.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @see ConversationService
 */
@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserService userService;

    /**
     * Checks the DB if a {@link Conversation} with the provided toUserId, fromUserId and bookName exists and returns it.
     * If a {@link Conversation} is not found a new {@link Conversation} object is created with the provided parameters. The new {@link Conversation} object is saved to DB and returned.
     *
     * @param toUserId   identifier of the user with whom the conversation is started.
     * @param fromUserId identifier of the user who starts the conversation.
     * @param bookName   the topic of the conversation, the name of the book around which the conversation is taking place.
     * @return a {@link Conversation} instance.
     */
    @Override
    public Conversation startConversation(long toUserId, long fromUserId, String bookName) {
        Conversation conversation = conversationRepository.getConversation(toUserId, fromUserId, bookName);

        if (conversation == null) {
            User toUser = userService.findById(toUserId);
            User fromUser = userService.findById(fromUserId);
            conversation = new Conversation(toUser, fromUser, bookName);
            conversationRepository.save(conversation);
        }

        return conversation;
    }

    /**
     * Returns the all the conversation in which the user is engaged.
     *
     * @param userId the user identifier
     * @return a list of {@link Conversation} object
     */
    @Override
    public List<Conversation> getUserConversations(long userId) {
        return conversationRepository.getConversations(userId);
    }

    /**
     * Return a specific {@link Conversation} object.
     *
     * @param conversationId the identifier of the conversation to be returned.
     * @return {@link Conversation} instance.
     */
    @Override
    public Conversation findById(long conversationId) {
        return conversationRepository.findById(conversationId).get();
    }

    /**
     * Deletes from DB a specific {@link Conversation}.
     *
     * @param conversationId the indentifier of the conversation to be deleted.
     */
    @Override
    public void deleteById(long conversationId) {
        conversationRepository.deleteById(conversationId);
    }
}
