package seedu.cardli.commands.deck;

import seedu.cardli.commands.Command;
import seedu.cardli.commands.CommandResult;
import seedu.cardli.exceptions.FieldEmptyException;
import seedu.cardli.flashcard.Deck;
import seedu.cardli.parser.deck.DeleteCardParser;

public class DeleteCardCommand extends Command {

    private static final String FIELD_EMPTY_ERROR_MESSAGE = "You cannot leave any field empty! "
            + "Format should be\n delete <word/phrase/index>";

    private DeleteCardParser parser;
    private Deck deck;

    public DeleteCardCommand(String arguments, Deck deck) {
        super("DeleteCardCommand", arguments);
        this.parser = new DeleteCardParser();
        this.deck = deck;
    }

    @Override
    public CommandResult execute() {
        CommandResult result;

        try {
            String[] parameters = parser.parseArguments(super.arguments);
            String enterInput = parameters[0];
            if (enterInput.isEmpty()) {
                throw new FieldEmptyException(FIELD_EMPTY_ERROR_MESSAGE);
            }
            result = new CommandResult(deck.prepareToDeleteFlashCard(enterInput));
        } catch (FieldEmptyException e) {
            result = new CommandResult(e.getMessage());
        }
        return result;
    }
}