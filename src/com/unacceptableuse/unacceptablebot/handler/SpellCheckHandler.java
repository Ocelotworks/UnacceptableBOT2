package com.unacceptableuse.unacceptablebot.handler;

import org.xeustechnologies.googleapi.spelling.Language;
import org.xeustechnologies.googleapi.spelling.SpellChecker;
import org.xeustechnologies.googleapi.spelling.SpellCorrection;
import org.xeustechnologies.googleapi.spelling.SpellResponse;

public class SpellCheckHandler {

	Language lang = null;

	public SpellCheckHandler(Language locale) {
		lang = locale;
	}

	public String doCheck(String sent) {
		SpellChecker checker = new SpellChecker();
		checker.setLanguage(lang);

		SpellResponse spellResponse = checker.check(sent);

		for (SpellCorrection sc : spellResponse.getCorrections())
			return sc.getValue();

		return "";

	}

}
