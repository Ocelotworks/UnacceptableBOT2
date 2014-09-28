package com.unacceptableuse.unacceptablebot.threading;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimerTask;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;
import com.unacceptableuse.unacceptablebot.variable.Job;

public class JobThread extends TimerTask
{

	@Override
	public void run()
	{
		try
		{
			ResultSet rs = UnacceptableBot.getConfigHandler().sql.query("SELECT * FROM `teknogeek_settings`.`Jobs`");
			while(rs.next())
			{
				UnacceptableBot.getJobHandler().queueJob(Job.getJob(rs.getString(2)), rs.getString(3));
			}
			
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
