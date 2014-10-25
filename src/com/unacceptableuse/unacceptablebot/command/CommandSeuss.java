/**
 * 
 */
package com.unacceptableuse.unacceptablebot.command;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.pircbotx.Channel;
import org.pircbotx.User;

/**
 * @author Neil
 * 
 */
public class CommandSeuss extends Command
{

	@Override
	public void performCommand(User sender, Channel channel, String message, String[] args)
	{
		switch (args[1])
		{
		default:
		{
			try
			{
				sendMessage(getLine(args[1]), channel);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			break;
		}
		case ("list"):
		{
			File folder = new File("books/");
			File[] listOfFiles = folder.listFiles();
			String books = "";
			for (int i = 0; i < listOfFiles.length; i++)
			{
				books.concat(listOfFiles[i].getName());
			}
			sendMessage("Books: ".concat(books), channel);
			break;
		}
		}

	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "seuss" };
	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}

	public static String getLine(String args) throws IOException
	{
		File file = new File("books/", args);
		int maxLines = countLines("books/" + args);
		int rand = new Random().nextInt(maxLines);
		if (rand == maxLines)
		{
			rand = rand + 1;
		}

		BufferedReader in = null;
		List<String> fileList = new ArrayList<String>();
		try
		{
			in = new BufferedReader(new FileReader(file));
			String str;
			while ((str = in.readLine()) != null)
			{
				fileList.add(str);
			}
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			if (in != null)
			{
				in.close();
			}
		}

		return fileList.get(rand);
	}

	public static int countLines(String filename) throws IOException
	{
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try
		{
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1)
			{
				empty = false;
				for (int i = 0; i < readChars; ++i)
				{
					if (c[i] == '\n')
					{
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally
		{
			is.close();
		}
	}

	@Override
	public String getHelp()
	{
		return "Usage: seuss <bookName|list> | Result: Either pick a line of a Dr. Seuss book at random, or list all availble Dr. Seuss books ";
	}
}
