package ast;

import Midi.Note;

import java.util.List;

public class Wait extends Command{

    private final double length;

    public Wait(double length){
        this.length = length;
    }

    public double getlength(){
        return length;
    }
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return v.visit(context, this);
    }
}
