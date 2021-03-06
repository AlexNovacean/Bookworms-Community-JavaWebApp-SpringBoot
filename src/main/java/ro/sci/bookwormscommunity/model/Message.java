package ro.sci.bookwormscommunity.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * POJO class representing the messages send between two users engaged in a Conversation.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 */
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(length = 1000)
    private String content;

    @NotNull
    private Date sentDate;

    @OneToOne
    @JoinColumn(name = "toUserId", referencedColumnName = "id")
    private User toUser;

    @OneToOne
    @JoinColumn(name = "fromUserId", referencedColumnName = "id")
    private User fromUser;

    @ManyToOne
    private Conversation conversation;

    public Message() {
        this.sentDate = new Date();
    }

    public Message(String content, User toUser, User fromUser) {
        this.content = content;
        this.sentDate = new Date();
        this.toUser = toUser;
        this.fromUser = fromUser;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
}
