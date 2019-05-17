package com.darsan.game.worldobjects;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Filter;
import com.darsan.game.Game.MainGameScreen;
import com.darsan.game.Game.MyGdxGame;

import java.util.Random;

public class Chest extends InteractiveTileObject {
    private boolean open;

    public Chest(MainGameScreen screen, MapObject object) {
        super(screen, object, true, false);
        fixture.setUserData(this);
        Filter filter =new Filter();
        filter.categoryBits= MyGdxGame.SENSORBIT;
        fixture.setFilterData(filter);

    }


    public boolean isopen() {
        return open;
    }

    public int setOpen(boolean open) {
        this.open = open;
        return new Random().nextInt(3)+1;
    }
}
