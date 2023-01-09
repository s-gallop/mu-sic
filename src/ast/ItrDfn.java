package ast;

import Midi.Note;

import java.util.List;

public class ItrDfn extends Node {
    private final String name;

    public ItrDfn(String name) {
         this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return v.visit(context, this);
    }
}
