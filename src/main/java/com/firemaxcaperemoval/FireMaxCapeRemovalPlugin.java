package com.firemaxcaperemoval;

import com.google.inject.Provides;
import javax.inject.Inject;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Player;
import net.runelite.api.PlayerComposition;
import net.runelite.api.Renderable;
import net.runelite.api.kit.KitType;

import net.runelite.client.callback.RenderCallback;
import net.runelite.client.callback.RenderCallbackManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
		name = "Fire Max Cape Removal",
		description = "Hides players if they are wearing a fire max cape.",
		tags = {"hide", "players", "fire max cape"}
)
public class FireMaxCapeRemovalPlugin extends Plugin
{
	private static final int FIRE_MAX_CAPE_ITEM_ID = ItemID.FIRE_MAX_CAPE;
	private static final int FIRE_MAX_CAPE_L_ITEM_ID = ItemID.FIRE_MAX_CAPE_L;

	@Inject
	private Client client;

	@Inject
	private FireMaxCapeRemovalConfig config;

	@Inject
	private RenderCallbackManager renderCallbackManager;

	private final RenderCallback renderCallback = new RenderCallback()
	{
		@Override
		public boolean addEntity(final Renderable renderable, final boolean ui)
		{
			if (ui)
			{
				return true;
			}

			if (!config.enabled())
			{
				return true;
			}

			if (!(renderable instanceof Player))
			{
				return true;
			}

			final Player player = (Player) renderable;
			final Player local = client.getLocalPlayer();

			if (local == null)
			{
				return true;
			}

			final boolean isLocal = player == local;

			// Exemptions for other players
			if (!isLocal)
			{
				if (config.neverHideFriends() && player.isFriend())
				{
					return true;
				}

				if (config.neverHideClanMembers() && player.isClanMember())
				{
					return true;
				}

				if (config.neverHideFriendsChat() && player.isFriendsChatMember())
				{
					return true;
				}
			}

			final PlayerComposition composition = player.getPlayerComposition();
			if (composition == null)
			{
				// If we cannot read their equipment yet, do not hide them
				return true;
			}

			final boolean wearingFireMaxCape = isWearingFireMaxCape(composition);

			// Local player logic
			if (isLocal)
			{
				if (!config.hideSelfIfWearing())
				{
					return true;
				}

				return !wearingFireMaxCape;
			}

			// Other players logic
			return !wearingFireMaxCape;
		}
	};

	@Provides
	FireMaxCapeRemovalConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(FireMaxCapeRemovalConfig.class);
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