package com.example.separatebobbers;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/** Выдаёт каждой удочке уникальный ID (хранится в custom_data предмета). */
public final class RodIds {
    private static final String KEY = "separatebobbers_id";

    private RodIds() {}

    public static String getOrCreate(ItemStack stack) {
        if (stack.isEmpty()) return null;

        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data != null) {
            Optional<String> existing = data.copyTag().getString(KEY);
            if (existing.isPresent()) return existing.get();
        }

        String id = UUID.randomUUID().toString();
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putString(KEY, id));
        return id;
    }
}
