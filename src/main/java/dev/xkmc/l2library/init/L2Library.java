package dev.xkmc.l2library.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.base.effects.EffectToClient;
import dev.xkmc.l2library.capability.conditionals.ConditionalData;
import dev.xkmc.l2library.capability.player.PlayerCapToClient;
import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.init.data.L2ConfigManager;
import dev.xkmc.l2library.init.data.L2TagGen;
import dev.xkmc.l2library.init.data.LangData;
import dev.xkmc.l2library.init.events.listeners.GeneralEventHandler;
import dev.xkmc.l2library.init.events.select.SelectionRegistry;
import dev.xkmc.l2library.init.events.select.SetSelectedToServer;
import dev.xkmc.l2library.init.events.select.item.ItemSelectionListener;
import dev.xkmc.l2library.serial.config.PacketHandler;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2library.serial.config.SyncPacket;
import dev.xkmc.l2library.serial.ingredients.EnchantmentIngredient;
import dev.xkmc.l2library.serial.ingredients.MobEffectIngredient;
import dev.xkmc.l2library.serial.ingredients.PotionIngredient;
import dev.xkmc.l2library.util.raytrace.TargetSetPacket;
import dev.xkmc.l2serial.serialization.custom_handler.Handlers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.network.NetworkDirection.PLAY_TO_SERVER;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(L2Library.MODID)
@Mod.EventBusSubscriber(modid = L2Library.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class L2Library {

	public static final String MODID = "l2library";
	public static final Logger LOGGER = LogManager.getLogger();

	public static final PacketHandlerWithConfig PACKET_HANDLER = new PacketHandlerWithConfig(new ResourceLocation(MODID, "main"), 1,
			"l2library_config",
			// generic data sync
			e -> e.create(SyncPacket.class, PLAY_TO_CLIENT),
			e -> e.create(EffectToClient.class, PLAY_TO_CLIENT),
			e -> e.create(PlayerCapToClient.class, PLAY_TO_CLIENT),
			e -> e.create(TargetSetPacket.class, PLAY_TO_SERVER),
			// slot click
			// selection
			e -> e.create(SetSelectedToServer.class, PLAY_TO_SERVER)
	);

	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public L2Library() {
		Handlers.register();
		FMLJavaModLoadingContext ctx = FMLJavaModLoadingContext.get();
		IEventBus bus = ctx.getModEventBus();
		MinecraftForge.EVENT_BUS.register(GeneralEventHandler.class);
		bus.register(L2Library.class);
		bus.addListener(PacketHandler::setup);
		L2LibraryConfig.init();
		L2ConfigManager.register();
		ConditionalData.register();
		SelectionRegistry.register(0, ItemSelectionListener.INSTANCE);

		REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::genLang);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, L2TagGen::genItemTags);
	}

	@SubscribeEvent
	public static void registerCaps(RegisterCapabilitiesEvent event) {
		for (PlayerCapabilityHolder<?> holder : PlayerCapabilityHolder.INTERNAL_MAP.values()) {
			event.register(holder.cls);
		}
	}

	@SubscribeEvent
	public static void registerRecipeSerializers(RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
			CraftingHelper.register(EnchantmentIngredient.INSTANCE.id(), EnchantmentIngredient.INSTANCE);
			CraftingHelper.register(PotionIngredient.INSTANCE.id(), PotionIngredient.INSTANCE);
			CraftingHelper.register(MobEffectIngredient.INSTANCE.id(), MobEffectIngredient.INSTANCE);
		}
	}

}
