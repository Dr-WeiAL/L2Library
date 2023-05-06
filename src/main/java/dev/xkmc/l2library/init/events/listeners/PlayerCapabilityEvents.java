package dev.xkmc.l2library.init.events.listeners;

import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.init.L2Library;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = L2Library.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerCapabilityEvents {

	@SubscribeEvent
	public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof Player player) {
			for (PlayerCapabilityHolder<?> holder : PlayerCapabilityHolder.INTERNAL_MAP.values()) {
				event.addCapability(holder.id, holder.generateSerializer(player, player.level));
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.player.isAlive() && event.phase == TickEvent.Phase.END)
			for (PlayerCapabilityHolder<?> holder : PlayerCapabilityHolder.INTERNAL_MAP.values()) {
				holder.get(event.player).tick();
			}
	}

	@SubscribeEvent
	public static void onServerPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayer e = (ServerPlayer) event.getEntity();
		if (e != null) {
			for (PlayerCapabilityHolder<?> holder : PlayerCapabilityHolder.INTERNAL_MAP.values()) {
				holder.network.toClientSyncAll(e);
				holder.network.toTracking(e);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		for (PlayerCapabilityHolder<?> holder : PlayerCapabilityHolder.INTERNAL_MAP.values()) {
			CompoundTag tag0 = TagCodec.toTag(new CompoundTag(), holder.get(event.getOriginal()));
			assert tag0 != null;
			Wrappers.run(() -> TagCodec.fromTag(tag0, holder.cls, holder.get(event.getEntity()), f -> true));
			holder.get(event.getEntity()).onClone(event.isWasDeath());
			ServerPlayer e = (ServerPlayer) event.getEntity();
			holder.network.toClientSyncClone(e);
			holder.network.toTracking(e);
		}
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		for (PlayerCapabilityHolder<?> holder : PlayerCapabilityHolder.INTERNAL_MAP.values()) {
			if (!(event.getTarget() instanceof ServerPlayer e)) continue;
			holder.network.startTracking((ServerPlayer) event.getEntity(), e);
		}
	}

}
