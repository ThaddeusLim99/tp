package seedu.duke.parser.system;

import seedu.duke.parser.CommandArgumentParser;

public class EnterDeckParser implements CommandArgumentParser {
    public EnterDeckParser() {
    }

    @Override
    public String[] parseArguments(String arguments) {
        String[] parameters = new String[1];
        String deckName = arguments;
        parameters[0] = deckName;
        return parameters;
    }
}