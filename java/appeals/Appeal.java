package appeals;

import java.time.Instant;

import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Appeal
{
	private User user;
	private String ign;
	private String message;
	private Instant time;
	private long id;
	
	public Appeal(User user)
	{
		this.user = user;
		this.ign = null;
		this.message = null;
		this.time = null;
		this.id = -1; //-1 if not set
	}
	
	public MessageEmbed getEmbed()
	{
		EmbedBuilder eb = new EmbedBuilder();
		eb = eb.setTitle("New Appeal")
				.setAuthor(this.getUser().getAsTag(), null, this.getUser().getAvatarUrl())
				.setColor(65280); //initially set colour green
		
		if(this.getIgn() != null)
			eb = eb.addField("IGN: ", this.getIgn(), true);
		else
			eb = eb.setColor(1); //if there is an ign or message missing, make colour red
		
		if(this.id != -1)
			eb = eb.addField("ID: ", Long.toString(this.getId()), true);
		else
			eb = eb.setColor(16074050); //if there is an ign or message missing, make colour red
		
		if(this.getMessage() != null)
			eb = eb.setDescription(this.getMessage());
		
		if(this.getTime() != null)
			eb = eb.setFooter(this.getTime().toString());
		
		//TODO: if appeal is missing a message or ign, tell the user in the footer in the appeal preview
		
		return eb.build();
	}
	
	//takes care of all the final steps to publish an appeal
	public void prime()
	{
		this.id = Main.ID_NO; //set id
		Main.ID_NO++;
		
		this.setTime(Instant.now()); //set timestamp
	}

	public String getIgn()
	{
		return ign;
	}

	public void setIgn(String ign)
	{
		this.ign = ign;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public Instant getTime()
	{
		return time;
	}

	public void setTime(Instant time)
	{
		this.time = time;
	}

	public User getUser()
	{
		return user;
	}
	
	public long getId()
	{
		return this.id;
	}
	
	public boolean isSent()
	{
		return this.id != -1;
	}
	
	public boolean hasMessage()
	{
		return this.getMessage() != null;
	}
	
	public boolean hasIgn()
	{
		return this.getIgn() != null;
	}
}
