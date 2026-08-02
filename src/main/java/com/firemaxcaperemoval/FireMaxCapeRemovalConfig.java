package com.firemaxcaperemoval;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("firemaxcaperemoval")
public interface FireMaxCapeRemovalConfig extends Config
{
	@ConfigItem(
			keyName = "enabled",
			name = "Enabled",
			description = "Hide other players if they are wearing a fire max cape."
	)
	default boolean enabled()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hideSelfIfWearing",
			name = "Hide me if wearing fire max cape",
			description = "If enabled, your own player will be hidden when you are wearing a fire max cape. You should burn that cape, too."
	)
	default boolean hideSelfIfWearing()
	{
		return false;
	}

	@ConfigItem(
			keyName = "neverHideFriends",
			name = "Never hide friends",
			description = "If enabled, friends are never hidden even if they do wear the fire max cape. But do you really want to stay friends with them?"
	)
	default boolean neverHideFriends()
	{
		return true;
	}

	@ConfigItem(
			keyName = "neverHideClanMembers",
			name = "Never hide clan members",
			description = "If enabled, clan members are never hidden even if they do wear the fire max cape. Better get out the ban hammer."
	)
	default boolean neverHideClanMembers()
	{
		return true;
	}

	@ConfigItem(
			keyName = "neverHideFriendsChat",
			name = "Never hide friends chat members",
			description = "If enabled, members of your current friends chat are never hidden even if they do wear the fire max cape. Time to restrict your friends chat."
	)
	default boolean neverHideFriendsChat()
	{
		return true;
	}
}