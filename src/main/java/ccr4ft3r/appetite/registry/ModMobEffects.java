package ccr4ft3r.appetite.registry;

import ccr4ft3r.appetite.ModConstants;
import ccr4ft3r.appetite.effect.FrozenAppetiteEffect;
import ccr4ft3r.appetite.effect.BrainFreezeEffect;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModMobEffects {

    public static final DeferredRegister<Effect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, ModConstants.MOD_ID);

    public static final RegistryObject<Effect> FROZEN_APPETITE = MOB_EFFECTS.register("frozen_appetite", FrozenAppetiteEffect::new);
    public static final RegistryObject<Effect> BRAIN_FREEZE = MOB_EFFECTS.register("brain_freeze", BrainFreezeEffect::new);

    public static void register() {
        MOB_EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}