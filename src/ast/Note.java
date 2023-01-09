package ast;

import java.util.ArrayList;
import java.util.List;

public class Note extends Sound {
    private final String note;
    private final int octave;
    private final String accidental;

    public Note(String note, int octave, String accidental) {
        this.note = note;
        this.octave = octave;
        this.accidental = accidental;
    }

    public String getNote() {
        return note;
    }

    public int getOctave() {
        return octave;
    }

    public String getAccidental() {
        return accidental;
    }

    @Override
    public <C, T> T accept(C context, Visitor<C, T, List<Midi.Note>> v) {
        return null;
    }

    @Override
    public <C, T, T2> T2 accept2(C context, Visitor<C,T,T2> v) {
        return v.visit(context, this);
    }
}
