package ccr4ft3r.appetite.registry;

import ccr4ft3r.appetite.ModConstants;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, ModConstants.MOD_ID);

    public static final RegistryObject<Potion> FROZEN_APPETITE = POTIONS.register("frozen_appetite",
        () -> new Potion(new MobEffectInstance(ModMobEffects.FROZEN_APPETITE.get(), 2400))
    );

    public static void register() {
        POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}