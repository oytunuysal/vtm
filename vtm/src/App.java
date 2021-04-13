import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("Input (e.g. 5d10): ");
            String rolls = input.nextLine();
            int numberOfDice;
            int maxDice;

            // Random randomGenerator = new Random();

            int indexD = rolls.lastIndexOf("d");
            numberOfDice = Integer.parseInt(rolls.substring(0, indexD));
            maxDice = Integer.parseInt(rolls.substring(indexD + 1));
            System.out.println("Number Of Dice = " + numberOfDice);
            System.out.println("Max Dice = " + maxDice);

            float[][] results = new float[maxDice][5];
            float[][] doubleSuccessResults = new float[maxDice][5];
            int[] randomRolls = new int[numberOfDice];
            int[] currentSuccess;
            int[] currentDoubleSuccess;
            int currentFail = 0;
            int currentIndex = 0;

            int rounds = 100000;

            for (int i = 0; i < rounds; i++) {
                currentSuccess = new int[maxDice];
                currentDoubleSuccess = new int[maxDice];
                currentFail = 0;
                // creating random rolls
                for (int j = 0; j < numberOfDice; j++) {
                    // randomRolls[j] = 1 + randomGenerator.nextInt(10);
                    randomRolls[j] = (int) getRandomIntegerBetweenRange(1, 10);
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
                            currentSuccess[threshold - 1] = numberOfDice - index; // for non speciality dices
                            currentDoubleSuccess[threshold - 1] = numberOfDice - index; // for actions with specialities
                            if (threshold == 10) {
                                for (int a = 1; a < maxDice; a++) {
                                    currentDoubleSuccess[a] = currentDoubleSuccess[a] + (numberOfDice - index);
                                }
                            }
                            // currentIndex = index + 1;
                            break;
                        }
                    }
                }

                statistics(results, currentSuccess, currentFail, maxDice);
                statistics(doubleSuccessResults, currentDoubleSuccess, currentFail, maxDice);

            }

            calculateStatistics(results, maxDice, rounds);
            calculateStatistics(doubleSuccessResults, maxDice, rounds);

            printResults(results);
            printResults(doubleSuccessResults);
        }
    }

    private static double getRandomIntegerBetweenRange(double min, double max) {
        double x = (int) (Math.random() * ((max - min) + 1)) + min;
        return x;
    }

    static private void statistics(float[][] results, int[] currentSuccess, int currentFail, int maxDice) {
        // botches and other statistics
        for (int threshold = 1; threshold < maxDice; threshold++) {
            if ((currentSuccess[threshold] == 0) && (currentFail > 0)) {
                // botch
                results[threshold][4] = results[threshold][4] + 1;
            } else if (currentSuccess[threshold] <= currentFail) {
                // ingame-fail move
                results[threshold][2] = results[threshold][2] + 1;
                if(currentSuccess[threshold] < currentFail){
                    // fail move
                    results[threshold][3] = results[threshold][3] + 1;
                }
            } else if (currentSuccess[threshold] > currentFail) {
                // success!
                results[threshold][0] = results[threshold][0] + (currentSuccess[threshold] - currentFail);
                results[threshold][1] = results[threshold][1] + 1;
            }
        }
    }

    static private void calculateStatistics(float[][] results, int maxDice, int rounds) {
        // final statistics
        for (int threshold = 1; threshold < maxDice; threshold++) {
            // average success
            results[threshold][0] = results[threshold][0] / rounds;
            // success rate
            results[threshold][1] = (results[threshold][1] / rounds) * 100;
            // ingame-fail rate
            results[threshold][2] = (results[threshold][2] / rounds) * 100;
            // fail rate
            results[threshold][3] = (results[threshold][3] / rounds) * 100;
            // botch rate
            results[threshold][4] = (results[threshold][4] / rounds) * 100;
        }
    }

    static private void printResults(float[][] results) {
        // prints
        System.out.println("Threshold\tSuccess\t\tSuccessRate\tFailRate\tNegativeRate\tBotchRate");
        for (int i = 1; i < results.length; i++) { // this equals to the row in our matrix.
            System.out.print((i + 1) + " \t\t");
            for (int j = 0; j < results[i].length; j++) { // this equals to the column in each row.
                System.out.printf("%.2f", results[i][j]);
                System.out.print("\t\t");
            }
            System.out.println(); // change line on console as row comes to end in the matrix.
        }
    }
}
