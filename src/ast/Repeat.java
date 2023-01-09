package ast;

import Midi.Note;

import java.util.List;

public class Repeat extends Command {
    private final int itr;
    private final List<Command> commands;
    private final ItrDfn dfn;

    public Repeat(int itr, ItrDfn dfn, List<Command> cmds) {
        this.itr = itr;
        this.dfn = dfn;
        commands = cmds;
    }

    public int getNumIterations() {
        return itr;
    }

    public ItrDfn getIterationDfn() {
        return dfn;
    }

    public List<Command> getCommands() {
        return commands;
    }

    @Override
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return v.visit(context, this);
    }
}
