package com.temenos.responder.loader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

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
            Files.walkFileTree(new File(classpathResource.getFile()).toPath(),
                    new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                            filesToContents.put(path.getFileName().toString(), fetchScript(path));
                            return FileVisitResult.CONTINUE;
                        }
            });
        } else{
            throw new IOException("Directory "+root+" does not exist.");
        }
        if(filesToContents.isEmpty()){
            throw new IOException("Directory "+root+" is empty.");
        }
        return filesToContents;
    }

    private String fetchScript(String name) throws IOException {
        return fetchScript(new File(name).toPath());
    }

    private String fetchScript(Path path) throws IOException {
        return new String(Files.readAllBytes(path)).trim();
    }
}
