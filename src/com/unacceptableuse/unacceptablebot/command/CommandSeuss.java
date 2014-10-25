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

	public static int countLines(final String filename) throws IOException
	{
		final InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try
		{
			final byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1)
			{
				empty = false;
				for (int i = 0; i < readChars; ++i)
					if (c[i] == '\n')
						++count;
			}
			return ((count == 0) && !empty) ? 1 : count;
		} finally
		{
			is.close();
		}
	}

	public static String getLine(final String args) throws IOException
	{
		final File file = new File("books/", args);
		final int maxLines = countLines("books/" + args);
		int rand = new Random().nextInt(maxLines);
		if (rand == maxLines)
			rand = rand + 1;

		BufferedReader in = null;
		final List<String> fileList = new ArrayList<String>();
		try
		{
			in = new BufferedReader(new FileReader(file));
			String str;
			while ((str = in.readLine()) != null)
				fileList.add(str);
		} catch (final FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (final IOException e)
		{
			e.printStackTrace();
		} finally
		{
			if (in != null)
				in.close();
		}

		return fileList.get(rand);
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "seuss" };
	}

	@Override
	public String getHelp()
	{
		return "Usage: seuss <bookName|list> | Result: Either pick a line of a Dr. Seuss book at random, or list all availble Dr. Seuss books ";
	}

	@Override
	public void performCommand(final User sender, final Channel channel, final String message, final String[] args)
	{
		switch (args[1])
		{
		default:
		{
			try
			{
				sendMessage(getLine(args[1]), channel);
			} catch (final IOException e)
			{
				e.printStackTrace();
			}
			break;
		}
		case ("list"):
		{
			final File folder = new File("books/");
			final File[] listOfFiles = folder.listFiles();
			final String books = "";
			for (final File listOfFile : listOfFiles)
				books.concat(listOfFile.getName());
			sendMessage("Books: ".concat(books), channel);
			break;
		}
		}

	}

	@Override
	public int requiredArguments()
	{
		return 1;
	}
}
