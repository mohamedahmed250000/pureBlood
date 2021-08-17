package elteam.pureblood.models;

import java.io.Serializable;

public class ItemTips implements Serializable {

    private String question, answer;
    private int image;

    public ItemTips() {
    }

    public ItemTips(String question, String answer, int image) {
        this.question = question;
        this.answer = answer;
        this.image = image;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
