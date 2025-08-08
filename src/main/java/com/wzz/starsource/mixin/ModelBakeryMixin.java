package com.wzz.starsource.mixin;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Set;

import static net.minecraft.client.resources.model.ModelBakery.MISSING_MODEL_LOCATION;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {
    @Shadow @Final private Map<ResourceLocation, UnbakedModel> unbakedCache;

    @Shadow @Final private Set<ResourceLocation> loadingStack;

    @Shadow @Final private static Logger LOGGER;

    @Shadow protected abstract void loadModel(ResourceLocation p_119363_) throws Exception;

    @Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
    private void getModel(ResourceLocation p_119342_, CallbackInfoReturnable<UnbakedModel> cir) {
        if (this.unbakedCache.containsKey(p_119342_)) {
            cir.setReturnValue(this.unbakedCache.get(p_119342_));
        } else if (this.loadingStack.contains(p_119342_)) {
            throw new IllegalStateException("Circular reference while loading " + p_119342_);
        } else {
            this.loadingStack.add(p_119342_);
            UnbakedModel unbakedmodel = this.unbakedCache.get(MISSING_MODEL_LOCATION);

            while(!this.loadingStack.isEmpty()) {
                ResourceLocation resourcelocation = this.loadingStack.iterator().next();

                try {
                    if (!this.unbakedCache.containsKey(resourcelocation)) {
                        this.loadModel(resourcelocation);
                    }
                } catch (ModelBakery.BlockStateDefinitionException modelbakery$blockstatedefinitionexception) {
                    LOGGER.warn(modelbakery$blockstatedefinitionexception.getMessage());
                    this.unbakedCache.put(resourcelocation, unbakedmodel);
                } catch (Exception exception) {
                    if (!resourcelocation.getNamespace().contains("starsource") && !resourcelocation.getNamespace().contains("slashblade"))
                        LOGGER.warn("Unable to load model: '{}' referenced from: {}: {}", resourcelocation, p_119342_, exception);
                    this.unbakedCache.put(resourcelocation, unbakedmodel);
                } finally {
                    this.loadingStack.remove(resourcelocation);
                }
            }
            cir.setReturnValue(this.unbakedCache.getOrDefault(p_119342_, unbakedmodel));
        }
    }
}
