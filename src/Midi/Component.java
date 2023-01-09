package Midi;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Component abstract class for all components of the songs
 */
public abstract class Component {
    protected Synthesizer synth;
    protected MidiChannel[] channels;
    protected static final List<List<String>> notes = Arrays.asList(Arrays.asList("C", "B#"),
            Arrays.asList("C#", "D♭"), Arrays.asList("D"),
            Arrays.asList("D#", "E♭"), Arrays.asList("E", "F♭"),
            Arrays.asList("F", "E#"), Arrays.asList("F#", "G♭"),
            Arrays.asList("G"), Arrays.asList("G#", "A♭"),
            Arrays.asList("A"), Arrays.asList("A#", "B♭"),
            Arrays.asList("B", "C♭"));
    protected static final int VOLUME = 80; // between 0 et 127

    public Component() {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            this.channels = synth.getChannels();
        } catch (Exception ignored) {
            // will never throw an exception in our implementation
        }
    }

    public abstract void shift(int numUp);

    /**
     * Activate sound playing of Note and Chords
     * wait: specify whether to wait until it finish (used for play then)
     */
    public abstract void activate() throws Exception;
}