package ast;

import Midi.Note;

import java.util.List;

public class Function extends Node {
    private String name;
    private List<Command> commands;

    public Function(String name, List<Command> commands) {
        this.name = name;
        this.commands = commands;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public String getName() {
        return name;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return v.visit(context, this);
    }
}
