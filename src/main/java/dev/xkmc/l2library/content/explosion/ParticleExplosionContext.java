package dev.xkmc.l2library.content.explosion;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;

public record ParticleExplosionContext(ParticleOptions small,
									   ParticleOptions large,
									   Holder<SoundEvent> sound) {
}