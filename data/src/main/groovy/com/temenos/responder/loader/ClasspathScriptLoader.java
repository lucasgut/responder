package com.temenos.responder.loader;

import java.io.*;
import java.net.URL;
import java.nio.CharBuffer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Douglas Groves on 08/12/2016.
 */
public class ClasspathScriptLoader implements ScriptLoader {

    private final String root;

    public ClasspathScriptLoader(String root){
        this.root = root;
    }

    @Override
    public String load(String name) throws IOException {
        URL resource = getClass().getClassLoader().getResource(name);
        if(resource == null){
            throw new IOException("Resource "+name+" does not exist.");
        }else{
            return fetchScript(resource.getFile());
        }
    }

    @Override
    public Map<String, String> loadAll() throws IOException {
        Map<String, String> filesToContents = new HashMap<>();
        URL classpathResource = getClass().getClassLoader().getResource(root);
        if(classpathResource != null) {
            walkFileStructure(new File(classpathResource.getFile()), filesToContents);
        }else{
            throw new IOException("Directory "+root+" does not exist.");
        }
        if(filesToContents.isEmpty()){
            throw new IOException("Directory "+root+" is empty.");
        }
        return filesToContents;
    }

    private void walkFileStructure(File file, Map<String, String> filesToContents) throws IOException {
        if(file == null || filesToContents == null) {
            return;
        }else if(file.isDirectory()) {
            for(File current : file.listFiles()) {
                walkFileStructure(current, filesToContents);
            }
        }else{
            filesToContents.put(file.getName(), fetchScript(file.getAbsolutePath()));
        }
    }

    private String fetchScript(String name) throws IOException {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new FileReader(name))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString().trim();
    }
}
