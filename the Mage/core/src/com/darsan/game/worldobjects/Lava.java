package com.darsan.game.worldobjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.darsan.game.Game.MainGameScreen;

public class Lava extends InteractiveTileObject{

    public Lava(MainGameScreen screen, MapObject object) {
        super(screen, object,true,false);
        fixture.setUserData(this);
    }
}
