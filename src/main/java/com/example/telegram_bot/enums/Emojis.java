package com.example.telegram_bot.enums;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Emojis {
	SPARKLES(EmojiParser.parseToUnicode(":sparkles:")),
	SCROLL(EmojiParser.parseToUnicode(":scroll:")),
	MAGE(EmojiParser.parseToUnicode(":mage:")),
	SCREAM(EmojiParser.parseToUnicode(":scream:"));

	private final String emojiName;

	@Override
	public String toString() {
		return emojiName;
	}
}