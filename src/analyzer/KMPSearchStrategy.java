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
 * The Knuth-Morris-Pratt algorithm is an approach that allows solving
 * the substring searching problem in linear time in the worst case.
 * The algorithm compares a pattern with substrings of a text trying
 * to find a complete matching. To decrease the number of comparisons,
 * it uses the prefix function for finding an optimal pattern shift.
 */

public class KMPSearchStrategy implements SearchStrategy {

    @Override
    public boolean search(String fileName, String pattern) {
//        List<Integer> occurrences = new ArrayList<>();
        int[] prefixFunc = prefixFunction(pattern);
        int j = 0;
        try {
            byte[] allBytes = Files.readAllBytes(Paths.get(fileName));
            String text = new String(allBytes);
//            System.out.println("text:"+text);
            for (int i = 0; i < text.length(); i++) {
                while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                    j = prefixFunc[j - 1];
                }
                if (text.charAt(i) == pattern.charAt(j)) {
                    j += 1;
                }
                if (j == pattern.length()) {
                    return true;
                /*occurrences.add(i - j + 1);
                j = prefixFunc[j - 1];*/
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return occurrences;
        return false;
    }

    public static int[] prefixFunction(String str) {
        int[] prefixFunc = new int[str.length()];

        for (int i = 1; i < str.length(); i++) {
            int j = prefixFunc[i -1];
            while (j > 0 && str.charAt(i) != str.charAt(j)) {
                j = prefixFunc[j - 1];
            }
            if (str.charAt(i) == str.charAt(j)) {
                j++;
            }
            prefixFunc[i] = j;
        }

        return prefixFunc;
    }
}
