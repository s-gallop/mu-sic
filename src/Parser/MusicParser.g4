parser grammar MusicParser;
options {tokenVocab = MusicLexer;}

program: set_tempo function_defn* command* EOF;

set_tempo: TEMPO INTEGER;
function_defn: FUNCTION NAME command* END_FUNCTION;
command: (wait_command | play_command | shift_command | call_command | chord_init | conditional | repeat);

wait_command: WAIT length;
play_command: PLAY set length then_command*;
shift_command: SHIFT chord DIRECTION INTEGER;
call_command: CALL NAME;
chord_init: chord ASSIGNMENT noteSet;
repeat: REPEAT INTEGER iteration_defn? command* END_REPEAT;

chord: CHORD NAME;
length: numerator=INTEGER (SLASH denominator=INTEGER)?;
set: sound (AND sound)*;
then_command: THEN_AFTER length PLAY set length;
noteSet: note (AND note)*;
iteration_defn: RENAME ITERATION_NUM TO NAME;

sound: (note | chord);
note: NOTE UNDERSCORE OCTAVE ACCIDENTAL?;
conditional: if_statement else_if_statement* else_statement? END_IF;

if_statement: IF comparison command*;
else_if_statement: ELSE_IF comparison command*;
else_statement: ELSE command*;

comparison: NAME COMPARATOR INTEGER;
