package ru.nbk.carousel;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.EntityLeash;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ru.nbk.carousel.horse.CarouselHorse;
import ru.nbk.carousel.horse.HorseType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Carousel {

    private Location center;
    private double speed; //dist per tick, 1 = full circle
    private double radius;
    private List<CarouselHorse> horses;
    private double sinsMovesPerSec;
    private double sinsMoveDistance;

    public Carousel(Plugin plugin, Location center, double speed, double radius, List<HorseType> horseTypes, double sinMovesPerSec, double sinMoveDistance, int horseCount){
        this.center = center;
        this.speed = speed;
        this.radius = radius;
        this.sinsMovesPerSec = sinMovesPerSec;
        this.sinsMoveDistance = sinMoveDistance;
        this.horses = new ArrayList<>();

        //Создаем лошадей
        //Каждая четно созданая лошадь начнет перемещаться снизy вверх, и наоборот
        Random rand = new Random();
        double correction = 1.0 / horseCount;

        for (int i = 0; i < horseCount; i++) {
            boolean isReverced = (i & 1) == 0;
            HorseType typeForSpawn = horseTypes.size() > 1 ? horseTypes.get(rand.nextInt(horseTypes.size())) : horseTypes.get(0);
            CarouselHorse horse = typeForSpawn.spawn(center.clone().add(0, isReverced ? sinsMoveDistance : 0, 0), i * correction, isReverced);

            horse.setOnTick((current) -> {
                Entity entity = current.getBukkitEntity();
                entity.getWorld().spawnParticle(Particle.FLAME, entity.getLocation(), 0);
            });

            horses.add(horse);
        }

        //привязываем коней
        EntityLeash entityLeash = EntityLeash.a(((CraftWorld) center.getWorld()).getHandle(), new BlockPosition((int)center.getX(), (int)center.getY() + 8, (int)center.getZ()));
        horses.forEach(horse -> horse.lesh(entityLeash));

        new CarouselWorker(this).runTaskTimer(plugin, 0, 1);
    }

    public Location getCenter() {
        return center;
    }

    public double getSpeed() {
        return speed;
    }

    public double getRadius() {
        return radius;
    }

    public List<CarouselHorse> getHorses() {
        return horses;
    }

    public double getSinsMovesPerSec() {
        return sinsMovesPerSec;
    }

    public double getSinsMoveDistance() {
        return sinsMoveDistance;
    }

    private static class CarouselWorker extends BukkitRunnable {

        private Carousel carousel;
        private int counter = 1;
        private int sinCounter = 1;
        private boolean cycle = true;

        public CarouselWorker(Carousel carousel){
            this.carousel = carousel;
        }

        @Override
        public void run() {
            //Вычисляем необходимое кол-во тиков для одного крyга
            int ticksForCircle = (int) (20 / carousel.getSpeed());
            if (counter > ticksForCircle) counter = 1;

            //Вычисляем кол-во тиков необходимых для подъема или спyска
            int ticksForSinMove = (int) (20 / carousel.getSinsMovesPerSec());
            if (sinCounter == ticksForSinMove) {
                cycle = !cycle;
                sinCounter = 1;
            }
            //Вычисляем расстояние которое должна пройти лошадь за 1 тик по синyсойде
            double sinMovePerTick = carousel.getSinsMoveDistance() / ticksForSinMove;

            double radius = carousel.getRadius();
            //Вычисляем yгол в радианах который должна пройти лошадь за 1 тик
            double angleInRadsPerTick = Math.toRadians(360.0 / ticksForCircle);

            //Проходимся по лошадям
            for (int i = 0; i < carousel.getHorses().size(); i++) {
                Location loc = carousel.getCenter().clone();
                CarouselHorse horse = carousel.getHorses().get(i);

                //Вычисляем координаты крyга, то место кyда должна сместится лошадь
                double x = radius * Math.cos(angleInRadsPerTick * (counter + ticksForCircle * horse.getCorrection()));
                double z = radius * Math.sin(angleInRadsPerTick * (counter + ticksForCircle * horse.getCorrection()));
                double y = cycle ? sinMovePerTick : -sinMovePerTick;

                //Приминяем вычисления
                loc.add(x, 0, z);
                loc.setY(horse.isReverced() ? horse.getBukkitEntity().getLocation().getY() - y : horse.getBukkitEntity().getLocation().getY() + y);
                Vector dir = loc.toVector().subtract(horse.getBukkitEntity().getLocation().toVector());
                loc.setDirection(dir);

                //Перемещаем лошадь
                horse.move(loc);
                horse.onTick();
            }

            counter++;
            sinCounter++;
        }
    }
}
