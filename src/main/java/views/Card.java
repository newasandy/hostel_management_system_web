package views;

public class Card {
    private String title;
    private int value;

    public Card(String title, int value) {
        this.title = title;
        this.value = value;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public int getValue() { return value; }
}
