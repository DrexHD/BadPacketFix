package me.drex.badpacketfix;

import me.drex.badpacketfix.commands.BadPacketFixClientCommands;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class BadPacketFixClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			// These commands are only intended for testing (in development environment).
			BadPacketFixClientCommands.register();
		}
	}

}