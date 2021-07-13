/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.movement;

import tomorrow.tomo.Client;
import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventMove;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.mods.Mod;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.cheats.world.TimerUtil;
import tomorrow.tomo.utils.math.MathUtil;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
@Mod(name = "Speed",description = "." , type = ModuleType.Movement)
public class Speed
        extends Module {
    private Mode<Enum> mode = new Mode("Mode", "mode", (Enum[]) SpeedMode.values(), (Enum) SpeedMode.HypixelHop);
    private int stage;
    private double movementSpeed;
    private double distance;
    private TimerUtil timer = new TimerUtil();

    public Speed() {
        super("Speed", ModuleType.Movement);
        this.addValues(this.mode);
    }

    @Override
    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0f;
    }

    private boolean canZoom() {
        if (this.mc.thePlayer.moving() && this.mc.thePlayer.onGround) {
            return true;
        }
        return false;
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {

    }

    @EventHandler
    private void onMove(EventMove e) {
        if (this.mode.getValue() == SpeedMode.HypixelHop) {
            if (this.canZoom() && this.stage == 1) {
                this.movementSpeed = 1.38 * MathUtil.getBaseMovementSpeed() - 0.01;
            } else if (this.canZoom() && this.stage == 2) {
                this.mc.thePlayer.motionY = 0.40842888 + Client.flag;
                e.setY(0.40842888);
                this.movementSpeed *= 1.7;
            } else if (this.stage == 3) {
                double difference = 0.30 * (this.distance - MathUtil.getBaseMovementSpeed());
                this.movementSpeed = this.distance - difference;
            } else {
                List collidingList = this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0));
                if (collidingList.size() > 0 || this.mc.thePlayer.isCollidedVertically && this.stage > 0) {
                    this.stage = this.mc.thePlayer.moving() ? 1 : 0;
                }
                this.movementSpeed = this.distance - this.distance / 159.0;
            }
            if (mc.thePlayer.motionY > 0) {
                this.movementSpeed = Math.max(this.movementSpeed * 1.1f, MathUtil.getBaseMovementSpeed());
                this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed * 1.1f);
            } else {
                this.movementSpeed = Math.max(this.movementSpeed, MathUtil.getBaseMovementSpeed());
                this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);
            }
            if (this.mc.thePlayer.moving()) {
                ++this.stage;
            }
        } else if (this.mode.getValue() == SpeedMode.Bhop) {
            this.mc.timer.timerSpeed = 1.07f;
            if (this.canZoom() && this.stage == 1) {
                this.movementSpeed = 2.55 * MathUtil.getBaseMovementSpeed() - 0.01;
            } else if (this.canZoom() && this.stage == 2) {
                this.mc.thePlayer.motionY = 0.3999;
                e.setY(0.3999);
                this.movementSpeed *= 2.1;
            } else if (this.stage == 3) {
                double difference = 0.66 * (this.distance - MathUtil.getBaseMovementSpeed());
                this.movementSpeed = this.distance - difference;
            } else {
                List collidingList = this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0));
                if (collidingList.size() > 0 || this.mc.thePlayer.isCollidedVertically && this.stage > 0) {
                    this.stage = this.mc.thePlayer.moving() ? 1 : 0;
                }
                this.movementSpeed = this.distance - this.distance / 159.0;
            }
            this.movementSpeed = Math.max(this.movementSpeed, MathUtil.getBaseMovementSpeed());
            this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);
            if (this.mc.thePlayer.moving()) {
                ++this.stage;
            }
        }
    }

    enum SpeedMode {
        Bhop,
        HypixelHop
    }

}

