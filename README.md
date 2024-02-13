# Assignment 2 - Ranked Choice Voting
Juan Diego Mora 
CMSC375 - Software Development 
02/12/24

## INTRO

I created an extra class that generates random data for the Election class, this one takes 2 arguments, Number of Candidates and Number of Voters. and outputs an election_data.txt with the right formatting. 

## Some information about the code/ things that can be improved

The GenerateData class selects letters from "a" to "z" for the names of the Candidates, therefore it will break if trying to input more than 26 Candidates. 
 
The selectWinner method has a lot of code duplication that could be condensed.

I still don't know what to do when there's tied candidates for the elimination round! the way the selectWinner method does it so far is deleting the first loser and keep running the next iteration.

If- else if() statements can be implemented
