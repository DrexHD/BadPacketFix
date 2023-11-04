package me.drex.badpacketfix;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BadPacketFix implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("badpacketfix");

	@Override
	public void onInitialize() {
		LOGGER.info("Initialized BadPacketFix");
	}
}