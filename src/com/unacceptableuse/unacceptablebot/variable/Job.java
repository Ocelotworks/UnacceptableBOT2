package com.unacceptableuse.unacceptablebot.variable;

public enum Job
{
	RESTART, SEND_MESSAGE, SHUTDOWN, UPDATE_CHANS;

	public static Job getJob(final String j)
	{
		return Job.valueOf(j);
	}

}
