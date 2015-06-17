package edu.radamus.samples.text2text;

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

	public String createRandomizedText() {

		return randomize(words[0]);
	}

	private String createRandomizedTextRecurrently() {
		return randomizeRecurrent(words[0], new StringBuilder(), new Random());
	}
	
	/**
	 * @param text
	 * @return
	 */
	private String[] extractWords(String text) {
		return text.split("\\s+");
	}

	/**
	 * @param words
	 * @return
	 */
	private Map<String, List<String>> buildWordMap(String[] words) {
		Map<String, List<String>> word2Conseq = new HashMap<String, List<String>>();
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			List<String> conseq = word2Conseq.get(word);
			if (conseq == null) {
				conseq = new ArrayList<String>();
				word2Conseq.put(word, conseq);
			}
			if (i < words.length - 1) {
				conseq.add(words[i + 1]);
			}
		}
		return word2Conseq;
	}

	/**
	 * @param startWord
	 *            - the word to start from
	 * @return string build from random pick of one of the directly subsequent
	 *         words of a given word
	 */
	private String randomize(String startWord) {
		StringBuilder builder = new StringBuilder();
		String randomWord = startWord;
		List<String> nextVector = word2Conseq.get(startWord);
		
		while (nextVector.size() != 0) {
			Random rand = new Random();
			int index = rand.nextInt(nextVector.size());
			randomWord = nextVector.get(index);
			builder.append(randomWord);
			builder.append(" ");
			nextVector = word2Conseq.get(randomWord);
		}

		return builder.toString().trim();
	}

	/**
	 * Recurrent version of the randomizer
	 * @param word
	 *            - the word to start from
	 * @return string build from random pick of one of the directly subsequent
	 *         words of a given word
	 */
	private String randomizeRecurrent(String word, StringBuilder builder, Random rand) {
		List<String> nextVector = word2Conseq.get(word);
		if (nextVector.size() == 0) {
			return builder.toString().trim();
		} else {			
			int index = rand.nextInt(nextVector.size());
			String randomWord = nextVector.get(index);
			builder.append(randomWord);
			builder.append(" ");
			return randomizeRecurrent(randomWord, builder, rand);

		}

	}

	private String explainWordMap() {
		StringBuilder builder = new StringBuilder();
		for(Entry<String, List<String>> entry : this.word2Conseq.entrySet()){
			builder.append(entry.getKey());
			builder.append(" -> ");
			for(String nextWord : entry.getValue()){
				builder.append(nextWord);
				builder.append(", ");
			}
			builder.append("\n");
		}
		return builder.toString().trim();
	}

	private static String loadText() {
		// tylko symulowanie ³adowania
		return "Ala ma Asa As to pies Ali to Ala i Ola Ala stoi i Ola stoi i lala Oli stoi.";
	}
	
	public static void main(String[] args) {		
		String text = loadText();
		System.out.println(">>Text:<<");
		System.out.println(text);
		TextRandomizer textRandomizer = new TextRandomizer(text);
		System.out.println("\n>>Mapa:<<");
		System.out.println(textRandomizer.explainWordMap());
		System.out.println("\n>>Losowe teksty iteracyjnie:<<");
		for(int i = 0; i < 5; i++){
			String newText = textRandomizer.createRandomizedText();
			System.out.println(newText);	
		}
		
		System.out.println("\n>>Losowe teksty rekurencyjnie:<<");
		for(int i = 0; i < 5; i++){
			String newText = textRandomizer.createRandomizedTextRecurrently();
			System.out.println(newText);	
		}
		
	}

	

	
}
