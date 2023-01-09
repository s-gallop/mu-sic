package Parser;

import ast.*;

import java.util.ArrayList;
import java.util.List;

public class ParseTreeToAST extends MusicParserBaseVisitor<Node> {

    StringBuilder warningString;
    StringBuilder errorString;
    public ParseTreeToAST(){
        warningString = new StringBuilder();
        errorString = new StringBuilder();
    }
    public StringBuilder getWarningString(){
        return warningString;
    }

    public StringBuilder getErrorString() {
        return errorString;
    }

    @Override
    public Program visitProgram(MusicParser.ProgramContext ctx) {
        SetTempo setTempo = (SetTempo) ctx.set_tempo().accept(this);

        ArrayList<Function> functions = new ArrayList<>();
        for (MusicParser.Function_defnContext fn : ctx.function_defn()) {
            functions.add((Function) fn.accept(this));
        }

        ArrayList<Command> commands = new ArrayList<>();
        for (MusicParser.CommandContext cmd : ctx.command()) {
            commands.add((Command) cmd.accept(this));
        }

        return new Program(setTempo, functions, commands);
    }

    @Override
    public Command visitCommand(MusicParser.CommandContext ctx) {
        return (Command) visitChildren(ctx);
    }

    @Override
    public SetTempo visitSet_tempo(MusicParser.Set_tempoContext ctx) {
        int tempo = Integer.parseInt(ctx.INTEGER().getText());
        if (tempo <=0){
            errorString.append("Error: tempo "+ tempo +" at "+ ctx.start.getLine()+ ":"+ctx.start.getCharPositionInLine()+ " is not valid \r\n");
        }
        return new SetTempo(Integer.parseInt(ctx.INTEGER().getText()));
    }

    @Override
    public Wait visitWait_command(MusicParser.Wait_commandContext ctx) {
        double length = ((Length) ctx.length().accept(this)).getLength();
        if (length == 0){
            warningString.append("Warning: wait length at "+ ctx.length().start.getLine()+ ":"+ctx.length().start.getCharPositionInLine()+ " is 0, are you sure you want that? \r\n");
        }
        return new Wait(length);
    }

    @Override
    public Play visitPlay_command(MusicParser.Play_commandContext ctx) {
        double length = ((Length) ctx.length().accept(this)).getLength();
        if (length == 0){
            warningString.append("Warning: play length at "+ ctx.length().start.getLine()+ ":"+ctx.length().start.getCharPositionInLine()+ " is 0, are you sure you want that? \r\n");
        }
        Set set = (Set) ctx.set().accept(this);
        List<Then> thens = new ArrayList<>();
        for (MusicParser.Then_commandContext c: ctx.then_command()) {
            thens.add((Then) c.accept(this));
        }
        return new Play(length, set, thens);
    }

    @Override
    public Then visitThen_command(MusicParser.Then_commandContext ctx) {
        Length afterLength = ((Length) ctx.length().get(0).accept(this));
        Length playLength = ((Length) ctx.length().get(1).accept(this));
        if (playLength.getLength() == 0){
            warningString.append("Warning: play length at "+ ctx.length().get(1).start.getLine()+ ":"+ctx.length().get(1).start.getCharPositionInLine()+ " is 0, are you sure you want that? \r\n");
        }
        Set soundSet = ((Set) ctx.set().accept(this));
        return new Then(afterLength, playLength, soundSet);
    }

    @Override
    public Call visitCall_command(MusicParser.Call_commandContext ctx) {
        String name = ctx.NAME().getText();
        return new Call(name);
    }

    @Override
    public Length visitLength(MusicParser.LengthContext ctx) {
        double length = Double.parseDouble(ctx.numerator.getText());
        if (ctx.denominator != null) {
            if ( Double.parseDouble(ctx.denominator.getText()) == 0){
                errorString.append("Error: DivideByZeroError at "+ ctx.start.getLine()+ ":"+ctx.start.getCharPositionInLine()+ " trying to divide by zero in length \r\n"  );
            }
            length /= Double.parseDouble(ctx.denominator.getText());

        }
        return new Length(length);
    }

    @Override
    public Set visitSet(MusicParser.SetContext ctx) {
        List<Sound> sounds = new ArrayList<>();
        for (MusicParser.SoundContext soundContext : ctx.sound()) {
            sounds.add((Sound) soundContext.accept(this));
        }
        return new Set(sounds);
    }

    @Override
    public ChordInit visitChord_init(MusicParser.Chord_initContext ctx) {
        String name = ctx.chord().NAME().getText();
        NoteSet set = (NoteSet) ctx.noteSet().accept(this);
        return new ChordInit(name, set);
    }

    @Override
    public NoteSet visitNoteSet(MusicParser.NoteSetContext ctx) {
        List<Sound> notes = new ArrayList<>();
        for (MusicParser.NoteContext n: ctx.note()) {
            notes.add((Note) n.accept(this));
        }
        return new NoteSet(notes);
    }

    @Override
    public Sound visitSound(MusicParser.SoundContext ctx) {
        return (Sound) visitChildren(ctx);
    }

    @Override
    public Shift visitShift_command(MusicParser.Shift_commandContext ctx) {
        double steps = Integer.parseInt(ctx.INTEGER().getText());
        if (steps == 0){
            warningString.append("Warning: shift steps at "+ ctx.start.getLine()+ ":"+ctx.start.getCharPositionInLine()+ " is 0, are you sure you want that? \r\n");
        }
        return new Shift((Chord) ctx.chord().accept(this), ctx.DIRECTION().getText(), steps);
    }

    @Override
    public Compare visitComparison(MusicParser.ComparisonContext ctx) {
        return new Compare(ctx.NAME().getText(), ctx.COMPARATOR().getText(), Integer.parseInt(ctx.INTEGER().getText()));
    }

    @Override
    //I think this should be the same as the Else If
    public If visitIf_statement(MusicParser.If_statementContext ctx) {
        Compare compare = (Compare) ctx.comparison().accept(this);
        List<Command> commands = new ArrayList<>();
        for (MusicParser.CommandContext c : ctx.command()) {
            commands.add((Command) c.accept(this));
        }
        return new If(compare, commands);
    }

    @Override
    public Elif visitElse_if_statement(MusicParser.Else_if_statementContext ctx) {
        Compare compare = (Compare) ctx.comparison().accept(this);
        List<Command> commands = new ArrayList<>();
        for (MusicParser.CommandContext c : ctx.command()) {
            commands.add((Command) c.accept(this));
        }
        return new Elif(compare, commands);
    }

    @Override
    public Else visitElse_statement(MusicParser.Else_statementContext ctx) {
        List<Command> commands = new ArrayList<>();
        for (MusicParser.CommandContext c : ctx.command()) {
            commands.add((Command) c.accept(this));
        }
        return new Else(commands);
    }

    @Override
    public Conditional visitConditional(MusicParser.ConditionalContext ctx) {
        List<Elif> elifs = new ArrayList<>();
        for (MusicParser.Else_if_statementContext c: ctx.else_if_statement()) {
            elifs.add((Elif) c.accept(this));
        }
        Else e = null;
        if (ctx.else_statement() != null) e = (Else) ctx.else_statement().accept(this);
        return new Conditional((If) ctx.if_statement().accept((this)), elifs, e);
    }

    @Override
    public Repeat visitRepeat(MusicParser.RepeatContext ctx) {
        int iterations = Integer.parseInt(ctx.INTEGER().getText());
        if (iterations <=1){
            warningString.append("Warning: repeat number at "+ ctx.start.getLine()+ ":"+ctx.start.getCharPositionInLine()+ " is " +iterations+ ", are you sure you want that? \r\n");
        }
        ItrDfn defn = null;
        if (ctx.iteration_defn() != null) {
         defn = (ItrDfn) ctx.iteration_defn().accept(this);
        }
        List<Command> commands = new ArrayList<>();
        for (MusicParser.CommandContext c : ctx.command()) {
            commands.add((Command) c.accept(this));
        }
        return new Repeat(iterations, defn, commands);
    }

    @Override
    public ItrDfn visitIteration_defn(MusicParser.Iteration_defnContext ctx) {
        return new ItrDfn(ctx.NAME().getText());
    }

    @Override
    public Function visitFunction_defn(MusicParser.Function_defnContext ctx) {
        String name = ctx.NAME().getText();
        List<Command> commands = new ArrayList<>();
        for (MusicParser.CommandContext c: ctx.command()) {
            commands.add((Command) c.accept(this));
        }
        return new Function(name, commands);
    }

    @Override
    public Note visitNote(MusicParser.NoteContext ctx) {
        int octave = Integer.parseInt(ctx.OCTAVE().getText());
        String note =  ctx.NOTE().getText();
        if ((octave == 8 && !note.equalsIgnoreCase("C")) || (octave == 0 && !note.equalsIgnoreCase("B") && !note.equalsIgnoreCase("A"))){
            errorString.append("Error: Note "+ note + octave+ " at " +ctx.start.getLine()+ ":"+ctx.start.getCharPositionInLine() + " is out of playable MIDI range \r\n");
        }
        String accidental = null;
        if (ctx.ACCIDENTAL() != null) accidental = ctx.ACCIDENTAL().getText();
        return new Note(ctx.NOTE().getText(), octave, accidental);
    }

    @Override
    public Chord visitChord(MusicParser.ChordContext ctx) {
        return new Chord(ctx.NAME().getText());
    }
}
