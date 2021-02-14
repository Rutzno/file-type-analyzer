package analyzer;

/**
 * @author Mack_TB
 * @version 1.0.7
 * @since 02/02/2021
 */

public class SearchContext {
    private SearchStrategy algorithm;

    public void setAlgorithm(SearchStrategy algorithm) {
        this.algorithm = algorithm;
    }

    public boolean searchPattern(String text, String pattern) {
        return algorithm.search(text, pattern);
    }
}
