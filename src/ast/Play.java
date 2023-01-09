package ast;

import Midi.Note;

import java.util.ArrayList;
import java.util.List;

public class Play extends Command {
    private final double length;
    private final Set set;
    private final List<Then> thens;

    public Play(double length, Set set, List<Then> thens) {
        this.length = length;
        this.set = set;
        this.thens = thens;
    }

    public double getLength() {
        return length;
    }

    public Set getSet() {
        return set;
    }

    public List<Then> getThens() {
        return thens;
    }

    @Override
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return v.visit(context, this);
    }
}
