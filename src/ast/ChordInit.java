package ast;

import Midi.Note;

import java.util.List;

public class ChordInit extends Command{
    private final String name;
    private final NoteSet set;

    public ChordInit(String name, NoteSet set) {
        this.name = name;
        this.set = set;
    }

    public String getName() {
        return name;
    }

    public NoteSet getSet() {
        return set;
    }

    @Override
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return v.visit(context, this);
    }
}
