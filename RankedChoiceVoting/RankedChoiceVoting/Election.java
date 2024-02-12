import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An Election consists of the candidates running for office, the ballots that 
 * have been cast, and the total number of voters.  This class implements the 
 * ranked choice voting algorithm.
 * 
 * Ranked choice voting uses this process:
 * 
 * Rather than vote for a single candidate, a voter ranks all the 
 * candidates.  For example, if 3 candidates are running on the ballot, a voter
 * identifies their first choice, second choice, and third choice.
 * 
 * The first-choice votes are tallied.  If any candidate receives > 50% 
 * of the votes, that candidate wins.
 * 
 * If no candidate wins > 50% of the votes, the candidate(s) with the 
 * lowest number of votes is(are) eliminated.  For each ballot in which an
 * eliminated candidate is the first choice, the 2nd ranked candidate is now
 * the top choice for that ballot.
 * 
 * Steps 2 & 3 are repeated until a candidate wins, or all remaining 
 * candidates have exactly the same number of votes.  In the case of a tie, 
 * there would be a separate election involving just the tied candidates.
 * 
 */
public class Election {
    // All candidates that were in the election initially.  If a candidate is 
    // eliminated, they will still stay in this array.
    private final Candidate[] candidates;
    
    // The next slot in the candidates array to fill.
    private int nextCandidate;

    private int numVoters; //ADDED to keep track of how many voters there are in the election
    
    /**
     * Create a new Election object.  Initially, there are no candidates or 
     * votes.
     * @param numCandidates the number of candidates in the election.
     */
    public Election (int numCandidates) {
        this.candidates = new Candidate[numCandidates];
    }
    
    /**
     * Adds a candidate to the election
     * @param name the candidate's name
     */
    public void addCandidate (String name) { //only used at initializeElection
        candidates[nextCandidate] = new Candidate (name);
        nextCandidate++;
    }
    
    /**
     * Adds a completed ballot to the election.
     * @param ranks A correctly formulated ballot will have exactly 1 
     * entry with a rank of 1, exactly one entry with a rank of 2, etc.  If 
     * there are n candidates on the ballot, the values in the rank array 
     * passed to the constructor will be some permutation of the numbers 1 to 
     * n.
     * @throws IllegalArgumentException if the ballot is not valid.
     */
    public void addBallot (int[] ranks) { //only used at the beginning of initializeElection()
        if (!isBallotValid(ranks)) {
            throw new IllegalArgumentException("Invalid ballot");
        }
        Ballot newBallot = new Ballot(ranks);
        numVoters++; //ADDED
        assignBallotToCandidate(newBallot);
    }

    /**
     * Checks that the ballot is the right length and contains a permutation 
     * of the numbers 1 to n, where n is the number of candidates.
     * @param ranks the ballot to check
     * @return true if the ballot is valid.
     */
    private boolean isBallotValid(int[] ranks) {
        if (ranks.length != candidates.length) {
            return false;
        }
        int[] sortedRanks = Arrays.copyOf(ranks, ranks.length);
        Arrays.sort(sortedRanks);
        for (int i = 0; i < sortedRanks.length; i++) {
            if (sortedRanks[i] != i + 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines which candidate is the top choice on the ballot and gives the
     * ballot to that candidate.
     * @param newBallot a ballot that is not currently assigned to a candidate
     */
    private void assignBallotToCandidate(Ballot newBallot) {// used to assign Ballots after elimination
            int candidate = newBallot.getTopCandidate(); //returns index of top CANDIDATE
            candidates[candidate].addBallot(newBallot); //accesses top CANDIDATE and assigns Ballot
    }

    /**
     * Finds the Candidates with the max number of votes
     * returns the indexes of the most voted Candidates
     * //ADDED//
     */
    private List<Integer> getTopCandidate(){
        List<Integer> winningCandidates = new ArrayList<>(); //array with winning candidates
        int maxVotes = 0;// max num of votes

        // Find the candidate with the highest number of votes.
        for (int i = 0; i < candidates.length; i++) {
            if (!candidates[i].isEliminated() && candidates[i].getVotes() > maxVotes) {
                maxVotes = candidates[i].getVotes();
            }
        }
        // looks for candidates with the same number of votes and puts them in the Array
        for (int i = 0; i < candidates.length; i++) {
            if (candidates[i].getVotes() == maxVotes){
                winningCandidates.add(i);
            }
        }
        return winningCandidates;
    }

    /**
     * Finds the Candidates with the lowest number of votes
     * returns the indexes of the least voted candidates
     * //ADDED//
     */
    private List<Integer> getLowerCandidate(){
        List<Integer> losingCandidates = new ArrayList<>(); //array with losing candidates
        int minVotes = numVoters;// min num of Votes

        // Find the candidate with the lowest number of votes.
        for (int i = 0; i < candidates.length; i++) {
            if (!candidates[i].isEliminated() && candidates[i].getVotes() < minVotes) {
                minVotes = candidates[i].getVotes();
            }
        }
        // looks for candidates with the same number of votes and puts them in the Array
        for (int i = 0; i < candidates.length; i++) {
            if (candidates[i].getVotes() == minVotes){
                losingCandidates.add(i);
            }
        }
        return losingCandidates;
    }

    /**
     * Apply the ranked choice voting algorithm to identify the winner.
     * @return If there is a winner, this method returns a list containing just
     * the winner's name is returned.  If there is a tie, this method returns a
     * list containing the names of the tied candidates.
     */
    public List<String> selectWinner () {
        List<String> winner = new ArrayList<>();
        int numActiveCandidates = candidates.length; //n of candidates that are still in the election
        List<Integer> topCandidates;
        List<Integer> lowerCandidates = getLowerCandidate();

        //if a candidate has 0 votes, instantly loses
        for (int i = 0; i < lowerCandidates.size(); i++){
            Candidate loser = candidates[lowerCandidates.get(i)];
            if(loser.getVotes() == 0){
                loser.eliminate();
                numActiveCandidates--;
                System.out.println("No Votes! candidate " + loser.getName() + " is eliminated");
            }
        }

        while (true){
            topCandidates = getTopCandidate();
            lowerCandidates = getLowerCandidate();
            //if only 1 winner and votes > than 50%, instantly wins
            if(topCandidates.size() == 1 && candidates[topCandidates.get(0)].getVotes() > numVoters/2){
                winner.add(candidates[topCandidates.get(0)].getName());
                break;//wins
            }
            // if there's same number of topCandidates and Active Candidates (Tied)
            if(topCandidates.size() == numActiveCandidates) {
                for(int i = 0; i < topCandidates.size(); i++){
                    winner.add(candidates[topCandidates.get(i)].getName());
                }
                break; //win
            }
            // if there's 2 losers or more, then delete 1 from election (and print who was deleted)
            if (lowerCandidates.size() > 1){
                int losingCandidate = lowerCandidates.get(0);
                numActiveCandidates--; //delete from active candidates
                System.out.println("Candidate " + candidates[losingCandidate].getName() + " was deleted! with "
                        + candidates[losingCandidate].getVotes() + " votes");
                List<Ballot> loserBallots = candidates[losingCandidate].eliminate();//delete candidate from election
                for(int i = 0; i < loserBallots.size(); i++){
                    Ballot losingBallot = loserBallots.get(i);
                    losingBallot.eliminateCandidate(losingCandidate);// delete candidate from individual ballot
                    assignBallotToCandidate(losingBallot);// assign ballot to new candidate
                }
            }
            // if only 1 loser, then delete it from election
            if (lowerCandidates.size() == 1){
                int losingCandidate = lowerCandidates.get(0);
                numActiveCandidates--;
                List<Ballot> loserBallots = candidates[losingCandidate].eliminate();//delete candidate from election
                for(int i = 0; i < loserBallots.size(); i++){
                    Ballot losingBallot = loserBallots.get(i);
                    losingBallot.eliminateCandidate(losingCandidate);// delete candidate from individual ballot
                    assignBallotToCandidate(losingBallot);// assign ballot to new candidate
                }
            }
        }
        return winner;
    }
}
