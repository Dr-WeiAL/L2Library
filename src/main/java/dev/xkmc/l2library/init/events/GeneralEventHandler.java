package dev.xkmc.l2library.init.events;

import dev.xkmc.l2library.base.explosion.BaseExplosion;
import dev.xkmc.l2library.init.L2Library;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

@Mod.EventBusSubscriber(modid = L2Library.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GeneralEventHandler {

	@SubscribeEvent
	public static void serverTick(TickEvent.ServerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		RayTraceUtil.serverTick();
	}

	@SubscribeEvent
	public static void onDetonate(ExplosionEvent.Detonate event) {
		if (event.getExplosion() instanceof BaseExplosion exp) {
			event.getAffectedEntities().removeIf(e -> !exp.hurtEntity(e));
		}
	}

}
