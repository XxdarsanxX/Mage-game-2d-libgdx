package com.darsan.game.worldobjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Filter;
import com.darsan.game.Game.MainGameScreen;
import com.darsan.game.Game.MyGdxGame;

public class Ladder extends InteractiveTileObject {
    public Ladder(MainGameScreen screen, MapObject object) {
        super(screen, object,true,false);
        fixture.setUserData(this);
        Filter filter=new Filter();
        filter.categoryBits= MyGdxGame.SENSORBIT;
        fixture.setFilterData(filter);

    }
}
