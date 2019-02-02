package com.herve.ocgames.mastermind;

import com.herve.ocgames.core.CodeChecker;
import com.herve.ocgames.core.GameCache;
import com.herve.ocgames.core.PropertyHelper;
import com.herve.ocgames.utils.Response;
import com.herve.ocgames.utils.StringTool;
import com.herve.ocgames.utils.UserInteraction;

public class CodeCheckerMasterMind extends CodeChecker {

    /*
     * Constructor
     */

    public CodeCheckerMasterMind(){
        super();
        this.debugVerbosity = 2 ;
    }

    /*
     * Partie Metier
     */

    @Override
    public Response generateEvaluation(String attempt, String secret) {
        if (GameCache.isFailed()) return null;
        // Initialize local variable
        String checkPresence = attempt;
        String checkPosition = attempt;
        String referencePresence = secret;
        String referencePosition = secret;
        String goodPositions = "";
        String presents = "";
        String result = "";
        String[][] langSubstitutes;
        Response computerResponse = new Response();
        Integer[] digitParameters = new Integer[]{this.digitsInGame,0,this.digitMaxRepeat};

        debugV3("error management with own exceptions"); // comment used for debug
        if (! StringTool.match(attempt, "^[0-" + (this.digitsInGame - 1) + "]{" + this.codeLength + "}$" ) ||
                ! StringTool.matchSpecificDigitRule(attempt, "digitMaxRepeat", digitParameters))
            invalidArgument("generateEvaluation","attempt");
        if (! StringTool.match(secret, "^[0-" + (this.digitsInGame - 1) + "]{" + this.codeLength + "}$" ))
            invalidArgument("generateEvaluation","secret");

        if (GameCache.isFailed()) return computerResponse;

        debugV3("The two strings are compared and return evaluation with symbols x and o"); // comment used for debug
        for (int i = 0; i < checkPosition.length(); i ++){
            if (checkPosition.charAt(i) == referencePosition.charAt(i)){
                goodPositions += "x";
                checkPresence = checkPresence.replaceFirst(String.valueOf(checkPosition.charAt(i)), "");
                referencePresence = referencePresence.replaceFirst(String.valueOf(checkPosition.charAt(i)), "");
            }
        }
        debugV2(lang("debug.mastermindGoodPosition") + goodPositions);
        for (int i = 0; i < checkPresence.length(); i ++){
            if (referencePresence.contains(String.valueOf(checkPresence.charAt(i)))) {
                presents += "o";
                referencePresence = referencePresence.replaceFirst(String.valueOf(checkPresence.charAt(i)), "");
            }
        }
        debugV2(lang("debug.mastermindWrongPosition") + presents);

        debugV3("Sort symbols in evaluation string"); // comment used for debug
        result = goodPositions + presents;

        debugV3("Set response properties and return response"); // comment used for debug
        computerResponse.appendValue("evaluation", result);
        computerResponse.appendValue("goodPosition", "" + goodPositions);
        computerResponse.appendValue("presents", "" + presents);
        langSubstitutes = new String[][] {{"VAR_RESULT_EVALUATION",result},
                {"VAR_RIGHT_POSITION", "" + goodPositions.length()},
                {"VAR_WRONG_POSITION", "" + presents.length()}};
        computerResponse.setMessage(lang("mastermind.resultComputerEvaluation", langSubstitutes));
        computerResponse.setSuccess(StringTool.match(result, "^x{"+ this.codeLength + "}$"));
        return computerResponse;
    }

    @Override
    public Response askPlayerEvaluation() {
        if (GameCache.isFailed()) return null;

        String color = "reset";
        if (GameCache.isDefender() && GameCache.isChallenger()) color = PropertyHelper.config("color.defender");
        String [][] langSubstitutes = new String[][] {{"VAR_PLAYER_SECRET", GameCache.getPlayerCode()}};
        // Initialize local variable
        String question = lang("mastermind.playerEvaluation", langSubstitutes);
        String codePattern = "^[xo-]{0," + this.codeLength + "}$";
        Response response = new Response();

        debugV3("Get player input with UserInteraction scanner"); // comment used for degug
        String evaluation = UserInteraction.promptInput(question, codePattern, color, false);
        evaluation = evaluation.replaceAll("-","");

        debugV3("Set response properties and return response"); // comment used for degug
        response.appendValue("evaluation", evaluation);
        return response;
    }

    @Override
    public Response askRefereeControl(String submittedEvaluation, String refereeEvaluation) {
        //if (GameCache.isFailed()) return null;
        // Initialize local variable
        Response response = new Response();
        String playerString ;
        String computerString ;
        String message = "";
        int attempts = Integer.parseInt(PropertyHelper.config("game.attempts"));
        boolean isSimilarEvaluation = true;
        Integer[] digitParameters = new Integer[]{this.digitsInGame,0,this.digitMaxRepeat};

        debugV3("error management if arguments are wrong"); // comment used for debug
        if (! StringTool.match(submittedEvaluation, "^[ox]{0," + this.codeLength + "}$" ) ||
                ! StringTool.matchSpecificDigitRule(submittedEvaluation, "digitMaxRepeat", digitParameters))
            invalidArgument("askRefereeControl","submittedEvaluation");
        if (! StringTool.match(refereeEvaluation, "^[ox]{0," + this.codeLength + "}$" ))
            invalidArgument("askRefereeControl","refereeEvaluation");

        if (GameCache.isFailed()) return response;

        debugV3("compare the two evalutions character by character to prevent different way of writing"); // comment used for debug
        playerString = submittedEvaluation.replaceAll("o", "");
        computerString = refereeEvaluation.replaceAll("o","");
        String goodPositions = "" + computerString.length();
        if (! playerString.contentEquals(computerString)) isSimilarEvaluation= false;
        playerString = submittedEvaluation.replaceAll("x", "");
        computerString = refereeEvaluation.replaceAll("x","");
        String presents = "" + computerString.length();
        if (! playerString.contentEquals(computerString)) isSimilarEvaluation= false;
        String[][] langSubstitutes = new String[][] {{"VAR_PLAYER_EVALUATION", submittedEvaluation},
                {"VAR_REFEREE_EVALUATION", refereeEvaluation},
                {"VAR_RIGHT_POSITION", goodPositions},
                {"VAR_WRONG_POSITION", presents}};

        debugV3("following result, set message and success in response and return response"); // comment used for debug
        if (! isSimilarEvaluation) {
            debugV3("Player didn't give the right evaluation"); // comment used for debug
            message += "%n" + lang("game.refereeWrongEvaluation", langSubstitutes);
            response.setSuccess(false);
        } else {
            boolean computerFindCode = StringTool.match(refereeEvaluation,"^x{" + this.codeLength + "}$");
            if ( computerFindCode ) {
                debugV3("Referee confirm that computer find the secret code"); // comment used for debug
                message += "%n"  + lang("game.computerFoundCode");
                response.setSuccess(false);
            } else if ( ! computerFindCode && GameCache.turn() == (attempts - 1)) {
                message += "%n" + lang("game.computerDidntFindCode");
                GameCache.wins();
            } else {
                message += lang("game.refereeAgree", langSubstitutes);
            }
        }
        response.setMessage(message);
        return response;
    }

}
