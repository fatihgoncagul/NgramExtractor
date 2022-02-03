import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * @author fatih goncagul 20170808061
 * @since 01.03.2021
 */

public class NgramExtractor {
          static int tokenNumber;
        static String inputFile;
        static String outputFile;
        static String n;

    public static void main(String[] args) {
     inputFile = args[0];
     outputFile = args[1];
     n = args[2];
     int given = Integer.parseInt(n);

        String[] wordArray;
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))){

             line = reader.readLine();

            while (line != null) {
                sb.append(line+" ");

                line = reader.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String everything = sb.toString();
        everything = everything.replaceAll("I","i");
        wordArray =everything.replaceAll("\\p{Punct}", "").toLowerCase().split("\\s+");

        HashMap<String, Integer> hashMap;

        List<String> list;

        list = makeNgram(wordArray,given);
        hashMap = createMap( list, list.size());
        hashMap = sortIt(hashMap);
        tokenNumber= wordArray.length;//this goes to getFrequency
        writeToFile(hashMap,outputFile);

    }

    /**
     * this method writes the data to the file
     * @param hashMap it has the data that required
     * @param file    filename/path
     */
    static void writeToFile(HashMap<String, Integer> hashMap, String file){


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            System.out.println("Total number of tokens: "+tokenNumber);
            writer.write("Total number of tokens:"+tokenNumber);
            System.out.println("\nngram, count, frequency");
            writer.newLine();
            writer.newLine();
            writer.write("ngram,count,frequency");
            writer.newLine();
            for (String name: hashMap.keySet()){
                int value = hashMap.get(name);
                System.out.println(name + "," + value +"," + getFrequency(value,tokenNumber));
                writer.write(name + "," + value +"," + getFrequency(value,tokenNumber));
                writer.newLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * this method sorts the hashmap by its values in descending order
     * @param hashMap it has the required data
     * @return  returns the sorted hashmap
     */
    static HashMap<String, Integer> sortIt(HashMap<String, Integer> hashMap){
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(hashMap.entrySet());

        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if (o2.getValue()> o1.getValue()){
                    return 1;
                }else if (o1.getValue()> o2.getValue()){
                    return -1;
                }else{ return 0;
                }

            }
        });
        HashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entries : entryList) {
            sortedMap.put(entries.getKey(), entries.getValue());
        }
        return sortedMap;
    }

    /**
     *simply creates the required hashmap by detecting repetitions of ngramed value
     * @param list takes a Ngramed list involving words
     * @param size the size of hashmap
     * @return returns the hashmap that has required data
     */
    static HashMap<String, Integer> createMap (List<String> list,int size){
        HashMap<String, Integer> hashMap = new HashMap<>(size);
        for (int i= 0;i< list.size();i++){
            int count =0;
            for (int j=0;j< list.size();j++) {
                //hashMap.put(wordArray[j],count);
                if (list.get(i).equals(list.get(j))) {
                    count++;
                    hashMap.put(list.get(j), count);//put(arr[j]+arr[j+1],count);
                }

            }
        }
        return hashMap;
    }

    /**
     *this methods turns all individual words into ngramed words
     * @param arr this array has all the words from the text
     * @param n this parameter specifies the ngram
     * @return returns a Ngramed list involving words
     */
    static List<String> makeNgram(String[] arr, int n) {
        List<String> ngramList = new ArrayList<>();
        for(int i=0; i< arr.length-n; i++) {
            String ngramWord = "";
            for (int j = i; j < i + n; j++){
                ngramWord += arr[j] + " ";
             }
            ngramWord = ngramWord.trim();//for taking the extra space
            ngramList.add(ngramWord);
        }
        return ngramList;
    }

    /**
     * this method calculates frequency
     * @param count the number of repetitions of the ngramed word
     * @param total the number of words from the given text
     * @return returns the frequency of the ngramed word
     */
    public static String getFrequency(int count, int total) {
        Float result = (float)100*count/total;
        return String.valueOf(result);
    }


}

