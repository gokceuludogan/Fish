package client;

/**
 * The abstraction for command
 *
 * @author gokce
 */
public class Command {

    private String userName;
    private float amount;
    private CommandName commandName;

    static enum CommandName {

        list, share, unshare, get, changefolder, exit, help, currentfolder, search;
    }

    ;

    /**
     * Default constructor
     */
    public Command() {
    }

    /**
     * Gets username
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets the amount in the command
     *
     * @return
     */
    public float getAmount() {
        return amount;
    }

    /**
     * Gets commandName
     *
     * @return
     */
    public CommandName getCommandName() {
        return commandName;
    }

    /**
     * Constructor with the commandName and amount
     *
     * @param commandName
     * @param amount
     */
    public Command(CommandName commandName, float amount) {
        this.commandName = commandName;
        this.amount = amount;
    }
}