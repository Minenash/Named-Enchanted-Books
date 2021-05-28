package com.minenash.named_enchanted_books.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookItemMixin extends Item {

    public EnchantedBookItemMixin(Settings settings) { super(settings); }

    @Override
    public Text getName(ItemStack stack) {

        NbtList list = EnchantedBookItem.getEnchantmentNbt(stack);
        if (list.isEmpty())
            return super.getName(stack);

        NbtCompound tag = list.getCompound(0);
        Enchantment enchantment = Registry.ENCHANTMENT.get(Identifier.tryParse(tag.getString("id")));

        if (enchantment != null) {
            MutableText text = new LiteralText("Book of ").append(new TranslatableText(enchantment.getTranslationKey()));
            int level = tag.getInt("lvl");

            if (level != 1 || enchantment.getMaxLevel() != 1)
                text.append(" ").append(new TranslatableText("enchantment.level." + level));

            if (list.size() > 1)
                text.append(" ...");

            return text;
        }
        return super.getName(stack);
    }
}
