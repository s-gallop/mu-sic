package ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChordTable {
    private List<Map<String, Set>> chordTable;

    public ChordTable() {
        chordTable = new ArrayList<>();
        addTable();
    }

    public void addTable() {
        chordTable.add(new HashMap<>());
    }

    public void deleteTable() {
        chordTable.remove(chordTable.size() - 1);
    }

    public Set getChord(String name) {
        for (int i = chordTable.size() - 1; i >= 0; i--) {
            if (chordTable.get(i).containsKey(name)) return chordTable.get(i).get(name);
        }
        return null;
    }

    public void addChord(String name, Set set) {
        chordTable.get(chordTable.size() - 1).put(name, set);
    }
}
