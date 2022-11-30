package ru.nbk.carousel.nms;

import net.minecraft.server.v1_12_R1.EntityHorseZombie;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityLeash;
import net.minecraft.server.v1_12_R1.EnumHand;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import ru.nbk.carousel.horse.CarouselHorse;
import ru.nbk.carousel.horse.HorseType;

import java.util.function.Consumer;

public class CustomHorseZombie extends EntityHorseZombie implements CarouselHorse {

    private double correction;
    private boolean isReverced;
    private Consumer<CarouselHorse> onTick;

    public CustomHorseZombie(Location location, double correction, boolean isReverced) {
        super(((CraftWorld)location.getWorld()).getHandle());
        setNoGravity(true);
        setLocation(location.getX(), location.getY(), location.getZ(), 0F, 0F);
        this.correction = correction;
        this.isReverced = isReverced;
    }

    //Yбираем AI
    @Override
    protected void r(){
    }

    public void move(Location location){
        setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public CarouselHorse spawn() {
        world.addEntity(this);
        return this;
    }

    @Override
    public double getCorrection() {
        return correction;
    }

    @Override
    public boolean isReverced() {
        return isReverced;
    }

    @Override
    public void lesh(EntityLeash leashHitch) {
        unlesh();
        setLeashHolder(leashHitch, true);
    }

    @Override
    public HorseType getType() {
        return HorseType.HORSE_ZOMBIE;
    }

    @Override
    public void onTick() {
        if (onTick != null) onTick.accept(this);
    }

    @Override
    public void setOnTick(Consumer<CarouselHorse> onTick) {
        this.onTick = onTick;
    }

    //Yбераем логикy поводка
    @Override
    protected void cZ(){

    }

    private void unlesh(){
        if (getLeashHolder() != null) unleash(true, false);
    }

    //Делаем возможным всегда оседлать лошадь
    @Override
    public boolean a(EntityHuman paramEntityHuman, EnumHand paramEnumHand) {
        g(paramEntityHuman);
        return true;
    }

}
