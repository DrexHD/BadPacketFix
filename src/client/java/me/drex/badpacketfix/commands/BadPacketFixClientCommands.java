package me.drex.badpacketfix.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import me.drex.badpacketfix.mixin.client.MultiPlayerGameModeAccessor;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class BadPacketFixClientCommands {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            LiteralArgumentBuilder<FabricClientCommandSource> root = literal("bad-packet-fix-client");
            LiteralArgumentBuilder<FabricClientCommandSource> containerClick = literal("containerClick");
            for (ClickType clickType : ClickType.values()) {
                containerClick.then(
                    argument("count", IntegerArgumentType.integer(1)).then(
                        argument("slot", IntegerArgumentType.integer()).then(
                            argument("button", IntegerArgumentType.integer()).then(
                                literal(clickType.name()).executes(context ->
                                    sendInvalidSwapPacket(context.getSource(), IntegerArgumentType.getInteger(context, "count"), IntegerArgumentType.getInteger(context, "slot"), IntegerArgumentType.getInteger(context, "button"), clickType))))));
            }
            containerClick.executes(BadPacketFixClientCommands::sendInvalidSwapPacket);
            root.then(containerClick);
            dispatcher.register(root);
        });
    }

    private static int sendInvalidSwapPacket(FabricClientCommandSource source, int count, int slot, int button, ClickType clickType) {
        Minecraft client = source.getClient();
        LocalPlayer player = source.getPlayer();
        ClientPacketListener connection = ((MultiPlayerGameModeAccessor) client.gameMode).getConnection();
        for (int i = 0; i < count; i++) {
            connection.send(
                new ServerboundContainerClickPacket(
                    player.inventoryMenu.containerId,
                    player.inventoryMenu.getStateId(),
                    slot,
                    button,
                    clickType,
                    player.inventoryMenu.getCarried().copy(),
                    Int2ObjectMaps.emptyMap()
                )
            );
        }
        return 1;
    }

    private static int sendInvalidSwapPacket(CommandContext<FabricClientCommandSource> context) {
        return sendInvalidSwapPacket(context.getSource(), 1000, 0, -1, ClickType.SWAP);
    }

}
