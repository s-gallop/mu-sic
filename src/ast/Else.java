package ast;

import Midi.Note;

import java.util.List;

public class Else extends Node{

    private List<Command> commands;

    public Else( List<Command> commands) {
        this.commands = commands;
    }


    public List<Command> getCommands() {
        return commands;
    }

    @Override
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return v.visit(context,this);
    }
}
