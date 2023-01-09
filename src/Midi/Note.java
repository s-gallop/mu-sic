package Midi;

import java.util.List;

/**
 * Note class containing all information for a particular note
 * Maybe can be used over and over again similar to var in DSL language?
 */
public class Note extends Component {
    private int duration;
    private int INSTRUMENT = 0;
    private int midiNumber;
    private String note;
    private int octave;

    public Note(String note, int octave, int duration) {
        super();
        this.midiNumber = id(octave + note);
        this.note = note;
        this.octave = octave;
        this.duration = duration;
    }

    public Note(int midiNumber, int duration){
        //Use this constructor if we know the midiNumber
        //Got this from https://stackoverflow.com/questions/712679/convert-midi-note-numbers-to-name-and-octave
        super();
        String notesListString = "C C#D D#E F F#G G#A A#B ";
        this.midiNumber = midiNumber;
        this.duration = duration;
        this.octave =(midiNumber / 12) -1;
        this.note  = notesListString.substring((midiNumber % 12) * 2, (midiNumber % 12) * 2 + 2).trim();
    }

    public int getMidiNumber() {
        return midiNumber;
    }

    public int getDuration() {
        return duration;
    }

    public String getNote() {
        return note;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    // Shift the current node by numUp
    // Should not be used anymore
    public void shift(int numUp) {
        midiNumber += numUp;
    }

    /**
     * Returns the MIDI id for a given octave + note string: eg. 4C -> 60
     *
     * @return int
     */
    public static int id(String note) {
        //got most of this from https://gist.github.com/pbloem/d29bf80e69d333415622
        int octave = Integer.parseInt(note.substring(0,1));
        for (int i =0; i< notes.size(); i++){
            List<String> notePair = notes.get(i);
            String noteValue = note.substring(1);

            if (notePair.contains(noteValue)){
                return i +12 * octave + 12;
            }
        }
        throw new RuntimeException("Note given is not valid: " + note);
    }
    public static int getOctaveFromMidiNumber(int midiNumber){
        return (midiNumber / 12) -1;
    }

    public static String getNoteStringFromMidiNumber(int midiNumber){
        String notesListString = "C C#D D#E F F#G G#A A#B ";
        return notesListString.substring((midiNumber % 12) * 2, (midiNumber % 12) * 2 + 2).trim();
    }
    /**
     * Play the sound of the current Note
     *
     * Found how to use channels and threads of MIDI here: https://gist.github.com/pbloem/d29bf80e69d333415622
     * Author: Peter
     */
    @Override
    public void activate() {
        Thread thread = new Thread(() -> {
            channels[INSTRUMENT].noteOn(midiNumber, VOLUME);
            // * wait (on this thread)
            try {
                Thread.sleep(duration, 0);
            } catch (InterruptedException e) {
                throw new RuntimeException("InterruptedException " + e.toString());
            }
            // * stop playing a note
            channels[INSTRUMENT].noteOff(midiNumber);
        });
        System.out.print(this.note + this.octave +  " ");
        // System.out.println("Midi number:"  + this.midiNumber); // Prints midi number of each note
        thread.start();
    }
}