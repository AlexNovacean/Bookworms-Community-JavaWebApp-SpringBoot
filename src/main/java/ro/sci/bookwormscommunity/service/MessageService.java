package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.sci.bookwormscommunity.model.Message;
import ro.sci.bookwormscommunity.repositories.MessageRepository;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;


    public List<Message> getMessages(long toUserId, long fromUserId) {
        return messageRepository.getExistingMessages(fromUserId, toUserId);
    }

    public List<Message> getUserMessages(long userId) {
        return messageRepository.getUserMessages(userId);
    }

    public void saveMessage(Message message){
        messageRepository.save(message);
    }
}
