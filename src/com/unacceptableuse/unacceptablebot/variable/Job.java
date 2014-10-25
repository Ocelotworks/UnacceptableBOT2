package com.unacceptableuse.unacceptablebot.variable;

public enum Job
{
	UPDATE_CHANS, RESTART, SHUTDOWN, SEND_MESSAGE;

	public static Job getJob(String j)
	{
		return Job.valueOf(j);
	}

}
