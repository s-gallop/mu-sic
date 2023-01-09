package ast;

import Midi.Note;

import java.util.List;

public class Program extends Node {
    private final SetTempo setTempo;
    private final List<Function> functions;
    private final List<Command> commands;

    public Program(SetTempo setTempo, List<Function> functions, List<Command> commands) {
        this.setTempo = setTempo;
        this.functions = functions;
        this.commands = commands;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public SetTempo getSetTempo() {
        return setTempo;
    }

    @Override
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return v.visit(context, this);
    }
}
