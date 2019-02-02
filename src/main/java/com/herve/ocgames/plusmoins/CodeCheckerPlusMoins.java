package com.herve.ocgames.plusmoins;

import com.herve.ocgames.core.CodeChecker;
import com.herve.ocgames.core.GameCache;
import com.herve.ocgames.core.PropertyHelper;
import com.herve.ocgames.utils.Response;
import com.herve.ocgames.utils.StringTool;
import com.herve.ocgames.utils.UserInteraction;

public class CodeCheckerPlusMoins extends CodeChecker {


    public CodeCheckerPlusMoins(){
        super();
        this.debugVerbosity = 2 ;
    }

    @Override
    public Response generateEvaluation(String attempt, String secret) {
        if (GameCache.isFailed()) return null;
        // Initialize local variable
        int testedNum;
        int expectedNum;
        String result = "";
        Response computerResponse = new Response();

        debugV3("error management if arguments are wrong"); // comment used for debug
        if (! StringTool.match(attempt, "^[0-" + (this.digitsInGame - 1) + "]{" + this.codeLength + "}$" ))
            invalidArgument("generateEvaluation","attempt");
        if (! StringTool.match(secret, "^[0-" + (this.digitsInGame - 1) + "]{" + this.codeLength + "}$" ))
            invalidArgument("generateEvaluation","secret");

        if (GameCache.isFailed()) return computerResponse;

        debugV3("The two strings are compared and return evaluation with symbols -+="); // comment used for debug
        for (int i=0 ; i < this.codeLength; i++) {
            debugV4("Compare one by one each digit from secret code with input"); // comment used for debug
            testedNum = (int) attempt.charAt(i);
            expectedNum = (int) secret.charAt(i);
            String compare = ((testedNum == expectedNum) ? "=" : ((testedNum > expectedNum) ? "-" : "+"));
            result += compare;
        }
        debugV2(lang("debug.plusmoinsComputerEvaluation") + result);

        debugV3("We return response after setting message, isSuccess and in values <\"evaluation\", result>");
        computerResponse.appendValue("evaluation", result);
        String[][] langSubstitutes = new String[][] {{"VAR_RESULT_EVALUATION",result}};
        computerResponse.setMessage(lang("game.resultComputerEvaluation", langSubstitutes));
        computerResponse.setSuccess(StringTool.match(result, "^={" + this.codeLength + "}$"));
        return computerResponse;
    }

    @Override
    public Response askPlayerEvaluation(){
        if (GameCache.isFailed()) return null;
        // Initialize local variable
        String color = "reset";
        if (GameCache.isChallenger() && GameCache.isDefender()) color = PropertyHelper.config("color.defender");
        String [][] langSubstitutes = new String[][] {{"VAR_PLAYER_SECRET", GameCache.getPlayerCode()},{"VAR_EQUAL","="}};
        String question = lang("plusmoins.playerEvaluation", langSubstitutes);
        String codePattern = "^[-\\+=]{" + this.codeLength + "}$";
        Response response = new Response();

        debugV3("Get player input with UserInteraction scanner"); // comment used for degug
        String evaluation = UserInteraction.promptInput(question, codePattern, color, false);

        debugV3("Set response properties and return response"); // comment used for degug
        response.appendValue("evaluation", evaluation);
        return response;
    }

    @Override
    public Response askRefereeControl(String submittedEvaluation, String refereeEvaluation){
        if (GameCache.isFailed()) return null;
        // Initialize local variable
        Response response = new Response();
        String message = "";
        int attempts = Integer.parseInt(PropertyHelper.config("game.attempts"));
        String[][] langSubstitutes = new String[][] {{"VAR_PLAYER_EVALUATION", submittedEvaluation},
                {"VAR_REFEREE_EVALUATION", refereeEvaluation}};

        debugV3("error management if arguments are wrong"); // comment used for debug
        if (! StringTool.match(submittedEvaluation, "^[-=+]{" + this.codeLength + "}$" ))
            invalidArgument("askRefereeControl","submittedEvaluation");
        if (! StringTool.match(refereeEvaluation, "^[-=+]{" + this.codeLength + "}$" ))
            invalidArgument("askRefereeControl","refereeEvaluation");

        if (GameCache.isFailed()) return response;


        debugV3("following result, set message and success in response and return response"); // comment used for debug
        if (! submittedEvaluation.contentEquals(refereeEvaluation)) {
            debugV3("Player didn't give the right evaluation"); // comment used for debug
            message += lang("game.refereeWrongEvaluation",langSubstitutes);
            response.setSuccess(false);
        } else {
            boolean computerFindCode = StringTool.match(refereeEvaluation,"^={" + this.codeLength + "}$");
            if ( computerFindCode ) {
                debugV3("Referee confirm that computer find the secret code"); // comment used for debug
                message += "%n"  + lang("game.computerFoundCode");
                response.setSuccess(false);
            }
            if ( ! computerFindCode && GameCache.turn() == (attempts - 1)){
                message += "%n" + lang("game.computerDidntFindCode");
                GameCache.wins();
            }
        }
        response.setMessage(message);
        return response;

    }

}
