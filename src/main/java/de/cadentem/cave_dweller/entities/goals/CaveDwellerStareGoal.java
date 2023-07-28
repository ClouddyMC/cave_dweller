package de.cadentem.cave_dweller.entities.goals;

import de.cadentem.cave_dweller.entities.CaveDwellerEntity;
import de.cadentem.cave_dweller.util.Utils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CaveDwellerStareGoal extends Goal {
    private final CaveDwellerEntity caveDweller;

    private boolean wasNotLookingPreviously;
    private int lookedAtCount;

    public CaveDwellerStareGoal(final CaveDwellerEntity caveDweller) {
        this.caveDweller = caveDweller;
    }

    @Override
    public boolean canUse() {
        if (caveDweller.isInvisible()) {
            return false;
        }

        if (!Utils.isValidPlayer(caveDweller.getTarget())) {
            return false;
        }

        return caveDweller.currentRoll == Roll.STARE;
    }

    @Override
    public boolean canContinueToUse() {
        if (!Utils.isValidPlayer(caveDweller.getTarget())) {
            return false;
        }

        return caveDweller.currentRoll == Roll.STARE;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        lookedAtCount = 0;
        wasNotLookingPreviously = false;
        caveDweller.pleaseStopMoving = false;
        caveDweller.getEntityData().set(CaveDwellerEntity.SPOTTED_ACCESSOR, false);
    }

    @Override
    public void tick() {
        LivingEntity target = caveDweller.getTarget();

        if (target == null) {
            caveDweller.disappear();
            return;
        }

        boolean actuallyLooking = caveDweller.targetIsLookingAtMe && target.hasLineOfSight(caveDweller);

        if (wasNotLookingPreviously && actuallyLooking) {
            lookedAtCount++;
        }

        if (lookedAtCount > 10 && actuallyLooking) {
            if (caveDweller.getRandom().nextDouble() < 0.1) {
                caveDweller.disappear();
            } else if (caveDweller.getRandom().nextDouble() < 0.1) {
                caveDweller.pickRoll(List.of(Roll.CHASE, Roll.FLEE));
            }
        }

        // Move towards the player when they are not looking
        if (!actuallyLooking) {
            caveDweller.pleaseStopMoving = false;
            caveDweller.getNavigation().moveTo(target, 1);
        } else {
            caveDweller.pleaseStopMoving = true;
            caveDweller.getNavigation().stop();
            caveDweller.setDeltaMovement(Vec3.ZERO);
        }

        caveDweller.getLookControl().setLookAt(target);
        wasNotLookingPreviously = !actuallyLooking;
    }
}