package net.wzz.starsource.procedure;

import net.wzz.starsource.item.ItemStarSourceInght;
import net.wzz.starsource.item.ItemBlessingStars;
import net.wzz.starsource.ElementsStarsourceMod;

import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;

import java.util.Map;

@ElementsStarsourceMod.ModElement.Tag
public class ProcedureHurt extends ElementsStarsourceMod.ModElement {
	public ProcedureHurt(ElementsStarsourceMod instance) {
		super(instance, 5);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			System.err.println("Failed to load dependency entity for procedure Hurt!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		if ((entity instanceof EntityPlayer)) {
			if (((EntityLivingBase) entity).getHeldItemMainhand()
					.getItem() == new ItemStack(ItemBlessingStars.block, (int) 1).getItem()) {
				((EntityPlayer) entity).inventory.clearMatchingItems(new ItemStack(ItemBlessingStars.block, (int) (1)).getItem(), -1, (int) 1,
						null);
				ItemStack _setstack = new ItemStack(ItemStarSourceInght.block, (int) (1));
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(((EntityPlayer) entity), _setstack);
				((EntityLivingBase) entity).setHealth((float) 1);
				if (dependencies.get("event") != null) {
					Object _obj = dependencies.get("event");
					if (_obj instanceof net.minecraftforge.fml.common.eventhandler.Event) {
						net.minecraftforge.fml.common.eventhandler.Event _evt = (net.minecraftforge.fml.common.eventhandler.Event) _obj;
						if (_evt.isCancelable())
							_evt.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event) {
		if (event != null && event.getEntity() != null) {
			Entity entity = event.getEntity();
			int i = (int) entity.posX;
			int j = (int) entity.posY;
			int k = (int) entity.posZ;
			World world = entity.world;
			java.util.HashMap<String, Object> dependencies = new java.util.HashMap<>();
			dependencies.put("x", i);
			dependencies.put("y", j);
			dependencies.put("z", k);
			dependencies.put("world", world);
			dependencies.put("entity", entity);
			dependencies.put("event", event);
			this.executeProcedure(dependencies);
		}
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}
}
