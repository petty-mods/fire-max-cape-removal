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
			description = "Replaces fire max capes with regular fire capes."
	)
	default boolean enabled()
	{
		return true;
	}

	@ConfigItem(
			keyName = "replaceSelf",
			name = "Replace my own fire max cape",
			description = "If enabled, your own player will have their fire max cape replaced. You should burn that cape, too."
	)
	default boolean replaceSelf()
	{
		return true;
	}

	@ConfigItem(
			keyName = "neverReplaceFriends",
			name = "Never replace friends",
			description = "If enabled, friends never have their fire max capes replaced. But do you really want to stay friends with them?"
	)
	default boolean neverReplaceFriends()
	{
		return false;
	}

	@ConfigItem(
			keyName = "neverReplaceClanMembers",
			name = "Never replace clan members",
			description = "If enabled, clan members never have their fire max capes replaced. Better get out the ban hammer."
	)
	default boolean neverReplaceClanMembers()
	{
		return false;
	}

	@ConfigItem(
			keyName = "neverReplaceFriendsChat",
			name = "Never replace friends chat members",
			description = "If enabled, members of your current friends chat never have their fire max capes replaced. Time to restrict your friends chat."
	)
	default boolean neverReplaceFriendsChat()
	{
		return false;
	}
}