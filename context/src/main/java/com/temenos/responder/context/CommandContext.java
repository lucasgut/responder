package com.temenos.responder.context;

import java.util.List;
import java.util.Map;

/**
 * A CommandContext is a {@link Context context} that contains a map of attributes and two parameters used
 * during {@link com.temenos.responder.commands.Command command} processing.
 *
 * @author Douglas Groves
 */
public interface CommandContext extends Context{
    List<String> from();
    void from(List<String> params);
    String into();
    void into(String param);
    int getResponseCode();
    void setResponseCode(int code);
}
