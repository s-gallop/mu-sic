package ast;

import Midi.Note;

import java.util.List;

public class Shift extends Command {
    private Chord chord;
    private String direction;
    private double steps;

    public Shift(Chord chord, String direction, double steps){
        this.chord = chord;
        this.direction = direction;
        this.steps = steps;
    }
    public String getName() {
        return chord.getName();
    }

    public double getSteps() {
        return steps;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return v.visit(context, this);
    }
}
