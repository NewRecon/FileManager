package com.example.filemanager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FileInfo {
    private enum FileType{
        FILE("F"),
        DIRECTORY("D");

        private String name;
        FileType(String string){
            this.name = string;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private FileType type;
    private String name;
    private long size;
    private LocalDateTime lastUpdate;

    public FileInfo(Path path){
        try {
            name = path.getFileName().toString();
            size = Files.size(path);
            type = Files.isDirectory(path)?FileType.DIRECTORY:FileType.FILE;
            lastUpdate = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneOffset.ofHours(0));
        } catch (IOException e) {
            throw new RuntimeException("File not found");
        }

    }

    public String getType() {
        return type.getName();
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
