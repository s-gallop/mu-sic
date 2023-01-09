package Midi;

import ast.Length;
import java.util.ArrayList;

// Class that holds all the notes generated from ast and plays each notes
public class Song{
    public ArrayList<Component> components = new ArrayList<>();
    public int tempo;
    public Length length;

    public Song(){

    }

    public void setLength(Length l){
        this.length = l;
    }
    public double getLength(){return length.getLength();}
    public void setTempo(int tempo){
        this.tempo = tempo;
    }
    public int getTempo(){
        return tempo;
    }

    public void playSong() throws Exception {
        for (Component comp : components) {
            comp.activate();
        }
    }
}