package com.temenos.responder.scaffold;

/**
 * Utility class for handling Scaffold classes.
 *
 * Created by dgroves on 26/01/2017.
 */
public class Scaffolds {
    private Scaffolds(){}

    public static String fromArray(String fieldSpec, Object... indices){
        return String.format(fieldSpec, indices);
    }
}
