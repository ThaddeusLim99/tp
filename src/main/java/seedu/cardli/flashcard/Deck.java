package seedu.cardli.flashcard;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import seedu.cardli.exceptions.CardLiException;
import seedu.cardli.exceptions.FieldEmptyException;
import seedu.cardli.exceptions.NoSlashException;
import seedu.cardli.parser.Parser;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Implements the list of added flashcards.
 */
public class Deck {

    private static final String EMPTY_DESCRIPTION_ERROR_MESSAGE = "\tCan't delete a card with no description!";
    private static final String CARD_DOES_NOT_EXIST_ERROR_MESSAGE =
            "\tThe card you are trying to delete does not exist.";

    private final ArrayList<FlashCard> cards = new ArrayList<FlashCard>();
    private String name;
    private static final Logger logger = Logger.getLogger(Deck.class.getName());

    public Deck(String name) {
        this.name = name;
    }

    public Deck() {
        this.name = "Untitled";
    }

    public boolean hasSameName(String input) {
        return name.trim().equals(input.trim());
    }

    public String editCard(String[] parameters) {
        String enteredCardIndex = parameters[0];
        int cardIndex = Integer.parseInt(enteredCardIndex) - 1;
        String side = parameters[1];
        boolean isFront = side.equalsIgnoreCase("front");
        String changeTo = parameters[2];

        if (isFront) {
            cards.get(cardIndex).setFront(changeTo);
        } else {
            cards.get(cardIndex).setBack(changeTo);
        }
        return ("Changed " + side + " of card " + enteredCardIndex + " to " + changeTo);
    }

    public String getName() {
        return name;
    }

    public ArrayList<FlashCard> getCards() {
        return cards;
    }

    public void setDeckName(String input) {
        this.name = input;
    }

    public FlashCard getCard(int index) {
        assert getDeckSize() > 0;
        assert (index >= 0 && index < getDeckSize());
        return cards.get(index);
    }

    public int getDeckSize() {
        return cards.size();
    }

    private String returnNewFlashCard(String front, String back) {
        String result = "\tAdded card:" + System.lineSeparator()
                + returnCardInfo(front, back);
        if (getDeckSize() == 1) {
            result = result.concat("\tYou have " + getDeckSize()
                    + " card in your card deck." + System.lineSeparator());
        } else {
            result = result.concat("\tYou have " + getDeckSize()
                    + " cards in your card deck." + System.lineSeparator());

        }
        return result;
    }

    private String returnCardInfo(String front, String back) {
        return "\t\tFront: " + front + System.lineSeparator()
                + "\t\tBack: " + back + System.lineSeparator();
    }

    private String returnDeletedFlashCardMessage(String front, String back) {
        String result = "\tDeleted card:" + System.lineSeparator();
        result = result.concat(returnCardInfo(front, back));
        return result;
    }

    public String prepareToAddFlashCard(String[] input) {
        //String[] flashCardWords = trimStrings(input);
        addFlashCard(input[0], input[1]);
        return returnNewFlashCard(input[0], input[1]);
    }

    /**
     * Deletes the flash card given by the user's input.
     * The card will only be deleted if the input matches
     * exactly with FlashCard.front.
     *
     * @param input user's input in its entirety
     */
    public String prepareToDeleteFlashCard(String input) {
        logger.entering(Deck.class.getName(), "prepareToDeleteFlashCard");
        logger.setLevel(Level.WARNING);
        logger.log(Level.INFO, "Starting delete process");
        String result = "";
        try {
            result = deleteFlashCard(input);
        } catch (ArrayIndexOutOfBoundsException e) {
            result = EMPTY_DESCRIPTION_ERROR_MESSAGE;
            logger.log(Level.SEVERE, "Empty field error, no description found after command term");
        } catch (CardLiException e) {
            result = "\tThe card you are trying to delete does not exist.";
            logger.log(Level.SEVERE, "CardLi error, query card does not exist");
        }
        logger.log(Level.INFO, "End of delete process");
        logger.exiting(Deck.class.getName(), "prepareToDeleteFlashCard");
        return result;
    }

    /**
     * Deletes the flashcard with the given input.
     *
     * @param input description of the card to delete
     * @throws CardLiException if card does not exist
     */
    public String deleteFlashCard(String input) throws CardLiException {
        logger.setLevel(Level.WARNING);
        if (cards.isEmpty()) {
            throw new CardLiException();
        }
        assert getDeckSize() > 0 : "cards.size() should be greater than 0";
        logger.log(Level.INFO, "Detecting the type of input, ie word/phrase or index");
        if (Parser.isInteger(input)) {
            return deleteFlashCardByIndex(input);
        } else {
            return deleteFlashCardByIndex(input);
        }
    }

    /**
     * Deletes the flashcard with the given index.
     *
     * @param index user's input (index of the card to be deleted)
     * @throws CardLiException if the index of the card exceeds the number of flashcards in cards
     *                         or index of card is less than 1
     */
    public String deleteFlashCardByIndex(String index) throws CardLiException {
        logger.setLevel(Level.WARNING);
        int indexToBeRemoved = Integer.parseInt(index) - 1;
        if (!((indexToBeRemoved < getDeckSize()) && (indexToBeRemoved >= 0))) {
            throw new CardLiException("Please enter a valid deck index.");
        }
        assert getDeckSize() > 0 : "cards.size() should be greater than 0";
        logger.log(Level.INFO, "Detecting the type of input, ie word/phrase or index");

        FlashCard card = cards.get(indexToBeRemoved);
        cards.remove(card);
        return returnDeletedFlashCardMessage(card.getFront(), card.getBack());
    }

    /**
     * Deletes the flashcard with the given description.
     *
     * @param description user's input (front of the card to be deleted)
     * @throws CardLiException if none of the front of the cards match the description input by user
     */
    private String deleteFlashCardByDescription(String description) throws CardLiException {
        assert getDeckSize() > 0 : "cards.size() should be greater than 0";
        for (int i = 0; i < getDeckSize(); i++) {
            FlashCard card = cards.get(i);
            if (hasExactCard(description, card)) {
                cards.remove(card);
                return returnDeletedFlashCardMessage(card.getFront(), card.getBack());
            }
        }
        throw new CardLiException();
    }

    private boolean hasExactCard(String query, FlashCard card) {
        return card.getFront().equalsIgnoreCase(query);
    }

    //this one only appears in tests
    public String[] trimStrings(String input) throws FieldEmptyException, NoSlashException {
        int slashIndex = input.indexOf("/bac");
        String[] flashCardWords = new String[2];
        if (slashIndex < 3) {
            throw new NoSlashException();
        }
        flashCardWords[0] = input.substring(0, slashIndex - 1).trim();
        flashCardWords[1] = input.substring(slashIndex + 4).trim();
        if (flashCardWords[0].isEmpty() || flashCardWords[1].isEmpty()) {
            throw new FieldEmptyException();
        }
        return flashCardWords;
    }


    public void addFlashCard(String front, String back) {
        cards.add(new FlashCard(front, back));
    }

    public void addFlashCard(FlashCard card) {
        cards.add(card);
    }

    //TODO: fix this
    public void addFlashCard(String front, String back, int userScore, int totalScore) {
        cards.add(new FlashCard(front, back, userScore, totalScore));

    }

    public int getCardIndex(FlashCard card) {
        return cards.indexOf(card);
    }

    public String returnAllFlashCards() { // TODO: throw exception if no cards
        String result = "";
        if (getDeckSize() > 0) {
            for (int i = 0; i < getDeckSize(); i++) {
                result = result.concat("Card " + (i + 1) + ":" + System.lineSeparator());
                FlashCard card = cards.get(i);
                result = result.concat(card.returnFlashCard());
            }
        } else {
            result = "This deck has no cards." + System.lineSeparator();
        }
        return result;
    }

    public void viewAllFlashCards() {
        String result = returnAllFlashCards();
        System.out.println(result);
    }

    public String returnMatchingFlashCards(String searchInput) {
        String result = "";
        ArrayList<FlashCard> matchingCards = (ArrayList<FlashCard>) cards.stream()
                .filter((f) -> f.getFront().contains(searchInput) || f.getBack().contains(searchInput))
                .collect(Collectors.toList());
        if (matchingCards.size() > 0) {
            result = result.concat("Decks in " + getName() + " that contain the term " + searchInput + ":"
                    + System.lineSeparator());
            for (int i = 0; i < matchingCards.size(); i += 1) {
                result = result.concat("Card " + (i + 1) + ":" + System.lineSeparator());
                FlashCard card = matchingCards.get(i);
                result = result.concat(card.returnFlashCard());
            }
            result = result.concat(System.lineSeparator());
        }
        return result;
    }

    @Override
    public String toString() {
        String cardsString = "";
        int cardsCount = getDeckSize();

        for (int i = 0; i < cardsCount; i++) {
            cardsString += cards.get(i);
        }

        return getName() + '\n'
                + cardsCount + '\n'
                + cardsString;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonDeck = new JSONObject();

        int cardsCount = getDeckSize();
        JSONArray jsonCards = new JSONArray();

        for (int i = 0; i < cardsCount; i++) {
            jsonCards.add(cards.get(i).toJsonObject());
        }
        jsonDeck.put("deckName", getName());
        jsonDeck.put("cards", jsonCards);
        return jsonDeck;
    }
}