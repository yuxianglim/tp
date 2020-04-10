package seedu.dietmanager.logic.commands.recipe;

import seedu.dietmanager.commons.core.MessageBank;
import seedu.dietmanager.commons.exceptions.InvalidFormatException;
import seedu.dietmanager.commons.exceptions.NegativeNumberException;
import seedu.dietmanager.logic.Result;
import seedu.dietmanager.logic.commands.Command;
import seedu.dietmanager.logic.parser.DescriptionParser;
import seedu.dietmanager.model.Profile;
import seedu.dietmanager.model.RecipeManager;
import seedu.dietmanager.ui.UI;

public class BuildNewRecipeCommand extends Command {
    private static final int ARGUMENTS_REQUIRED = 2;

    private boolean noDescription;
    private boolean isInvalidFormat;
    private boolean maxNumOverflow;
    private boolean noProfileFound;

    private String activityLevel;
    private int maxFoodNum;

    /**
     * Constructs the command object.
     *
     * @param command     the command prompt entered by the user.
     * @param description the description of the command.
     * @throws InvalidFormatException if the command doesn't contain correct number of parameters.
     */

    public BuildNewRecipeCommand(String command, String description) throws InvalidFormatException {
        super(command);
        this.noDescription = false;
        this.isInvalidFormat = false;
        this.noProfileFound = false;

        try {
            String[] descriptionArray = DescriptionParser.parseDescription(description, ARGUMENTS_REQUIRED);

            this.maxFoodNum = Integer.parseInt(descriptionArray[0].trim());
            this.activityLevel = descriptionArray[1].trim().toLowerCase();

            switch (activityLevel) {
            case "low":
            case "high":
            case "moderate":
                break;
            default:
                isInvalidFormat = true;
                break;
            }

            if (maxFoodNum <= 0) {
                throw new NegativeNumberException();
            }

        } catch (NullPointerException e) {
            this.noDescription = true;
        } catch (NumberFormatException | NegativeNumberException e) {
            this.isInvalidFormat = true;
        }
    }

    @Override
    public Result execute(Profile profile, UI ui) {
        if (!profile.isProfileExist()) {
            noProfileFound = true;
        }
        if (!noDescription && !isInvalidFormat & !noProfileFound) {
            RecipeManager manager = RecipeManager.getInstance();
            maxNumOverflow = manager.buildRecipe(profile, maxFoodNum, activityLevel);
        }
        Result result = getResult(profile);
        return result;
    }

    @Override
    public Result getResult(Profile profile) {
        this.resultString = "";

        if (noDescription) {
            this.resultString = MessageBank.NO_DESCRIPTION_MESSAGE;
        } else if (noProfileFound) {
            this.resultString = MessageBank.INVALID_PROFILE_MESSAGE;
        } else if (isInvalidFormat) {
            this.resultString = "You have given wrong format for parameters!!!\n"
                    + "First parameter is maximum food types, need to provide an integer.\n"
                    + "Second parameter is activity level, choose from -- low/moderate/high.";
        } else {
            if (maxNumOverflow) {
                this.resultString = "We support at most 3 kinds of food in a meal, "
                        + "otherwise it's easy to overtake calories and not good for your health!\n\n";
            }
            this.resultString += RecipeManager.getInstance().getRecipe();
        }
        return new Result(this.resultString);
    }
}
