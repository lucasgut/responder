package com.temenos.responder.loader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Douglas Groves on 07/12/2016.
 */
public interface ScriptLoader {
    String load(String name) throws IOException;
    Map<String, String> loadAll(String root) throws IOException;
}
