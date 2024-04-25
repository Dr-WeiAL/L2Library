package dev.xkmc.l2library.init.events;

import dev.xkmc.l2library.base.effects.EffectToClient;
import dev.xkmc.l2library.init.L2Library;
import dev.xkmc.l2library.init.data.L2TagGen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = L2Library.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EffectSyncEvents {

	@Deprecated
	public static final Set<MobEffect> TRACKED = new HashSet<>();

	public static boolean tracked(MobEffect eff) {
		return TRACKED.contains(eff) || ForgeRegistries.MOB_EFFECTS.tags().getTag(L2TagGen.TRACKED_EFFECTS).contains(eff);
	}

	@SubscribeEvent
	public static void onPotionAddedEvent(MobEffectEvent.Added event) {
		if (tracked(event.getEffectInstance().getEffect())) {
			onEffectAppear(event.getEffectInstance().getEffect(), event.getEntity(), event.getEffectInstance().getAmplifier());
		}
	}


	@SubscribeEvent
	public static void onPotionRemoveEvent(MobEffectEvent.Remove event) {
		if (event.getEffectInstance() != null && tracked(event.getEffectInstance().getEffect())) {
			onEffectDisappear(event.getEffectInstance().getEffect(), event.getEntity());
		}
	}


	@SubscribeEvent
	public static void onPotionExpiryEvent(MobEffectEvent.Expired event) {
		if (event.getEffectInstance() != null && tracked(event.getEffectInstance().getEffect())) {
			onEffectDisappear(event.getEffectInstance().getEffect(), event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
		if (!(event.getTarget() instanceof LivingEntity le))
			return;
		for (MobEffect eff : le.getActiveEffectsMap().keySet()) {
			if (tracked(eff)) {
				onEffectAppear(eff, le, le.getActiveEffectsMap().get(eff).getAmplifier());
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerStopTracking(PlayerEvent.StopTracking event) {
		if (!(event.getTarget() instanceof LivingEntity le))
			return;
		for (MobEffect eff : le.getActiveEffectsMap().keySet()) {
			if (tracked(eff)) {
				onEffectDisappear(eff, le);
			}
		}
	}

	@SubscribeEvent
	public static void onServerPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayer e = (ServerPlayer) event.getEntity();
		if (e != null) {
			for (MobEffect eff : e.getActiveEffectsMap().keySet()) {
				if (tracked(eff)) {
					onEffectAppear(eff, e, e.getActiveEffectsMap().get(eff).getAmplifier());
				}
			}
		}
	}

	@SubscribeEvent
	public static void onServerPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
		ServerPlayer e = (ServerPlayer) event.getEntity();
		if (e != null) {
			for (MobEffect eff : e.getActiveEffectsMap().keySet()) {
				if (tracked(eff)) {
					onEffectDisappear(eff, e);
				}
			}
		}
	}

	private static void onEffectAppear(MobEffect eff, LivingEntity e, int lv) {
		if (e.level().isClientSide()) return;
		L2Library.PACKET_HANDLER.toTrackingPlayers(new EffectToClient(e.getId(), eff, true, lv), e);
	}

	private static void onEffectDisappear(MobEffect eff, LivingEntity e) {
		if (e.level().isClientSide()) return;
		L2Library.PACKET_HANDLER.toTrackingPlayers(new EffectToClient(e.getId(), eff, false, 0), e);
	}

}
