package ro.sci.bookwormscommunity.model;

import javax.persistence.*;
import java.util.List;

/**
 * POJO class representing the conversation between two users.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 */
@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "toUserId", referencedColumnName = "id")
    private User toUser;

    @OneToOne
    @JoinColumn(name = "fromUserId", referencedColumnName = "id")
    private User fromUser;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    private String conversationTopic;

    public Conversation() {
    }

    public Conversation(User toUser, User fromUser, String conversationTopic) {
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.conversationTopic = conversationTopic;
    }

    public String getConversationTopic() {
        return conversationTopic;
    }

    public void setConversationTopic(String conversationTopic) {
        this.conversationTopic = conversationTopic;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }
}
