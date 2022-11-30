package ru.nbk.carousel.configuration;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.nbk.carousel.horse.HorseType;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CarouselConfig {

    private File rawConfig;
    private YamlConfiguration config;

    public CarouselConfig(JavaPlugin plugin){
        if (!plugin.getDataFolder().exists()){
            plugin.getDataFolder().mkdirs();
        }

        this.rawConfig = new File(plugin.getDataFolder(), "CarouselConfig.yml");
        if (!rawConfig.exists()){
            try {
                rawConfig.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.config = YamlConfiguration.loadConfiguration(rawConfig);
        checkDefault();
    }

    public void save(){
        try {
            config.save(rawConfig);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setIfNotExists(String path, Object o){
        if (!config.contains(path)){
            config.set(path, o);
        }
    }

    private void checkDefault(){
        setIfNotExists("horse-count", 6);
        setIfNotExists("horse-types", Arrays.asList(HorseType.HORSE.name(), HorseType.HORSE_ZOMBIE.name(), HorseType.HORSE_SKELETON.name()));
        setIfNotExists("carousel-speed", 0.25);
        setIfNotExists("carousel-sin-move-per-sec", 1.0);
        setIfNotExists("carousel-sin-move-distance", 2.5);
        setIfNotExists("carousel-leash-height", 8.0);
        setIfNotExists("carousel-radius", 8.0);
        save();
    }

    public int getHorseCount(){
        return config.getInt("horse-count");
    }

    public List<HorseType> getHorseTypes(){
        return config.getStringList("horse-types").stream().map(type -> HorseType.valueOf(type)).collect(Collectors.toList());
    }

    public double getCarouselSpeed(){
        return config.getDouble("carousel-speed");
    }

    public double getCarouselSinMovePerSec(){
        return config.getDouble("carousel-sin-move-per-sec");
    }

    public double getCarouselSinMoveDistance(){
        return config.getDouble("carousel-sin-move-distance");
    }

    public double getCarouselLeashHeight(){
        return config.getDouble("carousel-leash-height");
    }

    public double getCarouselRadius(){
        return config.getDouble("carousel-radius");
    }
}
