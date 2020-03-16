package seedu.duke.commands;

import seedu.duke.Profile;
import seedu.duke.exceptions.InvalidFormatException;
import seedu.duke.parser.Parser;
import seedu.duke.ui.UI;

public class SetWeightGoalCommand extends Command {

    private static final int ARGUMENTS_REQUIRED = 1;
    private double weightGoal;

    /**
     * Constructs the Command object.
     *
     * @param command the command prompt entered by the user.
     */

    public SetWeightGoalCommand(String command, String description)
            throws InvalidFormatException, NumberFormatException {
        super(command);
        String[] descriptionArray = Parser.parseDescription(description, ARGUMENTS_REQUIRED);
        this.weightGoal = Double.parseDouble(descriptionArray[0]);
    }

    @Override
    public void execute(Profile profile, UI ui) {
        profile.setWeightGoal(this.weightGoal);
        System.out.println(String.format("Your weight goal has been changed to %.2f", profile.getWeightGoal()));
    }
}
