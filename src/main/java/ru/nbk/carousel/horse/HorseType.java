package ru.nbk.carousel.horse;

import org.bukkit.Location;
import ru.nbk.carousel.nms.CustomHorse;
import ru.nbk.carousel.nms.CustomHorseSkeleton;
import ru.nbk.carousel.nms.CustomHorseZombie;

public enum HorseType {
    HORSE,
    HORSE_SKELETON,
    HORSE_ZOMBIE;

    public CarouselHorse spawn(Location location, double correction, boolean isReverced){
        CarouselHorse carouselHorse = null;
        switch (this){
            case HORSE -> carouselHorse = new CustomHorse(location, correction, isReverced);
            case HORSE_SKELETON -> carouselHorse = new CustomHorseSkeleton(location, correction, isReverced);
            case HORSE_ZOMBIE -> carouselHorse = new CustomHorseZombie(location, correction, isReverced);
        }
        return carouselHorse.spawn();
    }
}
