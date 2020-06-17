package ro.sci.bookwormscommunity.web.dto;

/**
 * POJO class who's instances will be used as Data Transfer Object between the client and the server.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 */
public class MessageDto {

    private String content;

    public MessageDto() {
    }

    public MessageDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
