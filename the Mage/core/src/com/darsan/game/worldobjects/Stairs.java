package com.darsan.game.worldobjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.physics.box2d.Filter;
import com.darsan.game.Game.MainGameScreen;

public class Stairs extends InteractiveTileObject{


    public Stairs(MainGameScreen screen, MapObject object) {
        super(screen, object,false,false);
        fixture.setUserData(this);
    }
}
