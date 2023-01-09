package ast;

import Midi.Note;

import java.util.List;

public class Chord extends Sound {
    private final String name;

    public Chord(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return null;
    }

    @Override
    public <C, T, T2> T2 accept2(C context, Visitor<C,T,T2> v) {
        return v.visit(context, this);
    }
}
