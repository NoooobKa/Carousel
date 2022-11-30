package ru.nbk.carousel.nms;

import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityTypes;
import net.minecraft.server.v1_12_R1.MinecraftKey;


public enum EntityMap {

    CUSTOM_HORSE("CustomHorse", 100, CustomHorse.class),
    CUSTOM_HORSE_SKELETON("CustomHorseSkeleton", 28, CustomHorseSkeleton.class),
    CUSTOM_HORSE_ZOMBIE("CustomHorseZombie", 29, CustomHorseZombie.class);

    private String entityName;

    private int id;

    private Class<? extends Entity> clazz;

    EntityMap(String entityName, int id, Class<? extends Entity> clazz) {
        this.entityName = entityName;
        this.id = id;
        this.clazz = clazz;
    }

    public void register() {
        MinecraftKey key = new MinecraftKey(entityName);
        EntityTypes.b.a(this.id, key, this.clazz);
    }

    public static void registerAll(){
        for (EntityMap entity : values()){
            entity.register();
        }
    }

}
