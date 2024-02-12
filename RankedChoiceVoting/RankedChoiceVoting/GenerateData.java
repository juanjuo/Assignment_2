import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Juan Diego Mora
 * Friday 02/11/24
 * This class generates random data for an Election and outputs to a .txt file
 * param args number of candidates, number of voters
 */
public class GenerateData {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println ("Please provide number of candidates " +
                    "and the number of voters on the " +
                    "command line.");
            return;
        }

        int numCandidates = Integer.parseInt(args[0]);
        int numVoters = Integer.parseInt(args[1]);

        FileWriter file = null;
        try {
            file = new FileWriter("election_data.txt");


            //write number of candidates
            file.write(Integer.toString(numCandidates));
            file.write(10);// new line

            //write candidate names
            for (int i = 97; i < 97+numCandidates; i++) {//writing letters from "a" to "z" in ASCII Decimal
                file.write(i);
                file.write(10); //new line
            }

            //write ballot for each voter

            //Initialize ballot to [1, 2, ...n of candidates]
            List<Integer> maxCandidates = new ArrayList<>();
            for (int i = 0; i < numCandidates; i++) {
                maxCandidates.add(i + 1);
            }
            //Shuffle array and write to file!
            for (int i = 0; i < numVoters; i++) {
                Collections.shuffle(maxCandidates);
                for (int p = 0; p < numCandidates; p++) {
                    file.write(Integer.toString(maxCandidates.get(p)));
                    file.write(32);//space between the numbers
                }
                file.write(10);//new line
            }
        }
        finally {
            if (file != null) {
                System.out.println("File Written Successfully!");
                file.close();
            }
            else{System.out.println("ERROR WITH FILE!");}
        }

    }
}
