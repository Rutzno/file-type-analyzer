package analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * @author Mack_TB
 * @version 1.0.7
 * @since 02/02/2021
 */

public class Main {

    static SearchContext context = new SearchContext();

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Please provide 2 parameters in command line's argument (FileToAnalyze pattern.db");
            return;
        }

        String algType = "--RabinKarp";
        String fileName = args[0];
        String patternFileName = args[1];

        final SearchStrategy alg = create(algType);
        context.setAlgorithm(alg);

        File directory = new File(fileName);
        File[] listFiles = directory.listFiles();
        File patternsFile = new File(patternFileName);
        List<Callable<String>> callables = new ArrayList<>();

        if (listFiles != null) {
            StringBuilder responses = new StringBuilder();
            for (File child : listFiles) {
                responses.append(getResponse(callables, child, patternsFile));
            }
            System.out.println(responses);
        } else {
            System.out.println(getResponse(callables, directory, patternsFile));
        }
    }

    private static String getResponse(List<Callable<String>> callables, File child, File patternsFile) {
        String response = null;
        try (Scanner scanner = new Scanner(patternsFile)) {
            while (scanner.hasNext()) {
                String dataLine = scanner.nextLine();
                FileType fileType = FileType.create(dataLine);
                callables.add(() -> {
                    boolean matched;
                    matched = child.getParent() != null ?
                            context.searchPattern(child.getParent() + "\\" + child.getName(), fileType.getPattern()) :
                            context.searchPattern(child.getName(), fileType.getPattern());
                    String res = matched ? fileType.getResult() : "Unknown file type";
                    return String.format("%d:%s", fileType.getPriority(), res);
                });
            }

            ExecutorService executor = Executors.newCachedThreadPool();
            List<Future<String>> futures = executor.invokeAll(callables);
            List<Future<String>> matchedFutures = new ArrayList<>();
            List<Integer> matchedPriorities = new ArrayList<>();
            for (Future<String> future : futures) {
                String[] res = future.get().split(":");
                if (!"Unknown file type".equals(res[1])) {
                    matchedPriorities.add(Integer.parseInt(res[0]));
                    matchedFutures.add(future);
                }
            }
            if (matchedPriorities.size() > 0) {
                int[] priorities = new int[matchedPriorities.size()];
                Arrays.setAll(priorities, matchedPriorities::get);
                mergeSort(priorities, 0, priorities.length);
                int finalPriority = priorities[priorities.length - 1];
                for (Future<String> future : matchedFutures) {
                    String[] res = future.get().split(":");
                    if (finalPriority == Integer.parseInt(res[0])) {
                        response = String.format("%s: %s\n", child.getName(), res[1]);
                        break;
                    }
                }
            } else {
                response = String.format("%s: %s\n", child.getName(), "Unknown file type");
            }
            executor.shutdown();
        } catch (FileNotFoundException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        callables.clear();
        return response;
    }

    private static SearchStrategy create(String algType) {
        switch (algType) {
            case "--naive":
                return new NaiveSearchStrategy();
            case "--KMP":
                return new KMPSearchStrategy();
            case "--RabinKarp":
                return new RabinKarpSearchStrategy();
            default:
                throw new IllegalArgumentException("Unknown algorithm type " + algType);
        }
    }

    private static void mergeSort(int[] array, int leftIncl, int rightExcl) {
        // the base case: if subarray contains <= 1 items, stop dividing because it's sorted
        if (rightExcl <= leftIncl + 1) {
            return;
        }

        /* divide: calculate the index of the middle element */
        int middle = leftIncl + (rightExcl - leftIncl) / 2;

        mergeSort(array, leftIncl, middle);  // conquer: sort the left subarray
        mergeSort(array, middle, rightExcl); // conquer: sort the right subarray

        /* combine: merge both sorted subarrays into sorted one */
        merge(array, leftIncl, middle, rightExcl);
    }

    private static void merge(int[] array, int left, int middle, int right) {
        int i = left;   // index for the left subarray
        int j = middle; // index for the right subarray
        int k = 0;      // index for the temp subarray

        int[] temp = new int[right - left]; // temporary array for merging

        /* get the next lesser element from one of two subarrays
           and then insert it in the array until one of the subarrays is empty */
        while (i < middle && j < right) {
            if (array[i] <= array[j]) {
                temp[k] = array[i];
                i++;
            } else {
                temp[k] = array[j];
                j++;
            }
            k++;
        }

        /* insert all the remaining elements of the left subarray in the array */
        for (; i < middle; i++, k++) {
            temp[k] = array[i];
        }

        /* insert all the remaining elements of the right subarray in the array */
        for (; j < right; j++, k++) {
            temp[k] = array[j];
        }

        /* effective copying elements from temp to array */
        System.arraycopy(temp, 0, array, left, temp.length);
    }


    private static boolean checkPattern(String fileName, String pattern) {
        byte[] bytePattern = pattern.getBytes();
        int size = bytePattern.length;
        boolean matched = false;
        try{
            byte[] allBytes = Files.readAllBytes(Paths.get(fileName));
            int j = 0;
            for (byte b : allBytes) {
                if (b == bytePattern[j]) {
                    if (j == size - 1) {
                        matched = true;
                        break;
                    }
                    j++;
                } else {
                    j = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matched;
    }
}
