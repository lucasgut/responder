package com.temenos.responder.context;

import java.util.List;
import java.util.Map;

/**
 * Created by Douglas Groves on 04/01/2017.
 */
public interface CommandContext extends Context{
    String getResponseCode();
    void setResponseCode(String code);
}
