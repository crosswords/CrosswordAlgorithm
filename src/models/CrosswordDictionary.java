package models;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tomek on 29.04.14.
 */
public class CrosswordDictionary implements ICrosswordDictionary {

    private String[] filenames = new String[] {"datasets/1.csv"};
    private String[] filenames2 = new String[] {"datasets/2.csv"};
    private String[] filenames3 = new String[] {"datasets/3.csv"};

    private static Set<IEntry> entries;

    @Override
    public Set<IEntry> allEntries() {
        return getEntries();

    }

    public Set<IEntry> getEntries() {
        if(entries == null){
            loadEntries();
        }
        return entries;
    }

    private void loadEntries() {
        entries = new HashSet<IEntry>();
        for(String p: filenames){
            try {
                List<String> strings = Files.readAllLines(FileSystems.getDefault().getPath(p), Charset.defaultCharset());
                for(String line: strings){
                    if(line.matches(".*,.*")){
                        Entry entry = new Entry();
                        entry.setWord(line.split(",")[0]);
                        entry.setClues(
                                Arrays.asList(
                                        line.substring(entry.getWord().length()).split("\",\"")
                                )
                        );
                        entries.add(entry);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(String p: filenames2){
            try {
                List<String> strings = Files.readAllLines(FileSystems.getDefault().getPath(p), Charset.defaultCharset());
                for(String line: strings){
                    if(line.matches(".*,.*")){
                        Entry entry = new Entry();
                        entry.setWord(line.split(",")[0]);
                        entry.setClues(
                                Arrays.asList(
                                        line.substring(entry.getWord().length()).split("\",\"")
                                )
                        );
                        entries.add(entry);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(String p: filenames3){
            try {
                List<String> strings = Files.readAllLines(FileSystems.getDefault().getPath(p), Charset.defaultCharset());
                for(String line: strings){
                    if(line.matches(".*,.*")){
                        Entry entry = new Entry();
                        entry.setWord(line.split(",")[0]);
                        entry.setClues(
                                Arrays.asList(
                                        line.substring(entry.getWord().length()).split("\",\"")
                                )
                        );
                        entries.add(entry);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}