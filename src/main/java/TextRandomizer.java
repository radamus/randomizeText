

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class TextRandomizer {

	private String[] words;
	private Map<String, List<String>> word2Conseq;

	public TextRandomizer(String text) {
		this.words = extractWords(text);
		word2Conseq = buildWordMap(words);
	}

	public String createRandomizedTextIteratively() {

		return randomizeIterative(words[0], new StringBuilder(), new Random());
	}

	private String createRandomizedTextRecurrently() {
		return randomizeRecurrent(words[0], new StringBuilder(), new Random(), 0);
	}

	/**
	 * Split text into words
	 * 
	 * @param text
	 * @return
	 */
	private String[] extractWords(String text) {
		return text.split("\\s+");
	}

	/**
	 * Build a map of unique words where values are lists of subsequent words
	 * 
	 * @param words
	 * @return
	 */
	private Map<String, List<String>> buildWordMap(String[] words) {
		Map<String, List<String>> uniqueWords2Followers = new HashMap<String, List<String>>();
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			List<String> conseq = uniqueWords2Followers.get(word);
			if (conseq == null) {
				conseq = new ArrayList<String>();
				uniqueWords2Followers.put(word, conseq);
			}
			if (i < words.length - 1) {
				conseq.add(words[i + 1]);
			}
		}
		return uniqueWords2Followers;
	}

	/**
	 * @param startWord
	 *            - the word to start from
	 * @return string build from random pick of one of the directly subsequent
	 *         words of a given word
	 */
	private String randomizeIterative(String startWord, StringBuilder builder,
			Random rand) {
		String randomWord = startWord;
		List<String> nextVector;

		for(int i = 0; i < words.length; i++){
			nextVector = word2Conseq.get(randomWord);
			if(nextVector.size() == 0){
				continue;
			}
			randomWord = pickNextWord(nextVector, rand);
			builder.append(randomWord).append(" ");
			
		}

		return builder.toString().trim();
	}

	/**
	 * Recurrent version of the randomizer
	 * 
	 * @param word
	 *            - the word to start from
	 * @return string build from random pick of one of the directly subsequent
	 *         words of a given word
	 */
	private String randomizeRecurrent(String word, StringBuilder builder,
			Random rand, int counter) {
		String randomWord = word;
		List<String> nextVector = word2Conseq.get(randomWord);
		if (counter < words.length && nextVector.size() > 0) {
			randomWord = pickNextWord(nextVector, rand);
			builder.append(randomWord).append(" ");
			return randomizeRecurrent(randomWord, builder, rand, ++counter);

		} else {
			return builder.toString().trim();
		}

	}

	private String pickNextWord(List<String> nextWordsVector, 
			Random rand) {
		String randomWord;
		int index = rand.nextInt(nextWordsVector.size());
		randomWord = nextWordsVector.get(index);		
		return randomWord;
	}

	private String explainWordMap() {
		StringBuilder builder = new StringBuilder();
		for (Entry<String, List<String>> entry : this.word2Conseq.entrySet()) {
			builder.append(entry.getKey());
			builder.append(" -> ");
			for (String nextWord : entry.getValue()) {
				builder.append(nextWord);
				builder.append(", ");
			}
			builder.append("\n");
		}
		return builder.toString().trim();
	}

	private static String loadDefaultText() {
		return "Ala ma Asa As to pies Ali to Ala i Ola Ala stoi i Ola stoi i lala Oli stoi.";
	}

	public static void main(String[] args) {
		String text;
		if (args.length > 0) {
			text = args[0];
		} else {
			text = loadDefaultText();
		}
		System.out.println(">>Text:<<");
		System.out.println(text);
		TextRandomizer textRandomizer = new TextRandomizer(text);
		System.out.println("\n>>Mapa:<<");
		System.out.println(textRandomizer.explainWordMap());
		System.out.println("\n>>Losowe teksty iteracyjnie:<<");
		for (int i = 0; i < 5; i++) {
			String newText = textRandomizer.createRandomizedTextIteratively();
			System.out.println(newText);
		}

		System.out.println("\n>>Losowe teksty rekurencyjnie:<<");
		for (int i = 0; i < 5; i++) {
			String newText = textRandomizer.createRandomizedTextRecurrently();
			System.out.println(newText);
		}

	}

}
