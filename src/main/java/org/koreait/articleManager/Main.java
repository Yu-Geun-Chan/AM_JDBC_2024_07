package org.koreait.articleManager;

import org.koreait.exception.SQLErrorException;


public class Main {
    public static void main(String[] args) {

        Container.init();

        try {
            new App().run();
        } catch (SQLErrorException e) {
            System.err.println("e.getMessage : " + e.getMessage());
            e.getOrigin().printStackTrace();
        }
    }
}