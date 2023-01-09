package ast;

import Midi.Note;

import java.util.List;

public abstract class Node {
    abstract public <C,T> T accept(C context, Visitor<C,T, List<Note>> v);
    public <C,T,T2> T2 accept2(C context, Visitor<C,T,T2> v) {
        return null;
    }
}
