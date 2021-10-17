package seedu.duke;

import java.util.logging.Level;

public class Category {
    private String name;
    private Deck manager;

    public Category(String name, Deck manager) {
        this.name = name;
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public Deck getManager() {
        return manager;
    }

    public void setCatName(String input) {
        this.name = input;
    }
}
