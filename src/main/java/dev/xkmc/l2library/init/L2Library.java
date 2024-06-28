package dev.xkmc.l2library.init;

import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2library.serial.config.SyncPacket;
import dev.xkmc.l2library.util.raytrace.TargetSetPacket;
import dev.xkmc.l2serial.network.PacketHandler;
import dev.xkmc.l2serial.serialization.custom_handler.Handlers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(L2Library.MODID)
@EventBusSubscriber(modid = L2Library.MODID, bus = EventBusSubscriber.Bus.MOD)
public class L2Library {

	public static final String MODID = "l2library";
	public static final Logger LOGGER = LogManager.getLogger();

	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final PacketHandler PACKET_HANDLER = new PacketHandler(MODID, 1,
			e -> e.create(SyncPacket.class),
			e -> e.create(TargetSetPacket.class)
	);

	public L2Library(IEventBus bus) {
		Handlers.register();
		L2LibReg.register(bus);
		L2LibraryConfig.init();
		NeoForge.EVENT_BUS.addListener(L2Library::onServerStarted);
	}

	public static void onServerStarted(ServerStartedEvent event) {
	}

}
