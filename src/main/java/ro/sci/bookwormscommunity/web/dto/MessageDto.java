package ro.sci.bookwormscommunity.web.dto;

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
