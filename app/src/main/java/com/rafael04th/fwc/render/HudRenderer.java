package com.rafael04th.fwc.render;

import com.rafael04th.fwc.core.impl.GLGraphics;
import com.rafael04th.fwc.graphics.ShaderProgram;
import com.rafael04th.fwc.graphics.Texture;
import com.rafael04th.fwc.graphics.TextureRegion;
import com.rafael04th.fwc.graphics.render.SpriteBatcher;
import com.rafael04th.fwc.input.VirtualButton;
import com.rafael04th.fwc.input.VirtualJoystick;

public class HudRenderer {
    private final TextureRegion joystickBaseRegion, joystickHandleRegion, btnBreakRegion, btnPlaceRegion, btnJumpRegion, btnNextBlockRegion, crossRegion;
    public HudRenderer(TextureRegion joystickBaseRegion, TextureRegion joystickHandleRegion, TextureRegion btnBreakRegion, TextureRegion btnPlaceRegion, TextureRegion btnJumpRegion, TextureRegion btnNextBlockRegion, TextureRegion crossRegion) {
        this.joystickBaseRegion = joystickBaseRegion;
        this.joystickHandleRegion = joystickHandleRegion;
        this.btnBreakRegion = btnBreakRegion;
        this.btnPlaceRegion = btnPlaceRegion;
        this.btnJumpRegion = btnJumpRegion;
        this.btnNextBlockRegion = btnNextBlockRegion;
        this.crossRegion = crossRegion;
    }
    public void render(
            SpriteBatcher batcher,
            ShaderProgram program,
            Texture atlas,
            GLGraphics glGraphics,
            VirtualJoystick joystick,
            VirtualButton breakButton,
            VirtualButton placeButton,
            VirtualButton jumpButton,
            VirtualButton nextButton) {
        batcher.beginBatch(program, atlas);

        {
            float diameter = joystick.radius * 2;
            batcher.drawSprite(joystick.centerX, joystick.centerY, diameter, diameter, joystickBaseRegion);
            batcher.drawSprite(joystick.currentX(), joystick.currentY(), diameter/4f, diameter/4f, joystickHandleRegion);
        }
        {
            batcher.drawSprite(breakButton.x+ (float) breakButton.width /2, breakButton.y+ (float) breakButton.height /2, breakButton.width, breakButton.height, btnBreakRegion);
            batcher.drawSprite(placeButton.x+ (float) placeButton.width /2, placeButton.y+ (float) placeButton.height /2, placeButton.width, placeButton.height, btnPlaceRegion);
            batcher.drawSprite(jumpButton.x+ (float) jumpButton.width /2, jumpButton.y+ (float) jumpButton.height /2, jumpButton.width, jumpButton.height, btnJumpRegion);
            batcher.drawSprite(nextButton.x+ (float) nextButton.width /2, nextButton.y+ (float) nextButton.height /2, nextButton.width, nextButton.height, btnNextBlockRegion);
        }
        {
            // Aimcross
            float x = (float) glGraphics.getWidth() /2;
            float y = (float) glGraphics.getHeight() /2;
            float len = Math.min(x,y)/10;
            batcher.drawSprite(x, y, len, len, crossRegion);
        }
        batcher.endBatch(program);
    }
}
