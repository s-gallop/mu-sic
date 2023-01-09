lexer grammar MusicLexer;

WS: [\r\n\t ]+ -> channel(HIDDEN);
ASSIGNMENT: '=';
COMPARATOR: ('==' | '=/=' | '>=' | '<=' | '>' | '<');
SLASH: '/';
INTEGER: [0-9]+;
UNDERSCORE: '_' -> mode(OCTAVE_MODE);

TEMPO: 'tempo:';

WAIT: 'wait';

PLAY: 'play';
THEN_AFTER: 'then after';
AND: '&';

SHIFT: 'shift';
DIRECTION: ('up' | 'down');

NOTE: [a-g];
ACCIDENTAL: ('sharp' | 'flat');

CHORD: 'chord';
FUNCTION: 'function';
END_FUNCTION: 'end function';
COMMA: ',';
CALL: 'call';

IF: 'if';
ELSE_IF: 'else if';
ELSE: 'else';
END_IF: 'end if';

REPEAT: 'repeat';
END_REPEAT: 'end repeat';
ITERATION_NUM: 'iteration';
RENAME: 'rename';
TO: 'to';
NAME: [A-Za-z][A-Za-z0-9]*;

mode OCTAVE_MODE;
OCTAVE: [0-8] -> mode(DEFAULT_MODE);