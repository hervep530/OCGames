package com.herve.ocgames.core.interfaces;

import com.herve.ocgames.utils.Response;

public interface CodeGeneratorInterface {


    /**
     * Generate secret code for computer before beginning challenger game.
     */
    Response generateRandom();

    /**
     * Ask Player secret code before beginning defender game
     * @return Response object (contains return code, error and applicative messages and a map<String,Object>
     */
    Response askPlayerSecretCode();

    /**
     * Generate computer attempt in defender mode
     * @return Response object (contains return code, error and applicative messages and a map<String,Object>
     */
    Response generateComputerAttempt();

    /**
     * Ask Player attempt in challenger mode
     * @return  Response object (contains return code, error and applicative messages and a map<String,Object>
     */
    Response askPlayerCodeAttempt();

}
