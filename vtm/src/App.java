import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Input: ");
        String rolls = input.nextLine();
        int numberOfDice;
        int maxDice;

        Random randomGenerator = new Random();

        int indexD = rolls.lastIndexOf("d");
        numberOfDice = Integer.parseInt(rolls.substring(0, indexD));
        maxDice = Integer.parseInt(rolls.substring(indexD + 1));
        System.out.println("numberOfDice = " + numberOfDice);
        System.out.println("maxDice = " + maxDice);

        float[][] results = new float[maxDice][4];
        int[][] doubleSuccessResults = new int[maxDice][4];
        int[] randomRolls = new int[numberOfDice];
        int[] currentSuccess;
        int currentFail = 0;
        int currentIndex = 0;

        for (int i = 0; i < 10000; i++) {
            currentSuccess = new int[maxDice];
            currentFail = 0;

            // creating random rolls
            for (int j = 0; j < numberOfDice; j++) {
                randomRolls[j] = 1 + randomGenerator.nextInt(10);
            }

            // sorting
            Arrays.sort(randomRolls);

            // finding total fails
            for (int j = 0; j < numberOfDice; j++) {
                if (randomRolls[j] == 1) {
                    currentFail++;
                } else {
                    break;
                }
            }

            // finding the success numbers for each turn
            currentIndex = currentFail;
            for (int threshold = 2; threshold <= maxDice; threshold++) {
                for (int index = currentIndex; index < numberOfDice; index++) {
                    if (threshold <= randomRolls[index]) {
                        currentSuccess[threshold - 1] = numberOfDice - index; // ??????????
                        //currentIndex = index + 1;
                        break;
                    }
                }
            }

            // botches and other statistics
            for (int threshold = 1; threshold < maxDice; threshold++) {
                if ((currentSuccess[threshold] == 0) && (currentFail > 0)) {
                    // its a botch
                    results[threshold][3] = results[threshold][3] + 1;
                } else if (currentSuccess[threshold] < currentFail) {
                    // fail move
                    results[threshold][2] = results[threshold][2] + 1;
                } else if (currentSuccess[threshold] > currentFail) {
                    // success!
                    results[threshold][0] = results[threshold][0] + (currentSuccess[threshold] - currentFail);
                    results[threshold][1] = results[threshold][1] + 1;
                }
            }
        }

        // final statistics
        for (int threshold = 1; threshold < maxDice; threshold++) {
            // average success
            results[threshold][0] = results[threshold][0] / 10000;
            // success rate
            results[threshold][1] = (results[threshold][1] / 10000) * 100;
            // fail rate
            results[threshold][2] = (results[threshold][2] / 10000) * 100;
            // botch rate
            results[threshold][3] = (results[threshold][3] / 10000) * 100;
        }


        //prints
        System.out.println("Threshold\tSuccess\t\tSuccessRate\tFaiRate\t\tBotchRate");
        for (int i = 1; i < results.length; i++) { // this equals to the row in our matrix.
            System.out.print((i+1) + " \t\t");
            for (int j = 0; j < results[i].length; j++) { // this equals to the column in each row.
                System.out.printf("%.2f", results[i][j]);
                System.out.print("\t\t");
            }
            System.out.println(); // change line on console as row comes to end in the matrix.
        }
    }
}
