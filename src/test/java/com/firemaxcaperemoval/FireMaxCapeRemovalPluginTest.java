package com.firemaxcaperemoval;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class FireMaxCapeRemovalPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(FireMaxCapeRemovalPlugin.class);
		RuneLite.main(args);
	}
}