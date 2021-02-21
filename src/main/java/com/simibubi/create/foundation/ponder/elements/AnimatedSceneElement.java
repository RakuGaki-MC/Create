package com.simibubi.create.foundation.ponder.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.simibubi.create.foundation.ponder.PonderWorld;
import com.simibubi.create.foundation.utility.LerpedFloat;
import com.simibubi.create.foundation.utility.MatrixStacker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public abstract class AnimatedSceneElement extends PonderSceneElement {

	protected Vec3d fadeVec;
	protected LerpedFloat fade;

	public AnimatedSceneElement() {
		fade = LerpedFloat.linear()
			.startWithValue(0);
	}

	public void forceApplyFade(float fade) {
		this.fade.startWithValue(fade);
	}
	
	public void setFade(float fade) {
		this.fade.setValue(fade);
	}

	public void setFadeVec(Vec3d fadeVec) {
		this.fadeVec = fadeVec;
	}
	
	@Override
	public final void renderFirst(PonderWorld world, IRenderTypeBuffer buffer, MatrixStack ms) {
		ms.push();
		float currentFade = applyFade(ms);
		renderFirst(world, buffer, ms, currentFade);
		ms.pop();
	}

	@Override
	public final void renderLayer(PonderWorld world, IRenderTypeBuffer buffer, RenderType type, MatrixStack ms) {
		ms.push();
		float currentFade = applyFade(ms);
		renderLayer(world, buffer, type, ms, currentFade);
		ms.pop();
	}
	
	@Override
	public final void renderLast(PonderWorld world, IRenderTypeBuffer buffer, MatrixStack ms) {
		ms.push();
		float currentFade = applyFade(ms);
		renderLast(world, buffer, ms, currentFade);
		ms.pop();
	}

	protected float applyFade(MatrixStack ms) {
		float currentFade = fade.getValue(Minecraft.getInstance()
			.getRenderPartialTicks());
		if (fadeVec != null)
			MatrixStacker.of(ms)
				.translate(fadeVec.scale(-1 + currentFade));
		return currentFade;
	}

	protected void renderLayer(PonderWorld world, IRenderTypeBuffer buffer, RenderType type, MatrixStack ms,
		float fade) {}

	protected void renderFirst(PonderWorld world, IRenderTypeBuffer buffer, MatrixStack ms, float fade) {}

	protected void renderLast(PonderWorld world, IRenderTypeBuffer buffer, MatrixStack ms, float fade) {}

	protected int lightCoordsFromFade(float fade) {
		int light = 0xF000F0;
		if (fade != 1) {
			light = (int) (MathHelper.lerp(fade, 5, 0xF));
			light = light << 4 | light << 20;
		}
		return light;
	}

}