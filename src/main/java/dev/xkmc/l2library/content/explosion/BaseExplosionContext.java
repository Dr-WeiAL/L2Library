package dev.xkmc.l2library.content.explosion;

import net.minecraft.world.level.Level;

public record BaseExplosionContext(Level level, double x, double y, double z, float r) {
}