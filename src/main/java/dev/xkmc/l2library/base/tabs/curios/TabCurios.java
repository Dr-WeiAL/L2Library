//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.xkmc.l2library.base.tabs.curios;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2library.base.tabs.core.BaseTab;
import dev.xkmc.l2library.base.tabs.core.TabManager;
import dev.xkmc.l2library.base.tabs.core.TabRegistry;
import dev.xkmc.l2library.base.tabs.core.TabToken;
import dev.xkmc.l2library.init.L2Library;
import dev.xkmc.l2library.init.data.GeneralPurposeEvent;
import dev.xkmc.l2library.init.data.GeneralPurposeEventPacket;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.Curios;
import top.theillusivec4.curios.client.gui.CuriosScreen;

public class TabCurios extends BaseTab<TabCurios> {

	static TabToken<TabCurios> tab;

	public TabCurios(TabToken<TabCurios> token, TabManager manager, ItemStack stack, Component title) {
		super(token, manager, stack, title);
	}

	public void onTabClicked() {
		L2Library.PACKET_HANDLER.toServer(new GeneralPurposeEventPacket(GeneralPurposeEvent.CURIO_OPEN));
	}

	@Override
	public void renderWidget(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.enableBlend();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, TEXTURE);
			token.type.draw(stack, manager.getScreen(), getX(), getY(), manager.selected == token, token.index);
			RenderSystem.setShaderTexture(0, new ResourceLocation(Curios.MODID, "textures/gui/inventory.png"));
			blit(stack, getX() + 6, getY() + 10, 50, 14, 14, 14);
		}
		if (this.token.index == TabRegistry.getTabs().size() - 1) { // draw on last
			manager.onToolTipRender(stack, mouseX, mouseY);
		}
	}
}
