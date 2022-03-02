import java.util.*;
import java.io.*;

// This is probably one of the least readable pieces of code i have ever written,
// I apologize to whoever decides to read it.
public class Ptwo {
    public static Map<String, List<String>> dict;
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("Eldrow Wordlist.csv"));
        Set<String> words = new HashSet<>();
        dict = new HashMap<>();
        while (sc.hasNextLine()) {
            String word = sc.nextLine();
            words.add(word);
            dict.put(word, new ArrayList<>());
        }

        List<String> longest = null;
        int maxSize = 0;
        for (String target : words){
            System.out.println(target);
            for (String s : dict.keySet()){  //reset dict
                dict.put(s, new ArrayList<>());
            }

            List<String> run = exhaust2(words, "", target, 0);
            if (maxSize < run.size()){
                maxSize = run.size();
                longest = run;
            }
            System.out.println(run);
        }
        System.out.println(longest);
    }

    public static List<String> exhaust2(Set<String> consider, String currentGuess, String target, int currentLongest){
        if (consider.isEmpty()){
            return new ArrayList<String>();
        }
        if (!currentGuess.equals("") && dict.get(currentGuess).size() != 0 && dict.get(currentGuess).size() < currentLongest) {
            return dict.get(currentGuess);
        }
        

        Set<String> copy = new HashSet<>(consider);
        int maxSize = 0;
        List<String> ret = new ArrayList<>();

        for (String word : consider){
            copy.remove(word);

            Set<String> reduction = reduce(copy, word, target);
            List<String> guessPath = exhaust2(reduction, word, target, maxSize);
            guessPath.add(0, word);

            copy.add(word);

            if (guessPath.size() > maxSize){
                maxSize = guessPath.size();
                ret = guessPath;
            }
        }
        dict.put(currentGuess, ret);
        return ret;
    }

    // public static List<String> exhaust(Set<String> consider, List<String> currentGuesses, String target, int currentLongest){
    //     if (consider.isEmpty()) {
    //         dict.put(currentGuesses.get(currentGuesses.size() -1), 0);
    //         return currentGuesses;
    //     }
    //     if (!currentGuesses.isEmpty() && currentLongest > dict.get(currentGuesses.get(currentGuesses.size() - 1))) return currentGuesses;

    //     System.out.println(currentGuesses);

    //     List<String> ret = new ArrayList<>();
    //     int maxSize = 0;
    //     Set<String> copy = new HashSet<>(consider);

    //     for (String word : consider){
    //         copy.remove(word);
    //         currentGuesses.add(word);

    //         Set<String> reduction = reduce(copy, word, target);
    //         List<String> guessList = exhaust(reduction, currentGuesses, target, maxSize);
    //         System.out.println(guessList);
    //         dict.put(word, guessList.size());
    //         copy.add(word);
    //         currentGuesses.remove(currentGuesses.size() - 1);    
            
    //         if (maxSize < guessList.size()){
    //             maxSize = guessList.size();
    //             ret = new ArrayList<>(guessList);
    //         }
    //     }
        
    //     return ret;
    // }
    
    public static Set<String> reduce(Set<String> consider, String guess, String target){
        String pattern = "-----";
        for (int i = 0; i < guess.length(); i++){
            if (guess.charAt(i) == target.charAt(i)){
                pattern = pattern.substring(0, i) + guess.charAt(i) + pattern.substring(i + 1, pattern.length());
            }
            for (int j = guess.indexOf(target.charAt(i)); j >= 0; j = guess.indexOf(target.charAt(i), j + 1)){
                if (pattern.charAt(j) == '-'){
                    pattern = pattern.substring(0, j) + '*' + pattern.substring(j + 1, pattern.length());
                }
            }
        }
        
        Set<String> ret = new HashSet<>();
        for (String s : consider){ 
            boolean contained = true;
            for (int i = 0; i < pattern.length(); i++){
                if (pattern.charAt(i) == '-' && (s.indexOf(guess.charAt(i)) != -1)){
                    contained = false;
                    break;
                } else if (pattern.charAt(i) == '*' && (s.indexOf(guess.charAt(i)) == -1 || s.charAt(i) == guess.charAt(i))){
                    contained = false;
                    break;
                } else if ((pattern.charAt(i) != '-' && pattern.charAt(i) != '*') && pattern.charAt(i) != s.charAt(i)) {
                    contained = false;
                    break;
                }
            }
            if (contained) ret.add(s);
        }

        return ret;
    }
}
    