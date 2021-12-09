/*
 * Decompiled with CFR 0_132.
 */
package tomorrow.tomo.mods.modules.movement;

import tomorrow.tomo.event.EventHandler;
import tomorrow.tomo.event.events.world.EventMove;
import tomorrow.tomo.event.events.world.EventPreUpdate;
import tomorrow.tomo.event.value.Mode;
import tomorrow.tomo.managers.ModuleManager;
import tomorrow.tomo.mods.Module;
import tomorrow.tomo.mods.ModuleType;
import tomorrow.tomo.utils.cheats.world.TimerUtil;
import tomorrow.tomo.utils.math.MathUtil;

import java.util.List;

public class Speed
        extends Module {
    private Mode mode = new Mode("Mode", "mode", new String[]{"Bhop", "HypixelHop"}, "HypixelHop");
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
        this.setSuffix(mode.getModeAsString());
    }

    @EventHandler
    private void onMove(EventMove e) {
        if (this.mode.getValue().equals("HypixelHop")) {
            if (this.canZoom()) {
                e.setY(mc.thePlayer.motionY = 0.42F);
                this.movementSpeed = 0.45f;
            } else {
                this.movementSpeed = Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
            }
            this.movementSpeed = Math.max(this.movementSpeed, MathUtil.getBaseMovementSpeed());

            this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);
        } else if (this.mode.getValue().equals("Bhop")) {
            if (this.canZoom()) {
                e.setY(mc.thePlayer.motionY = 0.18F);
                this.movementSpeed = 0.9f;
            } else {
                this.movementSpeed = 0.8f;
            }

            this.mc.thePlayer.setMoveSpeed(e, this.movementSpeed);
        }
        ((TargetStrafe) ModuleManager.getModuleByClass(TargetStrafe.class)).strafe(e, movementSpeed);
    }
}

