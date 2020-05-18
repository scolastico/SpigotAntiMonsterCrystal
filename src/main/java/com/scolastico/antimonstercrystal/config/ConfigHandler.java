package com.scolastico.antimonstercrystal.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigHandler {

    private Object _configObject;
    private final String _filename;
    private final File _file;
    private final Gson _gson;
    private boolean fresh = false;

    public ConfigHandler(Object configObject, String filename) throws Exception {

        _filename = filename;
        _file = new File(filename);
        _gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        if (_file.exists()) {
            _configObject = _gson.fromJson(new FileReader(_file), configObject.getClass());
        } else {
            fresh = true;
            writeStringToFile(_gson.toJson(configObject));
            _configObject = configObject;
        }

    }

    private void writeStringToFile(String string) throws IOException {
        Writer writer = new OutputStreamWriter(new FileOutputStream(_file.getPath()), StandardCharsets.UTF_8);
        writer.write(string);
        writer.close();
    }

    public void reloadConfigFile() throws Exception {
        _configObject = _gson.fromJson(new FileReader(_file), _configObject.getClass());
    }

    public Object getConfigObject() {
        return _configObject;
    }

    public void setConfigObject(Object configObject) {
        this._configObject = configObject;
    }

    public void saveConfigObject() throws IOException {
        writeStringToFile(_gson.toJson(_configObject));
    }

    public String getFilename() {
        return _filename;
    }

    public boolean isFresh() {
        return fresh;
    }

}