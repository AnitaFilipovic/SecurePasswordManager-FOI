package spm.storage;

import java.util.ArrayList;

public class PasswordStorageFolder extends ArrayList<PasswordStorageItem> {
    private String name;

    public PasswordStorageFolder(String name) {
        this.name = name;
    }

    public PasswordStorageFolder() {
        this("_folder_");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
