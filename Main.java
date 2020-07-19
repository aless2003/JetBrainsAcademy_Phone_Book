package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static Pattern numberPattern = Pattern.compile("(\\d+) ([()`.'a-zA-Z-]+ ?[()`a-zA-Z-',]*)");

    static ArrayList<String> foundNumbers = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException {
        File directory = new File("C:\\Users\\aless\\IntelliJIDEAProjects\\Phone Book\\Phone Book\\task\\src\\phonebook\\directory.txt");
        File find = new File("C:\\Users\\aless\\IntelliJIDEAProjects\\Phone Book\\Phone Book\\task\\src\\phonebook\\find.txt");

        Scanner fileScanner = new Scanner(directory);
        ArrayList<String> numberList = new ArrayList<>();
        ArrayList<String> searchedUsers = new ArrayList<>();
        long time = 0;
        long linearTime = 0;

        while (fileScanner.hasNextLine()) {
            numberList.add(fileScanner.nextLine());
        }
        fileScanner = new Scanner(find);
        while (fileScanner.hasNextLine()) {
            searchedUsers.add(fileScanner.nextLine());
        }
        System.out.println("Start searching (linear search)...");
        time = linearSearch(searchedUsers, numberList);
        linearTime = time;
        output(searchedUsers, time);
        System.out.println("Start searching (bubble sort + jump search)...");
        ArrayList<String> numberListCopy = new ArrayList<>(numberList);
        jumpSearch(numberListCopy, searchedUsers, linearTime);
        numberListCopy = new ArrayList<>(numberList);

        binarySearch(numberList, searchedUsers);
        hashTableSearch(numberList, searchedUsers);

    }

    private static void hashTableSearch(ArrayList<String> numberList, ArrayList<String> searchedUsers) {
        long creatingTime = System.currentTimeMillis();


        System.out.println("Start searching (hash table)...");

        foundNumbers.clear();

        Hashtable<String, String> numberTable = new Hashtable<>();
        for (String number : numberList) {
            Matcher matcher = numberPattern.matcher(number);
            if (matcher.matches()) {
                numberTable.put(matcher.group(2), matcher.group(1));
            }
        }
        creatingTime = System.currentTimeMillis() - creatingTime;

        long searchingTime = System.currentTimeMillis();
        for (String searchedUser : searchedUsers) {
            if (numberTable.containsKey(searchedUser)) {
                foundNumbers.add(numberTable.get(searchedUser));
            }
        }
        searchingTime = System.currentTimeMillis() - searchingTime;
        output(searchedUsers, searchingTime, creatingTime, "hashTable");
    }

    private static void output(ArrayList<String> searchedUsers, long time) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        time -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
        time -= TimeUnit.SECONDS.toMillis(seconds);
        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. %d ms.\n\n", foundNumbers.size(), searchedUsers.size(), minutes, seconds, time);
    }


    private static void output(ArrayList<String> searchedUsers, long searchTime, long sortTime, String method) {

        long time = searchTime + sortTime;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        time -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
        time -= TimeUnit.SECONDS.toMillis(seconds);
        long searchMinutes = TimeUnit.MILLISECONDS.toMinutes(searchTime);
        searchTime -= TimeUnit.MINUTES.toMillis(searchMinutes);
        long searchSeconds = TimeUnit.MILLISECONDS.toSeconds(searchTime);
        searchTime -= TimeUnit.SECONDS.toMillis(searchSeconds);
        long sortMinutes = TimeUnit.MILLISECONDS.toMinutes(sortTime);
        sortTime -= TimeUnit.MINUTES.toMillis(sortMinutes);
        long sortSeconds = TimeUnit.MILLISECONDS.toSeconds(sortTime);
        sortTime -= TimeUnit.SECONDS.toMillis(sortSeconds);

        outputNotStopped(searchedUsers, searchTime, sortTime, time, minutes, seconds, searchMinutes, searchSeconds, sortMinutes, sortSeconds, method);
    }

    private static void outputNotStopped(ArrayList<String> searchedUsers, long searchTime, long sortTime, long time, long minutes, long seconds, long searchMinutes, long searchSeconds, long sortMinutes, long sortSeconds, String method) {
        System.out.println("Found " + foundNumbers.size() + " / " + searchedUsers.size() + " entries. Time taken: " + minutes + " min. " + seconds + " sec. " + time + " ms.");
        if (method.equals("binary") || method.equals("linear")) {
            System.out.print("Sorting time: ");
        } else {
            System.out.print("Creating time: ");
        }
        System.out.println(sortMinutes + " min. " + sortSeconds + " sec. " + sortTime + " ms.");
        System.out.println("Searching time: " + searchMinutes + " min. " + searchSeconds + " sec. " + searchTime + " ms.");
    }

    private static void output(ArrayList<String> searchedUsers, long searchTime, long sortTime, boolean stopped) {
        long time = searchTime + sortTime;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        time -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
        time -= TimeUnit.SECONDS.toMillis(seconds);
        long searchMinutes = TimeUnit.MILLISECONDS.toMinutes(searchTime);
        searchTime -= TimeUnit.MINUTES.toMillis(searchMinutes);
        long searchSeconds = TimeUnit.MILLISECONDS.toSeconds(searchTime);
        searchTime -= TimeUnit.SECONDS.toMillis(searchSeconds);
        long sortMinutes = TimeUnit.MILLISECONDS.toMinutes(sortTime);
        sortTime -= TimeUnit.MINUTES.toMillis(sortMinutes);
        long sortSeconds = TimeUnit.MILLISECONDS.toSeconds(sortTime);
        sortTime -= TimeUnit.SECONDS.toMillis(sortSeconds);


        if (stopped) {
            System.out.println("Found " + foundNumbers.size() + " / " + searchedUsers.size() + " entries. Time taken: " + minutes + " min. " + seconds + " sec. " + time + " ms.");
            System.out.println("Sorting time: " + sortMinutes + " min. " + sortSeconds + " sec. " + sortTime + " ms. - STOPPED, moved to linear search");
            System.out.println("Searching time: " + searchMinutes + " min. " + searchSeconds + " sec. " + searchTime + " ms.");
        } else {
            outputNotStopped(searchedUsers, searchTime, sortTime, time, minutes, seconds, searchMinutes, searchSeconds, sortMinutes, sortSeconds, "linear");
        }

    }

    private static void Input(Scanner fileScanner, ArrayList<String> users, ArrayList<String> numbers) {
        String temp;
        String[] tempArr;
        while (fileScanner.hasNextLine()) {
            temp = fileScanner.nextLine();
            tempArr = temp.split("\\s");
            numbers.add(tempArr[0]);
            temp = tempArr[1];
            if (tempArr.length > 2) {
                temp += " " + tempArr[2];
            }
            users.add(temp);
        }
    }

    private static long linearSearch(ArrayList<String> searchedUsers, ArrayList<String> numberList) {
        String currentName = null;
        Matcher matcher;
        long beginTime = System.currentTimeMillis();
        for (String searchedUser : searchedUsers) {
            for (String currentEntry : numberList) {
                matcher = numberPattern.matcher(currentEntry);
                if (matcher.matches()) {
                    currentName = matcher.group(2);
                }
                if (searchedUser.equals(currentName)) {
                    foundNumbers.add(matcher.group(1));
                    break;
                }
            }
        }
        long endTime = System.currentTimeMillis();
        return endTime - beginTime;

    }


    private static void jumpSearch(ArrayList<String> numberList, ArrayList<String> searchedUsers, long lastSearchLength) {
        Matcher matcher;
        int jumpLength = (int) Math.sqrt(numberList.size());
        int currentRight;
        int currentLeft;
        int foundIndex;
        foundNumbers.clear();
        long sortingTime = bubbleSort(numberList, lastSearchLength);
        long beginTime = System.currentTimeMillis();
        if (sortingTime < 10 * lastSearchLength) {
            for (String searchedUser : searchedUsers) {
                System.out.println("Searching User: " + searchedUser);
                currentRight = 0;
                matcher = numberPattern.matcher(numberList.get(0));
                if (matcher.matches()) {
                    if (matcher.group(2).equals(searchedUser)) {
                        foundNumbers.add(matcher.group(1));
                    }
                }

                while (currentRight != numberList.size() - 1) {
                    currentLeft = currentRight;
                    currentRight = Math.min(currentRight + jumpLength, numberList.size() - 1);

                    if (numberList.get(currentRight).compareTo(searchedUser) < 0) {
                        foundIndex = backwardLinearSearch(currentLeft, currentRight, searchedUser, numberList);
                        if (foundIndex != -1) {
                            matcher = numberPattern.matcher(numberList.get(foundIndex));
                            if (matcher.matches()) {
                                foundNumbers.add(matcher.group(1));
                            }
                        }
                    }
                }
            }
            long endTime = System.currentTimeMillis();
            long searchTime = endTime - beginTime;
            output(searchedUsers, searchTime, sortingTime, false);
        } else {
            long seachTime = linearSearch(searchedUsers, numberList);
            output(searchedUsers, seachTime, sortingTime, true);
        }
    }
    //TODO BubbleSort Time
    private static long bubbleSort(ArrayList<String> numberList, long searchTime) {
        Matcher matcher;
        String temp;
        String curName = null;
        String nextName = null;
        long beginTime = System.currentTimeMillis();
        long endTime;
        for (int i = 0; i < numberList.size(); i++) {
            for (int j = numberList.size() - 1; j > i; j--) {
                matcher = numberPattern.matcher(numberList.get(j));
                if (matcher.matches()) {
                    curName = matcher.group(2);
                }
                matcher = numberPattern.matcher(numberList.get(j - 1));
                if (matcher.matches()) {
                    nextName = matcher.group(2);
                }
                assert nextName != null;
                assert curName != null;
                if (curName.compareTo(nextName) < 0) {
                    temp = numberList.get(j);
                    numberList.set(j, numberList.get(j - 1));
                    numberList.set(j - 1, temp);
                }
                if (searchTime * 10 <= System.currentTimeMillis() - beginTime) {
                    endTime = System.currentTimeMillis();
                    return endTime - beginTime;
                }
            }
        }
        endTime = System.currentTimeMillis();
        return endTime - beginTime;
    }

    private static int backwardLinearSearch(int currentLeft, int currentRight, String searchedUser, ArrayList<String> numberList) {
        Matcher matcher;

        for (int i = currentRight; i > currentLeft; i--) {
            matcher = numberPattern.matcher(numberList.get(i));
            if (matcher.matches()) {
                if (searchedUser.equals(matcher.group(2))) {
                    return i;
                }
            }
        }
        return -1;
    }


    private static void binarySearch(ArrayList<String> numberList, ArrayList<String> searchedUsers) {
        foundNumbers.clear();
        int leftBarrier = 0;
        int rightBarrier = numberList.size() - 1;
        System.out.println("Start searching (quick sort + binary search)...");
        int mid = 0;
        Matcher matcher;
        String tempUser;
        long sortTime = quickSort(numberList, leftBarrier, rightBarrier);
        long startTime = System.currentTimeMillis();
        for (String searchedUser : searchedUsers) {
            boolean x = true;
            leftBarrier = 0;
            rightBarrier = numberList.size() - 1;
            while (leftBarrier <= rightBarrier && x) {
                mid = (leftBarrier + rightBarrier) >>> 1;
                matcher = numberPattern.matcher(numberList.get(mid));
                if (matcher.matches()) {
                    if (matcher.group(2).equals(searchedUser)) {
                        foundNumbers.add(matcher.group(1));
                        x = false;
                    } else if (matcher.group(2).compareTo(searchedUser) > 0) {
                        rightBarrier = mid - 1;
                    } else {
                        leftBarrier = mid + 1;
                    }
                } else {
                    System.out.println(numberList.get(mid));
                }
            }
        }
        long endTime = System.currentTimeMillis();
        output(searchedUsers, endTime - startTime, sortTime, "binary");
    }


    private static long quickSort(ArrayList<String> numberList, int left, int right) {
        long startTime = System.currentTimeMillis();
        if (left < right) {
            int pivotIndex = partitioningQuickSort(numberList, left, right); // the pivot is already on its place
            quickSort(numberList, left, pivotIndex - 1);  // sort the left subarray
            quickSort(numberList, pivotIndex + 1, right);
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private static int partitioningQuickSort(ArrayList<String> numberList, int left, int right) {
        Matcher matcher = numberPattern.matcher(numberList.get(right));
        String pivot = "";
        if (matcher.matches()) {
            pivot = matcher.group(2);
        }
        int partitionIndex = left;

        for (int i = left; i < right; i++) {
            matcher = numberPattern.matcher(numberList.get(i));

            if (matcher.matches()) {
                if (matcher.group(2).compareTo(pivot) <= 0) {
                    swapQuickSort(numberList, partitionIndex, i);
                    partitionIndex++;
                }
            }
        }

        swapQuickSort(numberList, right, partitionIndex);

        return partitionIndex;

    }

    private static void swapQuickSort(ArrayList<String> numberList, int j, int i) {
        String temp = numberList.get(i);
        numberList.set(i, numberList.get(j));
        numberList.set(j, temp);
    }

}


