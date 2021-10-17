package seedu.duke;

import seedu.duke.exceptions.DeckNotExistException;
import seedu.duke.exceptions.NoSlashException;

import java.util.ArrayList;

public class CategoryList {
    private static ArrayList<Category> decks = new ArrayList<>();

    public static void editCard(String[] args) {
        if (args[2].equalsIgnoreCase("front")) {
            decks.get(Integer.parseInt(args[0]) - 1).getManager().getCard(Integer.parseInt(args[1]) - 1).setFront(args[3]);
        } else {
            decks.get(Integer.parseInt(args[0]) - 1).getManager().getCard(Integer.parseInt(args[1]) - 1).setBack(args[3]);
        }
    }

    public static void editCat(String[] args) {
        decks.get(Integer.parseInt(args[0]) - 1).setCatName(args[1]);
    }

    public static int getDecksSize() {
        return decks.size();
    }

    //to implement error handling
    public static Category getDeck(int index) {
        return decks.get(index);
    }
    public static void prepareToAddCategory(String categoryName) {
        if (!categoryExists(categoryName)) {
            addCategory(categoryName);
            printNewCategory(categoryName);
        } else {
            System.out.println("The category you are trying to create already exists.");
        }
    }

    private static void printNewCategory(String categoryName) {
        System.out.println("You have just made the category <<" + categoryName + ">>.");
    }

    private static boolean categoryExists(String categoryName) {
        for (Category fcc : decks) {
            if (fcc.getName().trim().equals(categoryName.trim())) {
                return true;
            }
        }
        return false;
    }

    private static void addCategory(String categoryName) {
        decks.add(new Category(categoryName, new Deck()));
    }

    public static void viewCategories() {
        if (decks.size() > 0) {
            int i = 1;
            System.out.println("These are your decks: ");
            for (Category fcc : decks) {
                System.out.println(i + ". " + fcc.getName());
                i += 1;
            }
        } else {
            System.out.println("You have no decks.");
        }
    }

    public static void viewOneCategory(String input) {
        try {
            int deckIndex = Integer.parseInt(input) - 1;
            if (deckIndex < decks.size() && deckIndex >= 0) {
                System.out.println("Viewing deck " + decks.get(deckIndex).getName() + " :");
                Deck fcmToView = decks.get(deckIndex).getManager();
                fcmToView.viewAllFlashCards();
            } else {
                throw new DeckNotExistException();
            }
        } catch (DeckNotExistException e) {
            System.out.println("This deck doesn't exist.");
        }
    }

    public static void testCategory(String input) {
        try {
            int deckIndex = Integer.parseInt(input) - 1;
            if (deckIndex < decks.size() && deckIndex >= 0) {
                Deck fcm = decks.get(deckIndex).getManager();
                if (fcm.cards.size() > 0) {
                    System.out.println("Testing deck " + decks.get(deckIndex).getName() + ":");
                    TestManager.testAllCardsInOrder(fcm);
                } else {
                    System.out.println("This deck has no cards and cannot be tested.");
                }
            } else {
                throw new DeckNotExistException();
            }
        } catch (DeckNotExistException e) {
            System.out.println("This deck doesn't exist.");
        }
    }

    public static void prepareToAddCardToDeck(String input) {
        try {
            int deckIndex = findDeckIndex(input, "/fro");
            if (deckIndex < decks.size() && deckIndex >= 0) {
                String addInput = trimToPass(input, "/fro");
                System.out.println("Added to deck " + decks.get(deckIndex).getName() + ":");
                Deck fcmToAdd = decks.get(deckIndex).getManager();
                fcmToAdd.prepareToAddFlashCard(addInput);
            } else {
                throw new DeckNotExistException();
            }
        } catch (NumberFormatException e) {
            System.out.println("That's not a number.");
        } catch (DeckNotExistException e) {
            System.out.println("That deck doesn't exist.");
        } catch (NoSlashException e) {
            System.out.println("Incorrect format. The correct format is:");
            System.out.println("add <deckIndex> /fro <frontOfCard> /bac <backOfCard>");
        }
    }

    public static void prepareToDeleteCardFromDeck(String input) {
        try {
            int deckIndex = findDeckIndex(input, "/car");
            if (deckIndex < decks.size() && deckIndex >= 0) {
                String addInput = trimToPass(input, "/car");
                System.out.println("Deleted from deck " + decks.get(deckIndex).getName() + " :");
                decks.get(deckIndex).getManager().prepareToDeleteFlashCard(addInput);
            } else {
                throw new DeckNotExistException();
            }
        } catch (NumberFormatException e) {
            System.out.println("That's not a number.");
        } catch (DeckNotExistException e) {
            System.out.println("That deck doesn't exist.");
        } catch (NoSlashException e) {
            System.out.println("Wrong format. The correct format is:");
            System.out.println("delete <deckIndex> /car <indexOfCard/frontOfCard>");
        }
    }

    public static String trimToPass(String input, String toSplit) {
        int splitIndex = input.indexOf(toSplit);
        return input.substring(splitIndex + 4).trim();
    }

    private static int findDeckIndex(String input, String lookFor) throws NoSlashException {
        int splitIndex = input.indexOf(lookFor);
        if (splitIndex >= 2) {
            String intAsString = input.substring(0, splitIndex).trim();
            return Integer.parseInt(intAsString) - 1;
        } else {
            throw new NoSlashException();
        }
    }
}
