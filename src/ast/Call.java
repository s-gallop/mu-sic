package ast;

import Midi.Note;

import java.util.List;

public class Call extends Command {
    private final String name;

    public Call(String name) {
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
