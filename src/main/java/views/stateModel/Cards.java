package views.stateModel;

public class Cards {
    private String title;
    private Long value;

    public Cards(String title, Long value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() { return title; }
    public Long getValue() { return value; }
}
