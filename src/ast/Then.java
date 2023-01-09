package ast;

import Midi.Note;

import java.util.List;

public class Then extends Command {

    //Refer to lexer and Parser for what these mean
    private Length afterLength;
    private Length playLength;
    private Set soundSet;
    public Then(Length afterLength, Length playLength, Set soundSet){
        this.afterLength = afterLength;
        this.playLength = playLength;
        this.soundSet = soundSet;
    }
    public Length getAfterLength(){
        return afterLength;
    }
    public Length getPlayLength(){
        return playLength;
    }

    public Set getSoundSet() {
        return soundSet;
    }

    @Override
    public <C, T> T accept(C context, Visitor<C, T, List<Note>> v) {
        return v.visit(context, this);
    }
}
