package com.firemaxcaperemoval;

import com.google.inject.Provides;
import javax.inject.Inject;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Player;
import net.runelite.api.PlayerComposition;
import net.runelite.api.Renderable;
import net.runelite.api.events.PlayerChanged;
import net.runelite.api.kit.KitType;

import net.runelite.client.callback.RenderCallback;
import net.runelite.client.callback.RenderCallbackManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
		name = "Fire Max Cape Removal",
		description = "Replaces fire max capes with regular fire capes.",
		tags = {"replace", "players", "fire max cape"}
)
public class FireMaxCapeRemovalPlugin extends Plugin
{
	private static final int FIRE_MAX_CAPE_ITEM_ID = ItemID.FIRE_MAX_CAPE;
	private static final int FIRE_MAX_CAPE_L_ITEM_ID = ItemID.FIRE_MAX_CAPE_L;
	private static final int FIRE_CAPE_ITEM_ID = ItemID.FIRE_CAPE;
	private static final int ITEM_OFFSET = ItemID.FIRE_CAPE - ItemID.OIL_LAMP;

	@Inject
	private Client client;

	@Inject
	private FireMaxCapeRemovalConfig config;

	@Inject
	private RenderCallbackManager renderCallbackManager;

	private final RenderCallback renderCallback = new RenderCallback()
	{

	};

	@Provides
	FireMaxCapeRemovalConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(FireMaxCapeRemovalConfig.class);
	}

	@Subscribe
	public void onPlayerChanged(PlayerChanged event)
	{
		if (!config.enabled())
		{
			return;
		}

		Player player = event.getPlayer();

		// Ignore yourself if replaceSelf is false
		if (player == client.getLocalPlayer() && !config.replaceSelf())
		{
			return;
		}

		// Ignore friends if neverReplaceFriends is true
		if (config.neverReplaceFriends() && player.isFriend())
		{
			return;
		}

		// Ignore clan members if neverReplaceClanMembers is true
		if (config.neverReplaceClanMembers() && player.isClanMember())
		{
			return;
		}

		// Ignore friends chat members if neverReplaceFriendsChat is true
		if (config.neverReplaceFriendsChat() && player.isFriendsChatMember())
		{
			return;
		}

		swapEquipment(player);
	}

	private void swapEquipment(Player player) {
		PlayerComposition composition = player.getPlayerComposition();
		if (composition == null) {
			return;
		}

		if (isWearingFireMaxCape(composition))
		{

			int[] equipmentIds = composition.getEquipmentIds();

			// Replace cape slot with a fire cape if player is wearing a fire max cape.
			equipmentIds[KitType.CAPE.getIndex()] = (FIRE_CAPE_ITEM_ID) + ITEM_OFFSET;
		}

		// Force RuneLite/the client to recalculate and redraw the modified player model
		composition.setHash();
	}


	@Override
	protected void startUp()
	{
		renderCallbackManager.register(renderCallback);
	}

	@Override
	protected void shutDown()
	{
		renderCallbackManager.unregister(renderCallback);
	}

	private static boolean isWearingFireMaxCape(final PlayerComposition composition)
	{
		final int capeValue = getCapeEquipmentValue(composition);
		if (capeValue < 0)
		{
			return false;
		}

		// Accept raw item ID or ITEM_OFFSET plus item ID
		if (capeValue == FIRE_MAX_CAPE_ITEM_ID || capeValue == FIRE_MAX_CAPE_L_ITEM_ID)
		{
			return true;
		}

		if (capeValue >= PlayerComposition.ITEM_OFFSET
				&& ((capeValue - PlayerComposition.ITEM_OFFSET) == FIRE_MAX_CAPE_ITEM_ID
					|| (capeValue - PlayerComposition.ITEM_OFFSET) == FIRE_MAX_CAPE_ITEM_ID))
		{
			return true;
		}

		return false;
	}

	private static int getCapeEquipmentValue(final PlayerComposition composition)
	{
		final int[] equipmentIds = composition.getEquipmentIds();
		if (equipmentIds != null)
		{
			final int idx = KitType.CAPE.getIndex();
			if (idx >= 0 && idx < equipmentIds.length)
			{
				return equipmentIds[idx];
			}
		}

		return composition.getEquipmentId(KitType.CAPE);
	}
}