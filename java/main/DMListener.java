package main;
import appeals.Appeal;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DMListener extends ListenerAdapter
{
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		// ^appeal
		if(args[0].equalsIgnoreCase(Main.PREFIX + "appeal"))
		{
			if(args.length == 1)
				return;
			
			//^appeal ign {ign}
			if(args[1].equalsIgnoreCase("ign"))
			{
				if(args.length == 2)
				{
					e.getChannel().sendMessage("Please specify an ign when using this command.").queue();
					return;
				}
				
				for(Appeal app : Main.appeals)
				{
					//if user already started an appeal, update the appeal ign
					if(!app.isSent() && app.getUser().equals(e.getAuthor()))
					{
						app.setIgn(args[2]);
						e.getChannel().sendMessage("Appeal IGN updated. Remember to use \"" + Main.PREFIX + "appeal message\" to"
								+ " add a message to this appeal and \"" + Main.PREFIX + "appeal send\" to send the appeal.").queue();
						return;
					}
				}
				
				//this point is reached if the user does not have an active appeal
				Appeal fresh_app = new Appeal(e.getAuthor()); //(make an appeal for them)
				fresh_app.setIgn(args[2]);
				Main.appeals.add(fresh_app);
				e.getChannel().sendMessage("Appeal created with the specified ign.").queue();
				return;
			}
			
			//^appeal message {message}
			if(args[1].equalsIgnoreCase("message"))
			{
				if(args.length == 2)
				{
					e.getChannel().sendMessage("Please specify a message when using this command.").queue();
					return;
				}
				
				//extract message
				String message = "";
				for(int i = 2; i < args.length; i++)
					message += " " + args[i];
				
				message = message.substring(1);
				
				for(Appeal app : Main.appeals)
				{
					//if user already started an appeal, update the message
					if(!app.isSent() && app.getUser().equals(e.getAuthor()))
					{
						app.setMessage(message);
						e.getChannel().sendMessage("Appeal message updated. Remember to use \"" + Main.PREFIX + "appeal ign\""
								+ " to add an ign to this appeal and \"" + Main.PREFIX + "appeal send\" to send the appeal.").queue();
						return;
					}
				}
				
				//this point is reached if the user does not have an active appeal
				Appeal fresh_app = new Appeal(e.getAuthor()); //(make them an appeal)
				fresh_app.setMessage(message);
				Main.appeals.add(fresh_app);
				e.getChannel().sendMessage("Appeal created with the specified message.").queue();
				
				return;
			}
						
			//^appeal view
			if(args[1].equalsIgnoreCase("view"))
			{
				int count = 0;
				
				for(Appeal app : Main.appeals)
				{
					if(app.getUser().equals(e.getAuthor())) // no return statement on the offchance they submit multiple appeals
					{
						e.getChannel().sendMessage(app.getEmbed()).queue();
						count++;
					}
				}
				
				//if they don't have a started appeal
				if(count == 0)
				{
					e.getChannel().sendMessage("You do not currently have an appeal started. If you wish to begin an appeal, "
							+ "use \"" + Main.PREFIX + "appeal message\", \"" + Main.PREFIX + "appeal ign\" or \""
									+ Main.PREFIX + "appeal open\" to begin writing an appeal.").queue();
				}
				
				return;
			}
			
			//^appeal open
			if(args[1].equalsIgnoreCase("open"))
			{
				for(Appeal app : Main.appeals)
				{
					if(!app.isSent() && app.getUser().equals(e.getAuthor()))
					{
						e.getChannel().sendMessage("You have already opened an appeal, please edit and send this appeal"
								+ " rather than create a new one.").queue();
						return;
					}
				}
				
				Appeal fresh_app = new Appeal(e.getAuthor());
				Main.appeals.add(fresh_app);
				e.getChannel().sendMessage("New appeal opened- edit this appeal with \"" + Main.PREFIX + "\"appeal ign\""
						+ " and \"" + Main.PREFIX + "appeal message\", and then send the appeal with \"" + Main.PREFIX
						+ "appeal send\".").queue();
				
				return;
				
			}
			
			//^appeal close
			if(args[1].equalsIgnoreCase("close"))
			{
				for(Appeal app : Main.appeals)
				{
					if(!app.isSent() && app.getUser().equals(e.getAuthor()))
					{
						Main.appeals.remove(app);
						e.getChannel().sendMessage("Appeal closed.").queue();
						return;
					}
				}
				
				return;
			}
			
			//^appeal send
			if(args[1].equalsIgnoreCase("send"))
			{
				for(Appeal app : Main.appeals)
				{
					if(!app.isSent() && app.getUser().equals(e.getAuthor()))
					{
						if(app.hasIgn() && app.hasMessage())
						{
							app.prime();
							e.getJDA().getTextChannelById(Main.BANLOGS).sendMessage(app.getEmbed()).queue(); //send to logs
							e.getChannel().sendMessage("Your appeal has been submitted! Please be patient, our staff team will"
									+ " review your appeal shortly.").queue();
							e.getChannel().sendMessage(app.getEmbed()).queue();
						}
						else
						{
							e.getChannel().sendMessage("Your appeal is missing some stuff").queue();
						}
						
						return;
					}
				}
				
				e.getChannel().sendMessage("You do not have an appeal created to send to the staff team- please first create an appeal").queue();
				return;
			}
		}
	}
}
