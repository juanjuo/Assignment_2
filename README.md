# Assignment 2 - Ranked Choice Voting
Juan Diego Mora 
CMSC375 - Software Development 
02/12/24

## INTRO

I created an extra class that generates random data for the Election class, 
this one takes 2 arguments, Number of Candidates and Number of Voters. and 
outputs an election_data.txt with the right formatting. 

## Some information about the code/ things that can be improved

in the RankedChoiceVoting, although the program throws an error when the number of
candidates(first line in the .txt) is less than the number of names for candidates.
it doesn't throw an error when the number of candidates is GREATER than the number
of names. leading to some interesting results (see case_1 in Test_Cases)

The GenerateData class selects letters from "a" to "z" for the names of the 
Candidates, therefore it will break if trying to input more than 26 Candidates. 
FIXED! I just changed it for it to be numbers instead of letters, less buggy but less
fun :(
 
The selectWinner method could be condensed.

## Test Cases

I created some test cases that thought would be useful to test the validity 
of my code, these Test Cases are in the Test_Cases folder and I run them through
my program using the command line. These test provide validity for:

case_2:
Number of losers is 1 every round, number of winners is 1 every round.
output = Winner is 3

case_3:
Number of losers is tied, number of winners is 1
output = Winner is 3

case_4:
Number of losers is 1, number of winners is tied
output = Tie! 2 and 3

case_5:
Number of losers is tied, number of winners is tied
output = Tie! 2 and 3

case_6: 
No votes for 1 of the candidates, Instantly lose
output = 
No Votes! candidate 1 is eliminated
Winner is 2

case_7:
All votes for one of the candidates, Instantly wins
output = Winner is 4

case_8:
Multiple winners! in case there's a tie since the beginning
output = Winner is 1, 2, 3, 4