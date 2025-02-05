package seedu.cardli.commands.system;

import seedu.cardli.commands.Command;
import seedu.cardli.commands.CommandResult;
import seedu.cardli.exceptions.FieldEmptyException;
import seedu.cardli.flashcard.DeckManager;
import seedu.cardli.parser.system.FindCardsParser;

public class FindCardsCommand extends Command {

    private FindCardsParser parser;
    private DeckManager deckManager;


    public FindCardsCommand(String arguments, DeckManager deckManager) {
        super("FindCardsCommand", arguments);
        this.deckManager = deckManager;
        this.parser = new FindCardsParser();
    }

    @Override
    public CommandResult execute() {
        CommandResult result;
        try {
            String[] parameters = parser.parseArguments(super.arguments);
            String findInput = parameters[0];
            if (findInput.isEmpty()) {
                throw new FieldEmptyException("You did not input a search term.");
            }
            result = new CommandResult(deckManager.findCards(findInput));
        } catch (FieldEmptyException e) {
            result = new CommandResult(e.getMessage());
        }
        return result;
    }

}
