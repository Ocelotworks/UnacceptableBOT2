package com.unacceptableuse.unacceptablebot.handler;

import java.util.ArrayList;

import com.unacceptableuse.unacceptablebot.variable.Job;

public class JobHandler
{

	private ArrayList<Job> queue = new ArrayList<Job>();
	private ArrayList<String> args = new ArrayList<String>();
	
	
	public void init()
	{
		
	}
	
	
	public void queueJob(Job job, String args)
	{
		queue.add(job);
		this.args.add(args);
	}
	
	
}
