package ast.evaluators;

import Midi.Song;
import ast.*;
import ast.Set;
import ui.ToMidi;
import java.util.*;

public class Evaluator implements Visitor<StringBuilder, Integer, List<Midi.Note>> {
    private final Map<String, Integer> iterationTable = new HashMap<>();
    private final ChordTable chordTable = new ChordTable();
    private final Map<String, Function> functionTable = new HashMap<>();
    private Song song;
    private ToMidi midiWriter;

    public Evaluator(Song song, ToMidi midiWriter) {
        this.song = song;
        this.midiWriter = midiWriter;
    }

    @Override
    public Integer visit(StringBuilder context, Program p) {
        p.getSetTempo().accept(context, this);
        for (Function fn : p.getFunctions()) {
            fn.accept(context, this);
            if (!context.isEmpty()) {
                System.out.println(context);
                System.exit(0);
                // If any errors are appended onto the string builder, stop the program and print msg
            }
        }
        for (Command cmd : p.getCommands()) {
            if (!context.isEmpty()) {
                System.out.println(context);
                System.exit(0);
                // If any errors are appended onto the string builder, stop the program and print msg
            }
            cmd.accept(context, this);
        }
        return null;
    }

    @Override
    public Integer visit(StringBuilder context, SetTempo s) {
        song.setTempo(s.getTempo());
        midiWriter.setTempo(s.getTempo());
        return null;
    }

    @Override
    public Integer visit(StringBuilder context, ast.Wait w) {
        //Conversion from BPM & length to ms
        song.components.add(new Midi.Wait((int) ((60000 / song.getTempo()) * w.getlength())));
        this.midiWriter.addWait((int) ((60000 / song.getTempo()) * w.getlength()));
        return null;
    }

    @Override
    public Integer visit(StringBuilder context, Play p) {
        List<Midi.Note> notes;
        int length = (int) ((60000 / song.getTempo()) * p.getLength());
        for (Sound s : p.getSet().getSoundList()) {
            notes = s.accept2(context, this);
            if (notes == null) {  // encountered an undeclared chord
                context.append("Attempt to use play chord that wasn't declared.\n");
                return null;
            }
            for (Midi.Note n : notes) {
                n.setDuration(length);
            }
            song.components.addAll(notes);
            this.midiWriter.addChord(notes);
        }
        if (!p.getThens().isEmpty()) {
            song.components.add(new Midi.Wait(length));
            this.midiWriter.addWait(length);
        }
        int prevWaitMS = 0;
        for (Then t : p.getThens()) {
            song.components.add(new Midi.Wait(prevWaitMS));
            this.midiWriter.addWait(prevWaitMS);
            prevWaitMS = t.accept(context, this);
        }
        return null;
    }

    @Override
    public Integer visit(StringBuilder context, Then t) {
        List<Midi.Note> notes;
        int playLenMS = (int) ((60000 / song.getTempo()) * t.getPlayLength().getLength());
        int afterLenMS = (int) ((60000 / song.getTempo()) * t.getAfterLength().getLength());
        song.components.add(new Midi.Wait(afterLenMS));
        midiWriter.addWait(afterLenMS);
        for (Sound s : t.getSoundSet().getSoundList()) {
            notes = s.accept2(context, this);
            for (Midi.Note n : notes) {
                n.setDuration(playLenMS);
            }
            song.components.addAll(notes);
            midiWriter.addChord(notes);
        }
        return playLenMS;
    }

    @Override
    public Integer visit(StringBuilder context, Call c) {
        if (!functionTable.containsKey(c.getName())) {
            context.append("Attempt to use function ").append(c.getName()).append(" that wasn't declared.\n");
        } else {
            Function function = functionTable.get(c.getName());
            List<Command> body = function.getCommands();

            chordTable.addTable();
            for (Command command : body) {
                command.accept(context, this);
            }
            chordTable.deleteTable();
        }
        return null;
    }

    @Override
    public Integer visit(StringBuilder context, Length length) {
        return null;
    }

    @Override
    public Integer visit(StringBuilder context, Set s) {
        // visitor for set is not used
        return null;
    }

    @Override
    public Integer visit(StringBuilder context, ChordInit c) {
        chordTable.addChord(c.getName(), c.getSet());
        return null;
    }

    @Override
    public Integer visit(StringBuilder context, Shift s) {

        String chordToShift = s.getName();
        Set chord = chordTable.getChord(chordToShift);
        //We know that it's always Notes so we can cast it
        if (chord == null) {
            context.append("Attempt to shift chord ").append(chordToShift).append(" that wasn't declared.\n");
            return null;
        }
        List<ast.Note> notesToShift = (List<ast.Note>)(List<?>)chord.getSoundList();
        List<ast.Sound> shiftedNotes = new ArrayList<>();
        String shiftDirection = s.getDirection();
        int shiftSteps = (int) s.getSteps();

        if (shiftDirection.equals("up")) {
            for (Note note: notesToShift){
                String accidental = "";
                if (note.getAccidental() != null && note.getAccidental().equals("sharp")){
                    accidental = "#";
                }
                else if (note.getAccidental() != null && note.getAccidental().equals("flat")){
                    accidental = "♭";
                }
                int noteMidiNumber = Midi.Note.id(note.getOctave()  + note.getNote().toUpperCase()+ accidental);
                int shiftedNoteMidiNumber = noteMidiNumber + shiftSteps;
                int shiftedNoteOctave = Midi.Note.getOctaveFromMidiNumber(shiftedNoteMidiNumber);
                String shiftedNoteNote = Midi.Note.getNoteStringFromMidiNumber(shiftedNoteMidiNumber);
                shiftedNotes.add(new ast.Note(shiftedNoteNote, shiftedNoteOctave, ""));
            }
            chordTable.addChord(chordToShift, new Set(shiftedNotes));

        } else if (shiftDirection.equals("down")) {
            for (Note note: notesToShift){
                String accidental = "";
                if (note.getAccidental() != null && note.getAccidental().equals("sharp")){
                    accidental = "#";
                }
                else if (note.getAccidental() != null && note.getAccidental().equals("flat")){
                    accidental = "♭";
                }
                int noteMidiNumber = Midi.Note.id(note.getOctave()  + note.getNote().toUpperCase() + accidental);
                int shiftedNoteMidiNumber = noteMidiNumber - shiftSteps;
                int shiftedNoteOctave = Midi.Note.getOctaveFromMidiNumber(shiftedNoteMidiNumber);
                String shiftedNoteNote = Midi.Note.getNoteStringFromMidiNumber(shiftedNoteMidiNumber);
                shiftedNotes.add(new ast.Note(shiftedNoteNote, shiftedNoteOctave, ""));
            }
            chordTable.addChord(chordToShift, new Set(shiftedNotes));
        } else {
            context.append("Shift direction has to be up or down, given: ").append(shiftDirection).append("\n");
        }
        return null;
    }

    @Override
    public Integer visit(StringBuilder context, Compare c) {
        String comparator  = c.getComparator();
        if (iterationTable.get(c.getName()) == null) {
            context.append("Attempt to use iteration variable " + c.getName() + " that wasn't declared.\n");
            return null;
        }
        int num = iterationTable.get(c.getName());
        int compInt = c.getCompareInt();
        switch (comparator){
            case"==":
                if(num == compInt){
                    return 1;
                }else return 0;
            case "=/=":
                if(num != compInt){
                    return 1;
                }else return 0;
            case ">=":
                if(num >= compInt){
                    return 1;
                }else return 0;
            case "<=":
                if(num <= compInt){
                    return 1;
                }else return 0;
            case ">":
                if(num > compInt){
                    return 1;
                }else return 0;
            case "<":
                if(num < compInt){
                    return 1;
                }else return 0;
        }
        context.append("Comparator is not valid, given: " + comparator + "\n");
        return null;
    }

    @Override
    public Integer visit(StringBuilder context, If i) {
        Integer compare = i.getCompare().accept(context, this);
        if (compare == null) {
            return null;
        }
        else if (compare == 1) {
            chordTable.addTable();
            for (Command c: i.getCommands()) {
                c.accept(context, this);
            }
            chordTable.deleteTable();
            return 1;
        }
        return 0;
    }

    @Override
    public Integer visit(StringBuilder context, Elif e) {
        Integer compare = e.getCompare().accept(context, this);
        if (compare == null) {
            return null;
        }
        else if (compare == 1) {
            chordTable.addTable();
            for (Command c: e.getCommands()) {
                c.accept(context, this);
            }
            chordTable.deleteTable();
            return 1;
        }
        return 0;
    }

    @Override
    public Integer visit(StringBuilder context, Else e) {
        chordTable.addTable();
        for (Command c: e.getCommands()) {
            c.accept(context, this);
        }
        chordTable.deleteTable();
        return null;
    }

    @Override
    public Integer visit(StringBuilder context, Conditional c) {
        Integer acceptIf = c.getIfStatement().accept(context, this);
        if (acceptIf == null || acceptIf == 1) return null;
        Integer acceptElif;
        for (Elif e: c.getElseIfStatements()) {
            acceptElif = e.accept(context, this);
            if (acceptElif == null || acceptElif == 1) return null;
        }
        if (c.getElseStatement() != null) c.getElseStatement().accept(context, this);
        return null;
    }

    @Override
    public Integer visit(StringBuilder context, Repeat p) {
        ItrDfn itr = p.getIterationDfn();
        chordTable.addTable();
        if (itr != null) addVariable(context, itr.getName(), 1);
        int iterations = p.getNumIterations();
        for (int i = 1; i <= iterations; i++) {
            if (itr != null) iterationTable.put(itr.getName(), i);
            for (Command c: p.getCommands()) {
                c.accept(context, this);
            }
        }
        if (itr != null) iterationTable.remove(itr.getName());
        chordTable.deleteTable();
        return null;
    }

    @Override
    public Integer visit(StringBuilder context, ItrDfn i) {
        return null;
    }

    @Override
    public Integer visit(StringBuilder context, Function f) {
        functionTable.put(f.getName(), f);
        return null;
    }

    @Override
    public List<Midi.Note> visit(StringBuilder context, Note n) {
        List<Midi.Note> notes = new ArrayList<>();
        Midi.Note note;
        if (n.getAccidental() != null && n.getAccidental().equals("sharp")) {
            note = new Midi.Note(n.getNote().toUpperCase() + "#", n.getOctave(), 1);
        } else if (n.getAccidental() != null && n.getAccidental().equals("flat")) {
            note = new Midi.Note(n.getNote().toUpperCase() + "♭", n.getOctave(), 1);
        } else {
            note = new Midi.Note(n.getNote().toUpperCase(), n.getOctave(), 1);
        }
        notes.add(note);
        return notes;
    }

    @Override
    public List<Midi.Note> visit(StringBuilder context, Chord c) {
        Set chord = chordTable.getChord(c.getName());
        if (chord == null) {
            context.append("Attempt to use variable ").append(c.getName()).append(" that wasn't declared.\n");
            return null;
        } else {
            List<Sound> noteList = chord.getSoundList(); // We know that chords can't be recursive
            List<Midi.Note> midiNoteList = new ArrayList<>();
            for (Sound s : noteList) {
                List<Midi.Note> currentNoteList = s.accept2(context, this);
                for (Midi.Note n : currentNoteList) {
                    midiNoteList.add(n);
                }
            }
            return midiNoteList;
        }
    }

    private void addVariable(StringBuilder context, String name, Integer itr) {
        if (checkName(context, name, iterationTable)) {
            iterationTable.put(name, itr);
        } else {
            context.append("as an iteration alias.\r\n");
        }
    }

    private void addVariable(StringBuilder context, String name, Function fn) {
        if (checkName(context, name, functionTable)) {
            functionTable.put(name, fn);
        } else {
            context.append("as an function name.\r\n");
        }
    }

    private boolean checkName(StringBuilder context, String name, Map map) {
        if (map.containsKey(name)) {
            context.append("Variable name '" + name + "' is already in use ");
            return false;
        }
        return true;
    }
}
