package ast;

import Midi.Note;

import java.util.List;

public class Set extends Node{

    private List<Sound> soundList;
    public Set(List<Sound> soundList){
        this.soundList = soundList;
    }

    public List<Sound> getSoundList() {
        return soundList;
    }

    @Override
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return v.visit(context, this);
    }
}
