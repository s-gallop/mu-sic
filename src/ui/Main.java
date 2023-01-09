package ui;

import Midi.*;
import Parser.MusicLexer;
import Parser.MusicParser;
import Parser.ParseTreeToAST;
import ast.Program;
import ast.evaluators.Evaluator;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import static java.lang.System.exit;


/**
 * Sample Code from 2022 Winter CPSC410 Exercise 2 by Alex Summers
 *
 */
public class Main {
    public static void main(String[] args) throws Exception {
        MusicLexer lexer = new MusicLexer(CharStreams.fromFileName("input.music"));
        for (Token token : lexer.getAllTokens()) {
            System.out.println(token);
        }
        lexer.reset();
        TokenStream tokens = new CommonTokenStream(lexer);
        System.out.println("Done tokenizing");

        MusicParser parser = new MusicParser(tokens);
        ParseTreeToAST visitor = new ParseTreeToAST();

        Program parsedProgram = visitor.visitProgram(parser.program());
        if (!visitor.getWarningString().isEmpty()){
            System.out.println(visitor.getWarningString());
        }
        if (!visitor.getErrorString().isEmpty()){
            System.out.println(visitor.getErrorString());
            System.out.println("Unable to compile, please fix errors!");
            exit(0);
        }

        System.out.println("Done parsing");

        Song song = new Song();
        ToMidi midiWriter = new ToMidi();
        midiWriter.initializeMidi();

        Evaluator e = new Evaluator(song, midiWriter);
        StringBuilder s = new StringBuilder();
        parsedProgram.accept(s, e);
        if(s.isEmpty()) {
            System.out.println("Evaluation completed successfully");
            midiWriter.writeMidi();
            System.out.println("Finished writing midi");
            song.playSong();
        } else {
            System.out.println("Error during runtime: \n" + s);
        }
    }
}
