package main;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import appeals.Appeal;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main
{
	static JDA jda;
	static final String PREFIX = "^", BANLOGS = "";
	static ArrayList<Appeal> appeals;
	public static long ID_NO;
	
	/*
	 * TODO: help messages, better isStaff method, modularise, better modlogs generalisation,
	 * error-catch
	 */
	
	public static void main(String[] args)
	{
		try
		{
			jda = new JDABuilder(AccountType.BOT).setToken("").build();
		}
		catch (LoginException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			jda.awaitReady();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		appeals = new ArrayList<Appeal>();
		ID_NO = 0;
		
		jda.addEventListener(new DMListener());
		jda.addEventListener(new ModListener());
		jda.addEventListener(new StopListener());
	}
}
