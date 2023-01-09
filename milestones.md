# Milestone 1 Project Proposal

1.Our DSL will allow the user to input musical notes and rests, customizing aspects of the song (.e.g tempo, dynamics) , allowing for recurring melodies/harmonies, and output as midi or mp3 format to play. Users can compose music by building musical measures, specifying the pitches and length of each note or chord. Users can also layer instruments for a richer sound. 

2.We store chords as an array type.  Setting mutable variables for verses/chorus/measures and chords to adjust accompaniment. Using loops for repeating melodies/harmonies. Melodies and harmonies can also be nested and composed together. 

3.Our DSL would involve music knowledge but not all of our members have a music background, so this was a concern from TA.

4.Find suitable tools for music generation, and find a way to incorporate functions into our language. More generally, we are working on a rough mockup of the language.

# Milestone 2

### Task Assignment

Jerome: User Studies <br>
Spencer: Tokenization/ANTLR <br>
Rest (Wilson, Jean, Yutong): Read jsyn documentation and think of implementation. Look at different possible Java libraries<br>


### Road Map:

When each thing will be ready / prototype


| Week        | General     |First Half of Week | Second Half of Week|
| ----------- | ----------- |-------------------|--------------------|
| Sep 19-25   | Tokenization and Parsing (ANTLR), first user study |Defining the grammar, finding appropriate Java libraries for sound generation|Finalizing and implementing grammar, implementation of lexer/parser|
|Sep 26- Oct 2| AST and static checks|Begin implementation for sound generation, notes|Playing strings of notes/repeats  possible. Implement Chords|
|Oct 3 - 9 |Dynamic checks, second user study|Finalizing implementation<br> Implement variables<br> Implement conditionals bug fixing | Implement functions<br> Planning for video recording|
|Oct 10 - 17 |Project done with dynamic/static validation implemented| Finishing up the entire project, video recording/editing | |


### Grammar Outline

VAR: \S+ ;<br>
COMPARATOR: (‘=/=’ | ‘=’ | ‘<=’ | ‘>=’ | ‘>’ | ‘<’);<br>

COMMAND: (TEMPO | SIGNATURE | WAIT | PLAY | CONDITIONAL | REPEAT) ;<br>

FRACTION: ‘\d+ / \d+’ ;<br>
INTEGER \d+ ;<br>

TEMPO: ‘tempo: ’ \d+ ;<br>
SIGNATURE: ‘signature: ‘ FRACTION ;<br>

WAIT: ‘wait ’ LENGTH;<br>

PLAY: ‘play ’ (SET ‘ ‘ LENGTH THEN* | FUNCTION) ;<br>
THEN: \n\t ‘then after’ LENGTH ‘ ‘ SET ‘ ‘ LENGTH ;<br>
LENGTH: (FRACTION | INTEGER) ;<br>
SET: SOUND (‘, ‘ SOUND)* ;<br>

SOUND: (NOTE PITCH ‘ ‘ ACCIDENTAL? | CHORD) ;<br>
CHORD: VAR ;<br>
CHORD_INIT: CHORD ‘=’ SET ;<br>
NOTE: [a-g] ;<br>
PITCH: ‘_’ OCTAVENUM ;<br>
OCTAVE_NUM: [0-8] ;<br>
ACCIDENTAL: (‘sharp’ | ‘flat’) ;<br>

COMPARISON: INTEGER COMPARATOR INTEGER ;<br>
BODY: (\n\t COMMAND)+ ;<br>

CONDITIONAL: IF ELSE_IF* ELSE? ;<br>
IF: ‘if ‘ COMPARISON ‘:’ BODY ;<br>
ELSE_IF: \n ‘else if ‘ COMPARISON ‘:’ BODY ;<br>
ELSE: \n ‘else:’ BODY ;<br>

REPEAT: ‘repeat ’ INTEGER ‘:’ BODY ;<br>
ITERATION_NUM: ‘iteration’ ;<br>

FUNCTION: VAR ;<br>
FUNCTION_DEFN: FUNCTION PARAMETERS? ‘:’ BODY ;<br>
PARAMETERS: ‘(‘ VAR (‘ ,’ VAR)* ‘)’ ;<br>

### Progress so far:
We drafted grammar.<br>
Assigned work for each group member.

# Milestone 3
1. See Lexer.g4 and Parser.g4 in our repository.<br>
2. **We gave user some concrete examples to user:**<br>
    
    **Example 1: Playing a C scale then a chord**<br>
    tempo: 120<br>
    signature: 4/4<br>
    chord cMaj = c_4, e_4, g_4<br>
    play c_4, 1<br>
    then after 0 play d_4 1<br>
    then after 0 play e_4 1<br>
    then after 0 play f_4 1<br>
    then after 0 play g_4 1<br>
    then after 0 play a_4 1<br>
    then after 0 play b_4 1<br>
    then after 0 play c_5 1<br>
    then after 1 play cMaj, c_2 3<br>
  
    **Example 2: Loop/conditional (Repeating a chord progression)**<br>
    tempo: 120<br>
    signature: 2/4<br>
    chord I = d_2, f_2 sharp, a_2<br>
    chord III = c_2, f_2 sharp, a_2<br>
    chord IV = d_2, g_2, b_2<br>
    repeat 4<br>
    if iteration == 4 play IV, I 2<br>
    else if iteration == 2 wait 2<br>
    else play III, IV 2 wait 1 play d_3 1 <br>
    end repeat;<br>
   
    **Example 3: Function**
    tempo: 77<br>
    signature: 4/8<br>
    function myMelody<br>
    play b_4 flat g_6 sharp 3<br>
    wait 2<br>
    play a_5 1<br>
    then after 2 play d_3 sharp 4<br>
    wait 1<br>
    play e_2, e_3, f_3 7 3<br>
    end function<br>
    play myMelody<br>

     **Example 4: Shift command**
     tempo: 100<br>
     signature: 4/4<br>
     chord myChord = a_4 sharp, d_5 sharp, f_5 sharp<br>
     play myChord<br>
     shift myChord up 7/2<br>
     play myChord<br>

     **Results** <br>
     We let a user to play a C scale two octaves apart at the same time, and programming a simple song (e.g. Mary had a little lamb, Old McDonald)<br>
     Below are notes from first user study:
      - Users liked the shift functionality 
      - Specifying a note's length could be more clear (with a keyword perhaps) 
      - The difference between 'wait' and 'then after' had to be explained (a quirk of our language) 
      - Users liked the repeat functionality 
      - Being able to define a left hand and right hand would be helpful 
      - Users were able to program a C scale in two octaves
      - Users were able to program a simple melody (Twinkle Twinkle Little Star)
      - Overall, language was easy to learn and use for users

3. We added a shift command, and made functions have no return values. We also made the language whitespace agnostic by adding end structure key words. Finally, we split the language into lexer and parser, which allowed for some simplifications.

4. On track

# Milestone 4

### Status of implementation:



* Finished functions for playing chords, notes, stanzas with repeats
* Finished Lexer and Parser
* Starting on implementation of variables and functions

### Plans for final user study



* Our group members will interview few students in the UBC School of Music without prior coding experience, and ask for their evaluation on our language

### Planned timeline for the remaining days


<table>
  <tr>
   <td>Week
   </td>
   <td>General
   </td>
   <td>First Half of week
   </td>
   <td>Second Half of week
   </td>
  </tr>
  <tr>
   <td>Oct 3 - 9
   </td>
   <td>Dynamic checks, second user study
   </td>
   <td>N/A
   </td>
   <td>Finalizing implementation w/ ASTs
<p>
Implement variables 
<p>
Implement conditionals
<p>
Implement functions
<p>
Planning for video recording
   </td>
  </tr>
  <tr>
   <td>Oct 10 - 17
   </td>
   <td>
   </td>
   <td>Project done with dynamic/static validation implemented
   </td>
   <td>Finishing up the entire project, video recording/editing
<p>
User studies
   </td>
  </tr>
</table>

# Milestone 5

### Status of implementation:

* Finished implementation, generating music works now using our language, with all promised features.
* Final dynamic/static checks and error handling needs to be finished
* Rough draft of video script completed

### Status/Results for final user study

* In progress, will be performed on Saturday

### Any changes to the language design

* Parameters removed from functions, as it not being necessary to the user

### Planned timeline for the remaining days

* Thurs - Fri: Finish final checks for language, perform user study, begin working on video
* Sat - Sun: Redesign language as per final user study, finish video
