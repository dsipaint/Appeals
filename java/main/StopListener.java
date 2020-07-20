package main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StopListener extends ListenerAdapter
{
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		if(isStaff(e.getMember()))
		{
			String msg = e.getMessage().getContentRaw();
			String[] args = msg.split(" ");
			
			// ^shutdown
			if(args[0].equalsIgnoreCase(Main.PREFIX + "shutdown"))
			{
				e.getJDA().shutdownNow();
				System.exit(0);
			}
			
			if(args.length == 1)
				return;
			
			// ^shutdown
			if(args[0].equalsIgnoreCase(Main.PREFIX + "disable") && args[1].equalsIgnoreCase("appeals"))
			{
				e.getChannel().sendMessage("*disabling al's appeal code*...").queue();
				e.getJDA().shutdown();
				System.exit(0);
			}
		}
	}
	
	//return true if a member has discord mod, admin or is owner
	public static boolean isStaff(Member m)
	{
		//if owner
		if(m.isOwner())
			return true;
		
		//if admin
		if(m.hasPermission(Permission.ADMINISTRATOR))
			return true;
		
		//if discord mod TODO: Make discord mod module for all servers
		switch(m.getGuild().getId())
		{
			case "565623426501443584" : //wilbur's discord
				for(Role r : m.getRoles())
				{
					if(r.getId().equals("565626094917648386")) //wilbur discord mod
						return true;
				}
				break;
				
			case "640254333807755304" : //charlie's server
				for(Role r : m.getRoles())
				{
					if(r.getId().equals("640255355401535499")) //charlie discord mod
						return true;
				}
				break;
		}
		
		return false;
	}
}
