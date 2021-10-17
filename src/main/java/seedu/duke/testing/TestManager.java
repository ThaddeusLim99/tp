package seedu.duke.testing;

import seedu.duke.exceptions.FieldEmptyException;
import seedu.duke.flashcard.DeckList;
import seedu.duke.parser.TestParser;
import seedu.duke.ui.TestUi;
import seedu.duke.flashcard.Deck;
import seedu.duke.flashcard.FlashCard;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Implements the test function.
 */
public class TestManager {
    public static AnswerList answersResponse = new AnswerList();
    private static final TestUi ui = new TestUi();
    private static final Logger logger = Logger.getLogger(TestManager.class.getName());

    /**
     * Enters test mode and requires user to input the index of the deck that they want to be tested.
     */
    public static void startTest() {
        ui.printStartTest();
        String input = ui.getUserMessage();
        try {
            int deckIndex = TestParser.toInt(input);
            Deck deck = DeckList.getDeckList().get(deckIndex);
            testAllCardsInOrder(deck);
        } catch (NumberFormatException e) {
            System.out.println("Incorrect input format, make sure the description is a numeric.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("This deck doesn't exist.");
        }
    }

    /**
     * Goes through all the flashcards and stores the user's responses into answersResponse ArrayList.
     */
    public static void testAllCardsInOrder(Deck deck) {
        logger.setLevel(Level.WARNING);
        logger.log(Level.INFO, "starting test");

        for (FlashCard question : deck.cards) {
            logger.log(Level.INFO, "starting to test a new card");
            int questionNumber = deck.getCardIndex(question);
            ui.printDividerLine();
            ui.printQuestion(question, questionNumber);
            //get user's answer to the card shown(currently assume user inputs only his/her answer)
            //later version to include question number and parsing to allow for randomised testing
            logger.log(Level.INFO, "getting user's answer to the question");
            String userResponse = ui.getUserMessage();
            try {
                userResponse = TestParser.parseUserResponse(userResponse);
            } catch (FieldEmptyException e) {
                logger.log(Level.WARNING, "No user input");
                userResponse = "NO ANSWER GIVEN :(";
                printAnswerEmptyError();
            }
            logger.log(Level.INFO, "Saving answer");
            answersResponse.addAnswer(userResponse, questionNumber);
            assert !answersResponse.isEmpty();
            assert answersResponse.getSize() > 0;
            logger.log(Level.INFO, "Finished this card's testing");
        }

        ui.printDividerLine();
        logger.log(Level.INFO, "Finished test");
        //let user know testing is over
        ui.printTestOver();
        viewTestResult(deck);
    }

    /**
     * Prints results of test to system output.
     */
    private static void viewTestResult(Deck deck) {
        logger.setLevel(Level.WARNING);
        int score = 0;
        logger.log(Level.INFO, "starting test check");

        //there must be at least one response to start a test
        assert answersResponse.getSize() > 0;
        for (Answer response : answersResponse.getAnswerList()) {
            int responseNumber = answersResponse.getAnswerIndex(response);
            FlashCard question = deck.getCard(responseNumber);
            String userAnswer = answersResponse.getUserAnswer(responseNumber);
            ui.printDividerLine();
            //display front of card so that user can understand question
            ui.printQuestion(question, responseNumber);
            ui.printCorrectAnswer(question);
            ui.printUserAnswer(userAnswer);

            if (answersResponse.isUserAnswerCorrect(userAnswer, question)) {
                score++;
                question.incrementUserScore();
                printCorrectAnsMessage();
                logger.log(Level.INFO, "user answer is correct");
            } else {
                printWrongAnsMessage();
                logger.log(Level.INFO, "user answer is wrong");
            }
            question.incrementTotalScore();
        }
        ui.printDividerLine();
        int answersCount = answersResponse.getSize();
        assert score <= answersCount;
        System.out.println("Your scored " + score + " out of " + answersCount + " for this test.");
        System.out.println("That is " + score / answersCount * 100 + "%!");
        logger.log(Level.INFO, "all answers checked, score printed to system output");
    }

    /**
     * View overall result statistics of all tests and individual flashcards.
     * Invoked by the user command "stats".
     */
    public static void viewTestStatistics() {
        assert DeckList.getDeckList().size() > 0 : "deckList must not be empty";
        System.out.println("Listing total scores of flashcards for all tests");
        for (Deck deck : DeckList.getDeckList()) {
            for (FlashCard card : deck.cards) {
                card.viewFlashCard();
                System.out.println("Score: " + card.getUserScore() + "out of " + card.getTotalScore());
            }
            ui.printDividerLine();
        }
    }

    private static void printCorrectAnsMessage() {
        System.out.println("Well done! You got this question correct");
    }

    private static void printWrongAnsMessage() {
        System.out.println("You got this question wrong! Take note of the correct answer!");
    }

    private static void printAnswerEmptyError() {
        System.out.println("Remember to provide an answer next time! Don't give up!");
    }
}
