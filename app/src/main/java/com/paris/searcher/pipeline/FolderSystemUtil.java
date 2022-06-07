package com.paris.searcher.pipeline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FolderSystemUtil implements Handler<String, List<String>> {
    private final List<String> listStr;

    public FolderSystemUtil() {
        this.listStr = new ArrayList<>();
    }

    @Override
    public List<String> process(String folder) {
        final File indexableDirectory = new File(folder);
        return listFilesForFolder(indexableDirectory);
    }

    //https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
    private List<String> listFilesForFolder(final File indexableDirectory) {
        for (final File fileEntry : Objects.requireNonNull(indexableDirectory.listFiles())) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                listStr.add(indexableDirectory.getAbsoluteFile().getAbsolutePath().concat("/").concat(fileEntry.getName()));
            }
        }
        return listStr;
    }
}
