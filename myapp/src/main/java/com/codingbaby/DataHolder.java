package com.codingbaby;

import android.content.Context;
import android.content.res.AssetManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class DataHolder {


    private static Map<Character, List<String>> english = new LinkedHashMap<>();
    private static List<String> english_primary1 = new ArrayList<>();
    private static List<String> english_primary2 = new ArrayList<>();
    private static List<String> english_primary3 = new ArrayList<>();
    private static List<String> english_primary4 = new ArrayList<>();
    private static List<String> english_primary5 = new ArrayList<>();
    private static List<String> english_primary6 = new ArrayList<>();


    private List<String> shortEnglish = new ArrayList<>();


    private Stack<String> englishWordHistory = new Stack<>();


    public List<String> getByChar(Character c) {
        return english.get(c);
    }


    public String popEnglish() {
        return englishWordHistory.pop();
    }


    public List<String> randShortEnglish() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Random rand = new Random();
            int index = rand.nextInt(shortEnglish.size());
            list.add(shortEnglish.get(index));
        }
        return list;
    }

    public String randEnglish(ButtonStatus buttonStatus) {
        String englishWord;
        Random rand = new Random();
        Set<Character> characters = english.keySet();
        List<Character> keyList = new ArrayList<>();
        for (Character character : characters) {
            keyList.add(character);
        }
        int index = rand.nextInt(keyList.size());
        Character character = keyList.get(index);
        List<String> wordList = english.get(character);
        rand = new Random();
        englishWord = wordList.get(rand.nextInt(wordList.size()));

        if (buttonStatus.english1) {
            index = rand.nextInt(english_primary1.size());
            englishWord = english_primary1.get(index);
        }

        if (buttonStatus.english2) {
            index = rand.nextInt(english_primary2.size());
            englishWord = english_primary2.get(index);
        }


        if (buttonStatus.english3) {
            index = rand.nextInt(english_primary3.size());
            englishWord = english_primary3.get(index);
        }

        if (buttonStatus.english4) {
            index = rand.nextInt(english_primary4.size());
            englishWord = english_primary4.get(index);
        }

        if (buttonStatus.english5) {
            index = rand.nextInt(english_primary5.size());
            englishWord = english_primary5.get(index);
        }


        if (buttonStatus.english6) {
            index = rand.nextInt(english_primary6.size());
            englishWord = english_primary6.get(index);
        }

        englishWordHistory.push(englishWord);

        return englishWord;

    }


    public DataHolder(Context context) {

        final AssetManager assets = context.getAssets();


        new Thread(new Runnable() {
            @Override
            public void run() {

                shortEnglish.addAll(FileReader.loadCet4Short(assets));

                english.putAll(FileReader.loadEnglishWord(assets));


                english_primary1.addAll(FileReader.freqEnglish(assets, "cet4/freq1.txt"));
                english_primary2.addAll(FileReader.freqEnglish(assets, "cet4/freq2.txt"));
                english_primary3.addAll(FileReader.freqEnglish(assets, "cet4/freq3.txt"));
                english_primary4.addAll(FileReader.freqEnglish(assets, "cet4/freq4.txt"));
                english_primary5.addAll(FileReader.freqEnglish(assets, "cet4/freq5.txt"));
                english_primary6.addAll(FileReader.freqEnglish(assets, "cet4/freq.txt"));

            }
        }).start();


    }
}
