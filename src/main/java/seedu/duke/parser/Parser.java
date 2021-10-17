package seedu.duke.parser;

import seedu.duke.CategoryList;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Deals with the parsing of user input at the command line.
 */
public class Parser {

    private static final Logger logger = Logger.getLogger(Parser.class.getName());

    /**
     * Parses user input at the command line and invokes the necessary follow up actions.
     */
    public static void parseCommand(String input) {
        logger.log(Level.INFO, "new user input detected");
        logger.setLevel(Level.WARNING);
        String command = getCommand(input);

        logger.log(Level.INFO, "new user input detected");

        switch (command) {
        case "add":
            String addInput = removeCommandWord(input, command.length());
            CategoryList.prepareToAddCardToDeck(addInput);
            logger.log(Level.INFO, "add command parsed and executed");
            break;
        case "addcat":
            String addcatInput = removeCommandWord(input, command.length());
            CategoryList.prepareToAddCategory(addcatInput);
            break;
        case "viewcat":
            CategoryList.viewCategories();
            break;
        case "delete":
            String deleteInput = removeCommandWord(input, command.length());
            CategoryList.prepareToDeleteCardFromDeck(deleteInput);
            logger.log(Level.INFO, "delete command parsed and executed");
            break;
        case "view":
            String viewInput = removeCommandWord(input, command.length());
            CategoryList.viewOneCategory(viewInput);
            logger.log(Level.INFO, "view command parsed and executed");
            break;
        case "test":
            String testInput = removeCommandWord(input, command.length());
            CategoryList.testCategory(testInput);
            logger.log(Level.INFO, "test command parsed and executed");
            break;
        case "editcard": //editcard /cat <cat index> /card <card index> /side <side> /input <input>
            String editCardInput = removeCommandWord(input, command.length());
            String[] parsedEditCardArgs = parseEditCardCommand(editCardInput);
            CategoryList.editCard(parsedEditCardArgs);
            logger.log(Level.INFO, "editcard command parsed and executed");
            break;
        case "editcat": //editcat /cat <cat index> /input <input>
            String editCatInput = removeCommandWord(input, command.length());
            String[] parsedEditCatArgs = parseEditCatCommand(editCatInput);
            CategoryList.editCat(parsedEditCatArgs);
            logger.log(Level.INFO, "editcat command parsed and executed");
            break;
        case "bye":
            logger.log(Level.INFO, "bye command parsed and executed, program will terminate");
            break;
        default:
            System.out.println("\tThat's not a command.");
            logger.log(Level.INFO, "command was unrecognised and could not be parsed");
        }
    }

    public static String getCommand(String line) {
        return line.trim().split(" ")[0];
    }

    /**
     * Returns all contents of the input after the command word.
     *
     * @param input user's input
     * @return description of card
     */
    public static String removeCommandWord(String input, int index) {
        assert input.length() > 0 : "input string should not be empty, at least have command word";
        return input.substring(index).trim();
    }

    public static String[] parseEditCardCommand(String input) {
        String trimmedInput = input.trim();
        String[] args = trimmedInput.split(" ",8);
        if(!args[0].equalsIgnoreCase("/cat") | !args[2].equalsIgnoreCase("/card") |
                !args[4].equalsIgnoreCase("/side") | ! args[6].equalsIgnoreCase("/input")) {
            System.out.println("Incorrect editcat command! Format should be\n" +
                    "editcard /cat <category index> /card <card index> /side <side> /input <input>");
        }
        int catIndex = Integer.parseInt(args[1]);
        int cardIndex = Integer.parseInt(args[3]);
        if(!(catIndex > 0 && catIndex <= CategoryList.getDecksSize())){
            System.out.println("Incorrect index for category!");
        }
        if(!(cardIndex > 0 && cardIndex <= CategoryList.getDeck(catIndex - 1).getManager().getCardsSize())){
            System.out.println("Incorrect index for Deck!");
        }
        if(!(args[5].equalsIgnoreCase("front") | args[5].equalsIgnoreCase("back"))){
            System.out.println("What side is this? Its only either front or back");
        }
        String[] editArgs = { args[1], args[3], args[5], args[7]};
        return editArgs;
    }

    public static String[] parseEditCatCommand(String input) {
        String trimmedInput = input.trim();
        String[] args = trimmedInput.split(" ",4);
        if(!args[0].equalsIgnoreCase("/cat") | !args[2].equalsIgnoreCase("/input")) {
            System.out.println("Incorrect editcat command! Format should be\n" +
                    "editcat /cat <category index> /input <input>");
        }
        int catIndex = Integer.parseInt(args[1]);
        if(!(catIndex > 0 && catIndex <= CategoryList.getDecksSize())){
            System.out.println("Incorrect index for category!");
        }
        String[] editArgs = {args[1], args[3]};
        return editArgs;
    }



}
