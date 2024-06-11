package spm.storage;

import java.util.ArrayList;

public class PasswordStorageRoot extends ArrayList<PasswordStorageFolder> {
    @Override
    public String toString() {
        return "root";
    }
}
