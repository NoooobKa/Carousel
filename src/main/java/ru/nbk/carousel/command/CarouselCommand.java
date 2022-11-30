package ru.nbk.carousel.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.nbk.carousel.Carousel;
import ru.nbk.carousel.configuration.CarouselConfig;

public class CarouselCommand implements CommandExecutor {

    private Plugin plugin;
    private CarouselConfig config;

    public CarouselCommand(JavaPlugin plugin, CarouselConfig config){
        this.plugin = plugin;
        this.config = config;

        plugin.getCommand("Carousel").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        new Carousel(plugin,
                player.getLocation(),
                config.getCarouselSpeed(),
                config.getCarouselRadius(),
                config.getHorseTypes(),
                config.getCarouselSinMovePerSec(),
                config.getCarouselSinMoveDistance(),
                config.getHorseCount());

        return true;
    }

}
