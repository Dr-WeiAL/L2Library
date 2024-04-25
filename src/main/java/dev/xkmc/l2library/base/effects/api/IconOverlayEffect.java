package dev.xkmc.l2library.base.effects.api;

import dev.xkmc.l2library.util.Proxy;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public interface IconOverlayEffect extends ClientRenderEffect {

	@Override
	default void render(LivingEntity entity, int lv, Consumer<DelayedEntityRender> adder) {
		if (entity == Proxy.getClientPlayer()) {
			return;
		}
		adder.accept(getIcon(entity, lv));
	}

	DelayedEntityRender getIcon(LivingEntity entity, int lv);

}
