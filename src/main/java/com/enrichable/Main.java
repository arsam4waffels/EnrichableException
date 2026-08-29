package com.enrichable;

import com.enrichable.config.ErrorLevel;

public class Main {
    public static void main3() {
        throw new EnrichableException(
                "main3 Error Title",
                "CODE-11",
                "What happened",
                ErrorLevel.CRITICAL,
                null
        );
    }
    public static void main2() {
        try {
            main3();
        } catch (EnrichableException e) {
            e.addInformation(
                    "main2 Information Title",
                    "CODE-22",
                    "More information on what happened",
                    ErrorLevel.CRITICAL
            ).addMetaData("Key1", "Value2").addMetaData("Key2", "Value2");
            throw e;
        }
    }
    public static void main1() {
        try {
            main2();
        } catch (EnrichableException e) {
            e.addInformation(
                    "main1 Information Title",
                    "CODE-33",
                    "More information on what happened",
                    ErrorLevel.CRITICAL
            ).addMetaData("Key3", "Value3").addMetaData("Key4", "Value4");
            throw e;
        }
    }
    public static void main(String[] args) {
        try {
            main1();
        } catch (EnrichableException e) {
            e.addMetaData("WeAre", "Home").writeLog();
        }
    }
}