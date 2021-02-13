package net.runelite.client.plugins.bhnotifier;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatRank;
import net.runelite.api.Varbits;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.FriendsChatChanged;
import net.runelite.api.events.FriendsChatMemberJoined;
import net.runelite.api.events.FriendsChatMemberLeft;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.Notifier;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "Bounty Hunter Notifier"
)
public class BountyHunterNotifierPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private BountyHunterNotifierOverlay overlayPanel;

	@Inject
	private Notifier notifier;

	@Getter
	private String targetName;

	@Getter
	private FriendsChatRank targetRank;

	@Getter
	private boolean inWilderness = false;

	private HashMap<String, FriendsChatRank> friendsChat;
	private static final Pattern TARGET_ACQUIRED = Pattern.compile("You've been assigned a target: ([a-zA-Z0-9_ ]+)");
	private static final String TARGET_NOT_AVAILABLE = "Your target is no longer available, so you shall be assigned a new target.";
	private static final String TARGET_ABANDONED = "You have abandoned your target.";
	private static final Pattern TARGET_KILLED = Pattern.compile("Target killed: ([a-zA-Z0-9_ ]+)! Kills: (\\d+)\\. Streak: (\\d+)\\.");
	
	@Override
	protected void startUp() throws Exception
	{
		log.info("Bounty Hunter Notifier started!");
		targetName = null;
		targetRank = null;
		overlayManager.add(overlayPanel);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Bounty Hunter Notifier stopped!");
		targetName = null;
		targetRank = null;
		overlayManager.remove(overlayPanel);
	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted)
	{
		String command = commandExecuted.getCommand();
		String[] args = commandExecuted.getArguments();

		if (command.equals("check"))
		{
			log.info("LIST:");
			for (String name : friendsChat.keySet())
			{
				log.info("Current: Rank: " + friendsChat.get(name).toString() + "\tName: " + name);
			}
		}

	}

	@Subscribe
	public void onFriendsChatChanged(FriendsChatChanged event)
	{
		if (event.isJoined())
		{
			friendsChat = new HashMap<>();
		}
	}

	@Subscribe
	public void onFriendsChatMemberJoined(FriendsChatMemberJoined event)
	{
		log.info("Joined: Rank: " + event.getMember().getRank() + "\tName: " + event.getMember().getName());
		friendsChat.put(event.getMember().getName(), event.getMember().getRank());
		if (event.getMember().getName().equals(targetName))
		{
			targetRank = event.getMember().getRank();
		}
	}

	@Subscribe
	public void onFriendsChatMemberLeft(FriendsChatMemberLeft event)
	{
		log.info("Left: Rank: " + event.getMember().getRank() + "\tName: " + event.getMember().getName());
		friendsChat.remove(event.getMember().getName());
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.GAMEMESSAGE && event.getType() != ChatMessageType.SPAM)
		{
			return;
		}

		String chatMsg = Text.removeTags(event.getMessage()); //remove color and linebreaks

		Matcher targetAcquired = TARGET_ACQUIRED.matcher(chatMsg);
		Matcher targetKilled = TARGET_KILLED.matcher(chatMsg);
		if (targetAcquired.matches())
		{
			targetName = targetAcquired.group(0);
			targetRank = friendsChat.get(targetName);
			if (targetRank != null)
			{
				log.info("You have been assigned " + targetName + "(" + targetRank.toString() + ")");
				notifier.notify("You have been assigned " + targetName + "(" + targetRank.toString() + ")");
			}
			else {
				notifier.notify("You have been assigned someone not in your cc!");
			}
		}
		else if (targetKilled.matches() || chatMsg.startsWith(TARGET_NOT_AVAILABLE) || chatMsg.startsWith(TARGET_ABANDONED))
		{
			if (targetRank != null)
			{
				log.info("You no longer have the target " + targetName + "(" + targetRank.toString() + ")");
				notifier.notify("You are no longer assigned " + targetName + "(" + targetRank.toString() + ")");
			}
			else {
				notifier.notify("You are no longer assigned a target!");
			}
			targetName = null;
			targetRank = null;
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged varbitChanged)
	{
		inWilderness = client.getVarbitValue(Varbits.IN_WILDERNESS.getId()) == 1;
	}
}
