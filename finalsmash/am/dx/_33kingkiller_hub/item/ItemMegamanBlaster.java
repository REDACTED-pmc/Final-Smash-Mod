package am.dx._33kingkiller_hub.item;

import am.dx._33kingkiller_hub.main.Smash;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemMegamanBlaster extends ItemBow {
	
		//Constructor.
		public ItemMegamanBlaster() {
			this.setCreativeTab(Smash.tabSmash);
			this.maxStackSize = 1;
	        this.setMaxDamage(384);
		}
		
		private ItemStack func_185060_a(EntityPlayer player)
	    {
	        if (this.func_185058_h_(player.getHeldItem(EnumHand.OFF_HAND)))
	        {
	            return player.getHeldItem(EnumHand.OFF_HAND);
	        }
	        else if (this.func_185058_h_(player.getHeldItem(EnumHand.MAIN_HAND)))
	        {
	            return player.getHeldItem(EnumHand.MAIN_HAND);
	        }
	        else
	        {
	            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
	            {
	                ItemStack itemstack = player.inventory.getStackInSlot(i);

	                if (this.func_185058_h_(itemstack))
	                {
	                    return itemstack;
	                }
	            }

	            return null;
	        }
	    }
			
		//Creates an arrow after the bow is shot (and other things).
		@Override
		public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	    {
	        if (entityLiving instanceof EntityPlayer)
	        {
	            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
	            boolean flag = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.infinity, stack) > 0;
	            ItemStack itemstack = this.func_185060_a(entityplayer);

	            int i = this.getMaxItemUseDuration(stack) - timeLeft;
	            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, (EntityPlayer)entityLiving, i, itemstack != null || flag);
	            if (i < 0) return;

	            if (itemstack != null || flag)
	            {
	                if (itemstack == null)
	                {
	                    itemstack = new ItemStack(Items.arrow);
	                }

	                float f = func_185059_b(i);

	                if ((double)f >= 0.1D)
	                {
	                    boolean flag1 = flag && itemstack.getItem() instanceof ItemArrow; //Forge: Fix consuming custom arrows.

	                    if (!worldIn.isRemote)
	                    {
	                        ItemArrow itemarrow = (ItemArrow)((ItemArrow)(itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() : Items.arrow));
	                        EntityArrow entityarrow = itemarrow.makeTippedArrow(worldIn, itemstack, entityplayer);
	                        entityarrow.func_184547_a(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);

	                        if (f == 1.0F)
	                        {
	                            entityarrow.setIsCritical(true);
	                        }

	                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.power, stack);

	                        if (j > 0)
	                        {
	                            entityarrow.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
	                        }

	                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.punch, stack);

	                        if (k > 0)
	                        {
	                            entityarrow.setKnockbackStrength(k);
	                        }

	                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.flame, stack) > 0)
	                        {
	                            entityarrow.setFire(100);
	                        }

	                        stack.damageItem(1, entityplayer);

	                        if (flag1)
	                        {
	                            entityarrow.canBePickedUp = EntityArrow.PickupStatus.CREATIVE_ONLY;
	                        }

	                        worldIn.spawnEntityInWorld(entityarrow);
	                    }

	                    worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.entity_arrow_shoot, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

	                    if (!flag1)
	                    {
	                        --itemstack.stackSize;

	                        if (itemstack.stackSize == 0)
	                        {
	                            entityplayer.inventory.deleteStack(itemstack);
	                        }
	                    }

	                    entityplayer.addStat(StatList.func_188057_b(this));
	                }
	                if(entityplayer.inventory.hasItemStack(new ItemStack(Smash.megamanBlaster)) && !entityplayer.capabilities.isCreativeMode) {
	            		entityplayer.inventory.deleteStack(new ItemStack(Smash.megamanBlaster, 1));
	            	}
	            }
	        }
	    }
			
		//Happens when the player right-clicks while holding the bow.
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	    {
	        boolean flag = this.func_185060_a(playerIn) != null;

	        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemStackIn, worldIn, playerIn, hand, flag);
	        if (ret != null) return ret;

	        if (!playerIn.capabilities.isCreativeMode && !flag)
	        {
	            return !flag ? new ActionResult(EnumActionResult.FAIL, itemStackIn) : new ActionResult(EnumActionResult.PASS, itemStackIn);
	        }
	        else
	        {
	            playerIn.setActiveHand(hand);
	            return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	        }
	    }
}
