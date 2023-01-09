package ast;

public interface Visitor<C,T, T2> {

    T visit(C context, Program p);          // visitor for program
    T visit(C context, SetTempo s);         // visitor for setTempo
    T visit(C context, Wait w);             // visitor for wait
    T visit(C context, Play p);             // visitor for play
    T visit(C context, Then t);             // visitor for then
    T visit(C context, Set s);              // visitor for set
    T2 visit(C context, Chord c);           // visitor for chord_init
    T visit(C context, Shift s);            // visitor for shift
    T visit(C context, Compare c);          // visitor for comparison
    T visit(C context, If i);               // visitor for if_statement
    T visit(C context, Elif e);             // visitor for else_if_statement
    T visit(C context, Else e);             // visitor for else_statement
    T visit(C context, Conditional c);      // visitor for conditional
    T visit(C context, Repeat p);           // visitor for repeat
    T visit(C context, Function f);         // visitor for function_defn

    T2 visit(C context, Note n);            // visitor for Note
    T visit(C context, ItrDfn i);           // visitor for iteration_defn
    T visit(C context, ChordInit c);        // visitor for chord init
    T visit(C context, Call call);          // visitor for call_command

    T visit(C context, Length length);   // visitor for length
}
