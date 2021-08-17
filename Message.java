package elteam.pureblood.models;

public class Message {

    private String senderId = null,
            receiverId = null,
            textMessage = null;

    public Message() {
    }

    public Message(String senderId, String receiverId, String textMessage) {

        this.senderId = senderId;
        this.receiverId = receiverId;
        this.textMessage = textMessage;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }
}
