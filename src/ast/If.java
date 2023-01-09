package ast;

import Midi.Note;

import java.util.List;

public class If extends Node{
    private Compare compare;
    private List<Command> commands;

    public If(Compare compare, List<Command> commands) {
        this.compare = compare;
        this.commands = commands;
    }

    public Compare getCompare() {
        return compare;
    }

    public List<Command> getCommands() {
        return commands;
    }

    @Override
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return v.visit(context,this);
    }
}
