package dev.xkmc.l2library.events;

import dev.xkmc.l2library.init.L2Library;
import dev.xkmc.l2library.util.raytrace.EntityTarget;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = L2Library.MODID, bus = EventBusSubscriber.Bus.GAME)
public class MiscClientEventHandler {

	@SubscribeEvent
	public static void clientTick(ClientTickEvent.Post event) {
		for (EntityTarget target : EntityTarget.LIST) {
			target.tickRender();
		}
	}

}
