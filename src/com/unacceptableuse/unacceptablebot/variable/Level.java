package com.unacceptableuse.unacceptablebot.variable;

public enum Level
{
	BOT, BANNED, USER, TRUSTED, ADMIN, SUPERADMIN;

	public static Level intToLevel(Integer num)
	{
		return (num < -1 ? BOT : num == -1 ? BANNED : num == 0 ? USER : num == 1 ? TRUSTED : num == 2 ? ADMIN : SUPERADMIN);
	}

	public static int levelToInt(Level lev)
	{
		return (lev == BOT ? -2 : lev == BANNED ? -1 : lev == USER ? 0 : lev == TRUSTED ? 1 : lev == ADMIN ? 2 : Integer.MAX_VALUE);
	}

}
