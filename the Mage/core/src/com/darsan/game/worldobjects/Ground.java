package com.darsan.game.worldobjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Filter;
import com.darsan.game.Game.MainGameScreen;

public class Ground extends InteractiveTileObject {
    public Ground(MainGameScreen screen, MapObject object) {
        super(screen, object,false,false);
        fixture.setUserData(this);

    }
}
