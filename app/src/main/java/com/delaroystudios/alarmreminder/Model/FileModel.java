package com.delaroystudios.alarmreminder.Model;

public class FileModel {
    String path;
    String name;

    public FileModel(String path, String name) {
        this.path = path;
        this.name = name;
    }
    public String getPath() {
        return path;
    }
    public String getName() {
        return name;
    }
}
