package peer;

/**
 * Command abstraction
 *
 * @author Gökçe Uludoğan
 */
public class Command {

    private CommandName commandName;

    /**
     * No parameter constructor of Command
     */

    public Command() {
    }

    /**
     * Constructor of Command
     *
     * @param commandName the typename of Command
     * @param amount      the amount in the Command
     */
    public Command(CommandName commandName, float amount) {
        this.commandName = commandName;
    }

    /**
     * Get command name from Command object
     *
     * @return commandName
     */
    public CommandName getCommandName() {
        return commandName;
    }

    enum CommandName {

        search, mysharedfiles, mydownloadfiles, exit, help
    }
}
