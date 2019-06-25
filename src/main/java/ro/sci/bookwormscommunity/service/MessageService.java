package ro.sci.bookwormscommunity.service;

import ro.sci.bookwormscommunity.model.Message;
import ro.sci.bookwormscommunity.web.dto.MessageDto;

import java.security.Principal;
import java.util.List;

public interface MessageService {
    List<Message> getUserMessages(long convId);

    void saveMessage(Message message);

    List<Message> saveAndRetrieve(long convId, MessageDto messageDto, Principal principal);
}
