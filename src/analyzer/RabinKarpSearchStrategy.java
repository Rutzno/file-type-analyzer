package analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Mack_TB
 * @version 1.0.7
 * @since 02/02/2021
 */

/**
 * Rabin-Karp algorithm is used for Substring searching.
 * It uses string hashing (a technique for associating a
 * string with a number) for a faster comparison
 * thus significantly reducing the total running time
 * compared with the naive approach.
 */

public class RabinKarpSearchStrategy implements SearchStrategy {

    @Override
    public boolean search(String fileName, String pattern) {

        try {
            byte[] allBytes = Files.readAllBytes(Paths.get(fileName));
            String text = new String(allBytes);
            if (text.length() < pattern.length()) {
                return false;
            }

            int a = 53;
            long m = 1_000_000_000 + 9;

            long patternHash = 0;
            long currSubstrHash = 0;
            long pow = 1;

            for (int i = 0; i < pattern.length(); i++) {
                patternHash += charToLong(pattern.charAt(i)) * pow;
                patternHash %= m;

                currSubstrHash += charToLong(text.charAt(text.length() - pattern.length() + i)) * pow;
                currSubstrHash %= m;

                if (i != pattern.length() - 1) {
                    pow = pow * a % m;
                }
            }
//      ArrayList<Integer> occurrences = new ArrayList<>();

            for (int i = text.length(); i >= pattern.length(); i--) {
                if (patternHash == currSubstrHash) {
                    boolean patternIsFound = true;

                    for (int j = 0; j < pattern.length(); j++) {
                        if (text.charAt(i - pattern.length() + j) != pattern.charAt(j)) {
                            patternIsFound = false;
                            break;
                        }
                    }

                    if (patternIsFound) {
//                    occurrences.add(i - pattern.length());
                        return true;
                    }
                }

                if (i > pattern.length()) {
                    currSubstrHash = (currSubstrHash - charToLong(text.charAt(i - 1)) * pow % m + m) * a % m;
                    currSubstrHash = (currSubstrHash + charToLong(text.charAt(i - pattern.length() - 1))) % m;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Collections.reverse(occurrences);
//        return occurrences;
        return false;
    }

    public static long charToLong(char ch) {
        return ch;
    }
}
