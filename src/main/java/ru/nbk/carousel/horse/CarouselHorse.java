package ru.nbk.carousel.horse;

import net.minecraft.server.v1_12_R1.EntityLeash;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface CarouselHorse{

    //Двигаем кобылy
    void move(Location location);

    CarouselHorse spawn();

    Entity getBukkitEntity();

    //Добавочная коррекция местоположения коня на крyге
    double getCorrection();

    //Флаг для определения того, должна ли лошадь изначально двигаться в низ или вверх
    boolean isReverced();

    //Для привязки поводка
    void lesh(EntityLeash leashHitch);

    HorseType getType();

    void onTick();

    void setOnTick(Consumer<CarouselHorse> onTick);
}
