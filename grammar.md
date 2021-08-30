# Grammar
##Values
A value is represented in a handful of types.
- Number
  - An integer or floating point value.
- Boolean
  - A boolean value, true or false.
- String
  - A list of characters, created by enclosing text in two double quotes.
- Array
  - A list of any value, created by enclosing values in square brackets separated by commas.
- Function
  - A command which can be defined and called.

###Variables
A variables is declared using either the *var* or *const* keywords.
Defining a variable with *const* makes it unchangeable later in the program.
Unlike in Java variables only hold values, not references.

###Functions
A function is declared using the *func* keyword.
All defined functions are constant and can not be changed after creation.
Functions need no defined return or parameter types, and will determine those values at runtime.

You can only use the *return* keyword inside a Function.

##Branching
Fizz has a few basic branching commands such as:
- if
  - A conditional branch which can be continued using the *elif* keyword, or finalized using the *else* keyword.
- while
  - A loop which will run until its condition is false.
- for
  - A loop which will run until its condition is met, but allows the use of a temporary variable that will incriment by a set value every loop.

Both *while* and *for* loops can use the *continue* and *break* keyword, which share the same functionality as in Java.

##Other Keywords
Some additional keywords include:
- cast
  - Used to cast one value to another.

