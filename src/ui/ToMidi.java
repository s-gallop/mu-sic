package ui;

import Midi.Note;
import java.io.*;
import java.util.*;
import javax.sound.midi.*;

/**
 * Code for Midi Writer is found from this website: http://www.automatic-pilot.com/midifile.html
 * Author: Karl Brown
 */

public class ToMidi {
    private double currTicks;
    private Track track;
    private Sequence sequence;

    private int tempo;


    public ToMidi() throws InvalidMidiDataException {
        this.currTicks = 1;
        sequence = new Sequence(javax.sound.midi.Sequence.PPQ,24);
        this.track = sequence.createTrack();
    }

    public void setTempo(int tempo){
        this.tempo = tempo;
    }

    // Helper method to add chord to Track
    public void addChord(List<Note> notes) {
        for (Note n : notes) {
            addNote(n);
        }
    }

    // Helper method to add wait time
    public void addWait(int waitTimeMs) {
        currTicks += ((double) waitTimeMs )   * sequence.getResolution() / tempo;
    }

    // Add note into midi track
    public void addNote(Note note) {
        ShortMessage mm;
        MidiEvent me;
        int ticksPerMinute = (tempo * sequence.getResolution());
        try {
            // note on
            mm = new ShortMessage();
            mm.setMessage(0x90, note.getMidiNumber(), 0x60);
            me = new MidiEvent(mm, (long) currTicks);
            track.add(me);
            // note off
            mm = new ShortMessage();
            mm.setMessage(0x80, note.getMidiNumber(), 0x40);
            me = new MidiEvent(mm, (long) (currTicks + (note.getDuration()  * sequence.getResolution() / tempo )));
            track.add(me);

        } catch (Exception e) {
            // none
        }
    }

    public void initializeMidi() {
        try {
            //****  General MIDI sysex -- turn on General MIDI sound set  ****
            byte[] b = {(byte) 0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte) 0xF7};
            SysexMessage sm = new SysexMessage();
            sm.setMessage(b, 6);
            MidiEvent me = new MidiEvent(sm, (long) 0);
            track.add(me);

            //****  set tempo (meta event)  ****
            MetaMessage mt = new MetaMessage();
            byte[] bt = {0x02, (byte) 0x00, 0x00};
            mt.setMessage(0x51, bt, 3);
            me = new MidiEvent(mt, (long) 0);
            track.add(me);

            //****  set track name (meta event)  ****
            mt = new MetaMessage();
            String TrackName = new String("midifile track");
            mt.setMessage(0x03, TrackName.getBytes(), TrackName.length());
            me = new MidiEvent(mt, (long) 0);
            track.add(me);

            //****  set omni on  ****
            ShortMessage mm = new ShortMessage();
            mm.setMessage(0xB0, 0x7D, 0x00);
            me = new MidiEvent(mm, (long) 0);
            track.add(me);

            //****  set poly on  ****
            mm = new ShortMessage();
            mm.setMessage(0xB0, 0x7F, 0x00);
            me = new MidiEvent(mm, (long) 0);
            track.add(me);

            //****  set instrument to Piano  ****
            mm = new ShortMessage();
            mm.setMessage(0xC0, 0x00, 0x00);
            me = new MidiEvent(mm, (long) 0);
            track.add(me);
        } catch (Exception e) {
            // none
        }
    }

    public void writeMidi() {
        try {
            // set end of track
            MetaMessage mt = new MetaMessage();
            byte[] bet = {}; // empty array
            mt.setMessage(0x2F,bet,0);
            MidiEvent me = new MidiEvent(mt, (long)currTicks+20);
            this.track.add(me);

            //****  write the MIDI sequence to a MIDI file  ****
            File f = new File("midifile.mid");
            MidiSystem.write(sequence,1,f);
        } catch(Exception e) {
            System.out.println("Exception caught " + e.toString());
        } //catch
    }
}