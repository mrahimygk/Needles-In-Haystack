package ir.mrahimy.needles_in_haystack;

import org.fluttercode.datafactory.impl.DataFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

/**
 * @author mojtaba rahimy
 */
public class Main {

    static String OUTPUT = "output.txt", EPOCH = "epoch.txt";

    public static void main(String[] args) {
        String bigOne;
        String smallOne;
        StringBuilder result = new StringBuilder();

        int n = 1000000;
        if (args.length == 1) {
            try {
                n = Integer.parseInt(args[0]);
            } catch (IllegalFormatException e) {
                e.printStackTrace();
            }
        }

        boolean[] truthTable = new boolean[n];

        DataFactory factory = new DataFactory();

        long t = 0L;
        int i = 0;
        while (n > 0) {
            // NOTE : this will deprive the first lines of isSequential(String, String)
            // the first purpose was to ask users to input data
            bigOne = factory.getRandomChars(20);//scanner.nextLine();
            smallOne = shuffle(bigOne).substring(0, 5); //scanner.nextLine();

            // will writeOutput this to a file to check the results
            result.append("searching ").append(smallOne).append(" in ").append(bigOne);

            // we just record boolean data instead of 'sout' to measure the elapsed time correctly
            long t1 = System.currentTimeMillis();
            truthTable[i] =
                    isSequential(bigOne, smallOne) ||
                            isSequential(new StringBuilder(bigOne).reverse().toString(),
                                    smallOne);
            long t2 = System.currentTimeMillis();

            t += t2-t1;

            result.append(truthTable[i] ? " --> YES" : " --> NO").append("\n");

            i++;
            n--;
        }

        System.out.println("cpu time: " + (t) + " ms.");

        //result.insert(0, "=======\nCurrent Epoch : " +String.valueOf(readEpoch())+"\n=======\n");
        result.insert(0, "\n=======\n");
        writeOutput(result.toString());
    }

    private static int readEpoch() {
        int res = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(EPOCH))) {
            String sCurrentLine;
            if ((sCurrentLine = br.readLine()) != null){
                sCurrentLine = sCurrentLine.trim();
                try
                {
                    res= Integer.parseInt(sCurrentLine);
                }catch (NumberFormatException we){
                    we.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    private static void writeOutput(String data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT, true))) {
            bw.write(data);
            //increaseEpoch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void increaseEpoch() {
        int epoch = readEpoch();
        epoch++;
        writeEpoch(epoch);
    }

    private static void writeEpoch(int epoch) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EPOCH))) {
            bw.write(epoch);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String shuffle(String input) {
        List<Character> characters = new ArrayList<Character>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while (characters.size() != 0) {
            int randPicker = (int) (Math.random() * characters.size());
            output.append(characters.remove(randPicker));
        }
        return (output.toString());
    }

    private static boolean isSequential(String bigOne, String smallOne) {
        int i = 0;
        String[] subs = new String[smallOne.length()];

        for (char c : smallOne.toCharArray()) {
            if (!bigOne.contains(String.valueOf(c))) {
                return false;
            }

            if (bigOne.contains(smallOne)) return true;

            subs[i++] = bigOne.substring(bigOne.indexOf(c));
            bigOne = bigOne.substring(bigOne.indexOf(c));
        }

        return isDescendingElementLength(subs);
    }

    private static boolean isDescendingElementLength(String[] subs) {
        int prev = subs[0].length() + 10;
        for (String s : subs) {
            int neu = s.length();
            if (neu < prev) {
                prev = neu;
            } else return false;
        }
        return true;
    }
}
