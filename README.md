# CSP Solver with Java (CSCE 421)

## Authors

- **Tomohide Bessho** - _UNL Undergraduate Student_ - https://github.com/tzxb018

## Homework 4 Progress Report (3/6/2020)

### Overview

This assignment requires me to create and use the data strucutres to run conflicted backtrack search (CBJ) to find the solutions of the given CSPs. As of now, I am currently still working on implementing the label and unlabel functions for the CBJ. I have the framework of these functions completed; however, I am having trouble with debugging some issues with finding the correct solution. When comparing my results with the past, it seems that 3q, 4q, and 5q match everyone else's results; however, when I get past 6q, my results differ. I know that my function is wrong because when I run the zebra problem, I cannot find a soluiton. I believe I am struggling with changing the correct conf_sets, as the indecies may be inconsistent in between label and unlabel.

## Homework 3 (2/27/2020)

### Overview

This assignment requried me to create and use these data structures to run search to find the solutions to the given CSP problems. Although there are several types of search and hybrids of these search, this assignment was focused on creating the basic search algoirhtm called backtracking.

### New and Updated Files/Directories

- /src/csp/BacktrackSearch/: a directory that holds all the .java files related to backtrack search
- /src/csp/BacktrackSearch/BacktrackSearch.java: the file that takes in the problem instance, sorts the variables into the variable-ordering heuristic, and initalizes the backtrack search
- /src/csp/BacktrackSearch/BCSSP.java: this file is responsible for running the backtrack search algorithm. In this file, you will find the main algoirhtm and the fucntions BT*Label and BT_Unlabel
  */src/csp/MainStructures/: this directory holds all the .java files responsible for holding information/data strucutres about the CSP problem
  \_/src/csp/MainStructures/MyVariable.java: updated this data structure to be able to find the degree of the current variable with the given constraints (weeds out unary constraints and will normailze the constraints to count the degree correctly)
  \*/src/csp/MyParser.java: updated how the program can take in inputs in the command line (will be explained below)

### Data Structures

- Current_path: this data strucutre is an ArrayList that holds the MyVariable data structure. The current_path data strucutre will start with a null pointer at the index 0 (to indicate the top of the tree), and then the variables in sequential order after being sorted by the inputted variable-order heuristic.
- Assignments: this data strucutre is an Array that holds the current assignments of all the varaibles. If the variable has not been assigned yet, the assignment for that variable will be initialized as -1.

### Compiling

The main method to use to compile this project is still the same. The MyParser.java file should be used to compile the projects. In the MyParser.java file, the file will look for all the flags given and the agruments behind the flags, which should be seperated by spaces. The flag needs to be placed before the agrument itself; however, the ordering of the which flags does not matter. Here is an example: -f ~/xmls/zebra-supports2.xml -s BT -u LX. The following are the flags built into the MyParser class:

- -f: path to the xcsp file
- -s: type of search algorithm (currently, only "BT" is implemented)
- -u: type of variable sorting heurisitc (options include: LX (lexiographical ordering), LD (least domains), DEG (degree ordering), and DD (domain to degree ratio ordering)
- -a: type of arc consistency algorithm (options include: ac1, ac3) (not used in this assignment)

## Homework 2

Tomo Bessho
2/14/2020
CSCE 421

This assignment was for me to use the data structures made in assignment 1 and implement AC-1 and AC-3 to reduce the domains of all the variables. I made two new classes, one called searchFunctions (I will change this name later) and ACAlgorithms. The search functions have the three main functions needed in the AC algorithms, check, supported, and revised. The check function takes in the argument of two vvps and returns whether the two vvps are supported or conflicted by the constraint shared between them. The supported function takes in a vvp and a variable to check each value of the second variable to see if that variable supports the current vvp. Lastly, the revised function takes in two variables and returns whether there is a domain change or not by running the supported functions for all the values in the first variable's domain.

The AC-1 and AC-3 algorithms use the revised function to determine if there has been a change made within their respective algorithms. The AC-1 uses a static queue that holds all the relations (in both directions) of all the constraints and the problem and runs revised for each relation to see if it can filter out any values in the domains of any of the variables. This will run until there are no more changes can be made in any of the domains of the variables. AC-3 improves upon this by using a dynamic queue that holds only the relations of the constraints that are affected by a domain change. The number of constraint checks, cpu time, fval, isize, fsize, and feffect are dislayed after runnning each algorithm.

The program is run through the MyParser.java class, where it takes in two arguments marked by the flags -f and -a. -f distinguishes which file to run, and -a distingushes which algorithm to run ("ac1" for AC-1 and "ac3" for AC-3). The program then makes a myProblem instance, a data structure that holds all the constraints, variables, and whether it is an extension problem or not, and feeds the myProblem instance into the ACAlgorithms class to run the algorithm.

## Homework 1

The purpose of this assignment is to parse a CSP XML file and parse it in a readable format. The parser will take in a CSP XML file (done in /src/csp/MyParser.java), parse the data, and format it in a readable way for the user to read. The MyParser.java is considered the driver class, and should be run from this script.
The parser class reads in the problem name, all the variables, and the constraints from the XML file and then puts this information in a problem data structure (called /src/csp/MyProblem.java). The MyProblem data structure takes the constraints and variables and fills in the necessary information for the two data structures.
The MyVariable data structure (foudn in /src/csp/MyVariable.java) holds the name of the variable, the initial domain and current domain, the constraints that use that particular variable, and all of its neighbors. The toString() method was overriden to format the output for the webgrader.
The MyConstraint data structure (found in /src/csp/MyConstraint.java) is a parent class for two children data structures, MyExtensionConstraint and MyIntensionConstraint. As the name says, these hold the information for an extension constraint and an intension constraint, respectively. That way, we can distinguish the difference between the two easily with a type check in the future. It is beneficial to have two different data structures since they require different functionalities and hold different information. Their parent class, MyConstraint, holds the name of the Constraint and the scope of the constraint. The toString() methods for all three classes were also overriden for formatting reasons. A MyFunction data structure was also created (found in /src/csp/MyFunction.java) to hold information for the intension constraints.
The organization and usages of these different data structures should lead to easier access to information needed later when implementing constraint solving algorithms.
