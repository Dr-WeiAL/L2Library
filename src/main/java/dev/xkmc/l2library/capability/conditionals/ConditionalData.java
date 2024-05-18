package dev.xkmc.l2library.capability.conditionals;

import dev.xkmc.l2library.capability.player.PlayerCapabilityHolder;
import dev.xkmc.l2library.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2library.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2library.init.L2Library;
import dev.xkmc.l2library.init.L2LibraryConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import javax.annotation.Nullable;
import java.util.HashMap;

@SerialClass
public class ConditionalData extends PlayerCapabilityTemplate<ConditionalData> {

	public static final Capability<ConditionalData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	public static final PlayerCapabilityHolder<ConditionalData> HOLDER = new PlayerCapabilityHolder<>(
			new ResourceLocation(L2Library.MODID, "conditionals"), CAPABILITY,
			ConditionalData.class, ConditionalData::new, PlayerCapabilityNetworkHandler::new);

	public static void register() {
	}

	@SerialClass.SerialField
	public HashMap<TokenKey<?>, ConditionalToken> data = new HashMap<>();
	@SerialClass.SerialField
	public int tickSinceDeath = 0;


	private HashMap<TokenKey<?>, ConditionalToken> copy = null;

	@Override
	public void onClone(boolean isWasDeath) {
		tickSinceDeath = 0;
	}

	public <T extends ConditionalToken, C extends Context> T getOrCreateData(TokenProvider<T, C> setEffect, C ent) {
		return Wrappers.cast(data.computeIfAbsent(setEffect.getKey(), e -> setEffect.getData(ent)));
	}

	@Nullable
	public <T extends ConditionalToken> T getData(TokenKey<T> setEffect) {
		if (copy != null) {
			var ans = copy.get(setEffect);
			if (ans != null) {
				return Wrappers.cast(ans);
			}
		}
		return Wrappers.cast(data.get(setEffect));
	}

	@Override
	public void tick() {
		tickSinceDeath++;
		if (L2LibraryConfig.COMMON.restoreFullHealthOnRespawn.get() &&
				tickSinceDeath < 60 && player.getHealth() < player.getMaxHealth()) {
			player.setHealth(player.getMaxHealth());
		}
		copy = data;
		data = new HashMap<>();
		copy.entrySet().removeIf(e -> e.getValue().tick(player));
		copy.putAll(data);
		data = copy;
		copy = null;
	}

	public boolean hasData(TokenKey<?> eff) {
		return copy != null && copy.containsKey(eff) || data.containsKey(eff);
	}

}
