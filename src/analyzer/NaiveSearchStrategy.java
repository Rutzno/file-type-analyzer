package analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Mack_TB
 * @version 1.0.7
 * @since 02/02/2021
 */

public class NaiveSearchStrategy implements SearchStrategy {

    @Override
    public boolean search(String fileName, String pattern) {
        boolean patternIsFound;
        try {
            byte[] allBytes = Files.readAllBytes(Paths.get(fileName));
            String text = new String(allBytes);
            if (text.length() < pattern.length()) {
                return false;
            }
            for (int i = 0; i < text.length() - pattern.length() + 1; i++) {
                patternIsFound = true;
                for (int j = 0; j < pattern.length(); j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        patternIsFound = false;
                        break;
                    }
                }
                if (patternIsFound) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
