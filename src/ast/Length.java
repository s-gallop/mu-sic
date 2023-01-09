package ast;

import Midi.Note;

import java.util.List;

public class Length extends Node {
    private final double length;

    public Length(double length) {
        this.length = length;
    }

    public double getLength() {
        return length;
    }

    @Override
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return v.visit(context, this);
    }
}
