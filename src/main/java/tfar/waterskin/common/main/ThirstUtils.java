package tfar.waterskin.common.main;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import org.apache.commons.lang3.tuple.Triple;
import toughasnails.api.item.ItemDrink;
import toughasnails.api.thirst.IDrink;
import toughasnails.api.thirst.WaterType;
import toughasnails.config.json.DrinkData;
import toughasnails.init.ModConfig;

public class ThirstUtils {


    //thirst, hydration, poison chance
    public static <T extends Enum<T> & IDrink> Triple<Integer,Float,Float> getDrinkData(ItemStack stack) {
        if (stack.getItem().equals(Items.POTIONITEM)) {
            if (PotionUtils.getFullEffectsFromItem(stack).isEmpty()) {
                return Triple.of(WaterType.NORMAL.getThirst(), WaterType.NORMAL.getHydration(), WaterType.NORMAL.getPoisonChance());
            } else {
                return Triple.of( 4, 0.3F, 0F);
            }
        } else if (stack.getItem() instanceof ItemDrink) {
            T type = getData(stack);
            return type == null ? null : Triple.of(type.getThirst(),type.getHydration(),type.getPoisonChance());
        } else {
            String registryName = stack.getItem().getRegistryName().toString();
            if (ModConfig.drinkData.containsKey(registryName)) {
                for (DrinkData drinkData : ModConfig.drinkData.get(registryName)) {
                    if (drinkData.getPredicate().apply(stack)) {
                        return Triple.of(drinkData.getThirstRestored(),drinkData.getHydrationRestored(),drinkData.getPoisonChance());
                    }
                }
            }
        }
        return null;
    }

    public static <T extends Enum<T> & IDrink> T getData(ItemStack stack) {
        ItemDrink<T> itemDrink = (ItemDrink<T>)stack.getItem();
        return itemDrink.getTypeFromMeta(stack.getMetadata());
    }
}
