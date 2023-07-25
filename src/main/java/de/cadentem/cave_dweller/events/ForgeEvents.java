package de.cadentem.cave_dweller.events;

import de.cadentem.cave_dweller.CaveDweller;
import de.cadentem.cave_dweller.entities.CaveDwellerEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = CaveDweller.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    private static final ConcurrentHashMap<Integer, Integer> HIT_COUNTER = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void handleDamage(final LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity instanceof CaveDwellerEntity caveDweller) {
            // TODO :: Also do it for OUT_OF_WORLD?
            if (event.getSource() == DamageSource.DROWN || event.getSource() == DamageSource.IN_WALL) {
                HIT_COUNTER.merge(caveDweller.getId(), 1, Integer::sum);

                if (HIT_COUNTER.get(caveDweller.getId()) > 5) {
                    HIT_COUNTER.remove(caveDweller.getId());

                    boolean couldTeleport = caveDweller.teleportToTarget();

                    if (!couldTeleport) {
                        // TODO :: Reduce the spawn time if this happens?
                        caveDweller.disappear();
                    }
                }
            }
        }
    }
}