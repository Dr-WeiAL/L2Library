package dev.xkmc.l2library.init;

import dev.xkmc.l2library.compat.misc.GeckoLibEventHandlers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib.GeckoLib;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = L2Library.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class L2LibraryClient {

	@SubscribeEvent
	public static void clientInit(FMLClientSetupEvent event){
		if (ModList.get().isLoaded(GeckoLib.MOD_ID)) {
			MinecraftForge.EVENT_BUS.register(GeckoLibEventHandlers.class);
		}
	}

}
