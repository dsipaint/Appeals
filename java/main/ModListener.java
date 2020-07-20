package main;

import appeals.Appeal;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ModListener extends ListenerAdapter
{
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		//staff commands for ban appeals
		if(StopListener.isStaff(e.getMember()))
		{
			String msg = e.getMessage().getContentRaw();
			String[] args = msg.split(" ");
			
			if(args[0].equalsIgnoreCase(Main.PREFIX + "appeal"))
			{
				if(args.length == 1)
					return;
				
				//^appeal close
				if(args[1].equalsIgnoreCase("close"))
				{
					if(args.length == 2)
						return;
					
					if(!args[2].matches("\\d+"))
					{
						e.getChannel().sendMessage("Please enter a valid id.").queue();
						return;
					}
					
					for(Appeal app : Main.appeals)
					{
						//if this is the specified appeal
						if(app.isSent() && app.getId() == Long.parseLong(args[2]))
						{
							app.getUser().openPrivateChannel().queue(channel ->
							{
								channel.sendMessage("Your appeal has now been closed.").queue();
								channel.sendMessage(app.getEmbed()).queue();
								
								//extract the message
								String message = "";
								for(int i = 3; i < args.length; i++)
									message += " " + args[i];
								
								if(!message.isEmpty())
								{
									message = message.substring(1);
									channel.sendMessage(new EmbedBuilder()
											.setAuthor(e.getAuthor().getAsTag(), null, e.getAuthor().getAvatarUrl())
											.setDescription(message)
											.setColor(65280)
											.build()).queue();
								}
							});
							
							e.getChannel().sendMessage("Informed user " + app.getUser().getAsTag() + " that their"
									+ " appeal was closed.").queue();
							
							Main.appeals.remove(app);
							return;
						}
					}
					
					e.getChannel().sendMessage("Could not find an active appeal with this id.").queue();
					
					return;
				}
				
				if(args[1].equalsIgnoreCase("list"))
				{
					int count = 0;
					
					for(Appeal app : Main.appeals)
					{
						if(app.isSent())
						{
							e.getChannel().sendMessage(app.getEmbed()).queue();
							count++;
						}
					}
					
					if(count == 0)
						e.getChannel().sendMessage("There are currently no appeals to display.").queue();
					
					return;
				}
			}
		}
	}
}
