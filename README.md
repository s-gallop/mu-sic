# μ-sic
By group 22

For project 1, we created a DSL targeted at musicians, that allows the user to 
programmatically play a song. The user can write their program in the input.music file
and when the program is run, the result is played as audio, printed to the command line 
and generated as a midi file. The midi file is the primary output, while the other 
two are intended for the user's reference only. Specifically, the audio output may not
be accurate due to lag, but the midi output will be.

## Getting Started
### Tempo and Signature
The first thing every song needs is a tempo and a signature.
For simplicity, the signature is always 4/4 meaning that
there are 4 beats per bar (undefined in our language, since
it doesn't output sheet music) and every beat is a quarter
note. The user can set the tempo using the keyword "tempo"
followed by a colon and then a whole number. The whole number represents
the number of beats per minute.

### Notes and Chords
The main components of our language are sounds and the
commands that let you play them. A note is defined as
a letter from [a-g], an underscore, a whole number from [0-8]
and optionally an accidental marker: either the word
"sharp" or "flat". The letter specifies which base note
should be played, the whole number indicates the octave (low
numbers are low octaves) and the accidental indicates if
the note should be sharp or flat. So, c_4 would be the
c note in the fourth octave (middle c) while g_5 sharp
would be the g sharp note one octave higher.

A chord is a collection of notes. Chords are variables
which are defined and assigned in the same line, using
the keyword "chord", followed by a variable name, then
an "=" and finally a list of notes delimited by "&".
Chords are only defined locally to whatever structure
they were defined in. Structures will be discussed 
later, but if they aren't defined within a structure
they're defined globally, otherwise they are only defined
in that structure. Any time a chord is used, it must be 
proceeded by the key word "chord". Finally, chords can be
reassigned a new value after they're declared, using
the same syntax.

### Playing and Waiting
The play command is used to play sounds. It uses the
key word "play", followed by a list of notes or chords
delimited by "&", followed by the number of beats (whole number
or fraction) for the sound to play, finally followed by 0 
or more then commands, which will be explained later. 
Play plays all notes given at the same time for the 
number of beats given. However, the play command doesn't
advance the clock, so if two play commands are used one
after another all notes from both play commands will be
played simultaneously.

The wait command is used to advance the clock, so if
the user wants to play sounds after others, they can
wait for the same number of beats as they played the
first sounds for, then play more sounds. If they wait
more, they effectively play a rest (ie. empty space).
This command uses the "wait" keyword, followed by a
whole number or fraction representing the number of beats
to wait for.

An alternative to the wait command, in certain situations,
is the then command, as mentioned earlier. This command
allows the user to automatically wait until the sounds
that were playing stop, then wait longer if necessary,
before playing more sounds. This command can only be used
directly after a play command, to indicate which sound to
wait for the end of. It uses the keywords "then after",
followed by a whole number or fraction representing how long to
wait after the previous sounds finish playing, followed by
the play command. This was designed for ease of use, as it
removes the need for many lines of wait commands. However,
wait commands are necessary, as there's no way to start
playing a note while another is already playing with the
then command. This case is also the reason that wait 
commands advance the clock instead of just acting as rests.

## Complex Options
### Mutating Chords
Chords can be mutated by using the shift command. This command
allows the user to shift all the notes in a chord by a number
of semitones (notes) either up or down. The command uses the
"shift" keyword, followed by the "chord" keyword and the name
of the chord to shift, followed by the direction to shift the
chord and finally the number of semitones to shift it by. For
example, shifting a chord that consists only of the note c_4
up by 4 would result in that chord holding the value e_4.

### Repeats and Conditionals
The repeat is a structure that repeats everything contained
inside it the number of times given. A repeat is declared using
the "repeat" keyword, followed by the number of times to repeat
the body, optionally followed brackets containing the keywords
"rename iteration to", followed by a variable name. This variable
holds the iteration number (starts at 1 and increments each iteration),
which can be used in conditionals. Following the declaration, 0 or more
commands can be used, until the end of the repeat, where the keywords
"end repeat" are used.

The conditional is a structure that allows the user to determine
an outcome depending on a comparison to the iteration number.
As such, conditionals can only be used in a repeat, where a 
variable for the iteration number is defined. A conditional is
declared using the "if" keyword, followed by a comparison consisting
of an iteration variable's name, a comparator (==, =/=, <, <=, etc), 
and a whole number. After the declaration are 0 or more commands that
will be evaluated if the comparison is true. Then, the user can
write 0 or more else if statements, which are declared with the
keywords "else if", followed by a comparison, then 0 or more commands
which are evaluated if the comparison is true and all previous
comparisons were false. Finally, the user can optionally include an else
statement, which is only the keyword "else" followed by 0 or more 
commands which only evaluate if all previous comparisons were false.
After all the desired statements in the conditional, the keywords "end
if" are used to signify its end.

There are a few design choices we'd like to explain. We chose the
implementation of iteration numbers and only allowing conditionals
inside repeats because we wanted to stick to our domain. The domain we
decided on was music, so we didn't want the user to have the ability to
do math with our language. Therefore, we decided that we wouldn't have 
mutable variables for numbers. This would stop us from implementing
conditionals, which would have been fine except for the fact that 
conditionals do exist in music, where they are only based on the 
iteration number of a repeat. Therefore, we designed iteration number
in such a way that the user could nest repeats and iteration numbers,
since they could name them, but they couldn't define their own number
variables, maintaining the integrity of our domain. We also thought
that our design of iteration numbers would be easier to understand
for our target demographic than a structure like a for loop.

### Functions
Functions are collections of commands that can be called upon at any
time. They are defined using the "function" keyword, followed by a
variable name then 0 or more commands and finally the "end function"
keywords. Functions must be defined after tempo is set, but before
any other commands are used.

To invoke a function, the user must use the call command, which starts
with the "call" keyword, followed by the name of the function to
invoke. From there, all the commands in the function definition will
execute, before returning to the command after the call.

In terms of variable scope, function calls are treated the exact same
as the two other structures, conditionals and repeats. Any variable
that was assigned before a function call will be usable in that 
function and any variables assigned in a function will not be defined
once the function ends. So, although parameters can't be included in
a function definition or call, a function can use chord and iteration
variables that are defined at the time of the call. This design was
decided on for simplicity and ease of use, as we thought it would be
easier than parameters for the non-programmer target demographic to
understand.
