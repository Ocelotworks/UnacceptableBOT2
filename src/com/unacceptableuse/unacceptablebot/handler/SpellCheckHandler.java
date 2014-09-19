package com.unacceptableuse.unacceptablebot.handler;

import java.io.File;
import java.io.IOException;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;


public class SpellCheckHandler {
	
    protected static SpellDictionaryHashMap dictionary = null;
    protected static SpellChecker spellChecker = null;

    static {

        try {

            dictionary =
                new SpellDictionaryHashMap(new
                File("english.0"));
        }
        catch (IOException e) {

            e.printStackTrace();
        }
        spellChecker = new SpellChecker(dictionary);
    }

    public static String getSuggestions(String word,
        int threshold) {

        return spellChecker.getSuggestions(word, threshold).get(0).toString();
    }


}
