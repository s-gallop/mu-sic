package ast;

import Midi.Note;

import java.util.List;

public class SetTempo extends Node{
    private int tempo;
    public SetTempo(int tempo){
        this.tempo = tempo;
    }

    public int getTempo(){
        return tempo;
    }
    public String toString(){
        return String.valueOf(getTempo());
    }
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return v.visit(context, this);
    }
}
