package com.temenos.responder.scaffold;

import com.temenos.responder.entity.runtime.Type;

/**
 * This {@link Scaffold scaffold} validates response payloads returned by the version API.
 *
 * Created by Douglas Groves on 06/01/2017.
 */
public class ScaffoldVersion implements Scaffold {
    /**
     * The version number of the deployed application.
     */
    public static final String VERSION_NUMBER = "versionNumber";
    public static final Type VERSION_NUMBER_TYPE = Type.STRING;
    /**
     * The date this application was built.
     */
    public static final String BUILD_DATE = "buildDate";
    public static final Type BUILD_DATE_TYPE = Type.STRING;
    /**
     * The person responsible for maintaining this build.
     */
    public static final String BLAME = "blameThisPerson";
    public static final Type BLAME_TYPE = Type.STRING;
}
