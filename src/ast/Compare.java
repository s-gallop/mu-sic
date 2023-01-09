package ast;

import Midi.Note;

import java.util.List;

public class Compare extends Node{
    private final String name;
    private final String comparator;
    private final int compareInt;

    public Compare(String name, String comparator, int compareInt) {
        this.name = name;
        this.comparator = comparator;
        this.compareInt = compareInt;
    }

    public int getCompareInt() {
        return compareInt;
    }

    public String getComparator() {
        return comparator;
    }

    public String getName() {
        return name;
    }

    @Override
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return v.visit(context, this);
    }
}
