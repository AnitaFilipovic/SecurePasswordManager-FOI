package spm.storage;

import java.util.ArrayList;

public class PasswordStorageItem extends ArrayList<PasswordStorageData> {
    private String name;

    public PasswordStorageItem(String name) {
        this.name = name;
    }

    public PasswordStorageItem() {
        this("_item_");
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
