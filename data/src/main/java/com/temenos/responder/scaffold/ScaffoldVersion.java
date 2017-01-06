package com.temenos.responder.scaffold;

import com.temenos.responder.entity.runtime.Type;

/**
 * Created by Douglas Groves on 06/01/2017.
 */
public class ScaffoldVersion implements Scaffold {
    public static final String VERSION_NUMBER = "versionNumber";
    public static final Type VERSION_NUMBER_TYPE = Type.STRING;
    public static final String BUILD_DATE = "buildDate";
    public static final Type BUILD_DATE_TYPE = Type.STRING;
    public static final String BLAME = "blameThisPerson";
    public static final Type BLAME_TYPE = Type.STRING;
}
