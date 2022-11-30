package ru.nbk.carousel;

import org.bukkit.plugin.java.JavaPlugin;
import ru.nbk.carousel.command.CarouselCommand;
import ru.nbk.carousel.configuration.CarouselConfig;
import ru.nbk.carousel.nms.EntityMap;

public class Main extends JavaPlugin {

    public void onEnable(){
        EntityMap.registerAll();
        CarouselConfig config = new CarouselConfig(this);
        new CarouselCommand(this, config);
    }

}
