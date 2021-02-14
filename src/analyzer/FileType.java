package analyzer;

/**
 * @author Mack_TB
 * @version 1.0.7
 * @since 02/02/2021
 */

public class FileType {

    private final int priority;
    private final String pattern;
    private final String result;

    private FileType(int priority, String pattern, String result) {
        this.priority = priority;
        this.pattern = pattern;
        this.result = result;
    }

    public int getPriority() {
        return priority;
    }

    public String getPattern() {
        return pattern;
    }

    public String getResult() {
        return result;
    }

    public static FileType create(String dataLine) {
        String[] data = dataLine.replaceAll("\"", "").split(";");
        return new FileType(Integer.parseInt(data[0]), data[1], data[2]);
    }
}
