package ast;

import Midi.Note;

import java.util.List;

public class Conditional extends Command {

    private If ifStatement;
    private List<Elif> elseIfStatements;
    private Else elseStatement;

    public Conditional(If ifStatement, List<Elif> elseIfStatements, Else elseStatement) {
        this.ifStatement = ifStatement;
        this.elseIfStatements = elseIfStatements;
        this.elseStatement = elseStatement;
    }

    public List<Elif> getElseIfStatements() {
        return elseIfStatements;
    }

    public Else getElseStatement() {
        return elseStatement;
    }

    public If getIfStatement() {
        return ifStatement;
    }


    @Override
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return v.visit(context, this);
    }


}
