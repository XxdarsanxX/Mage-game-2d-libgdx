package com.darsan.game.worldobjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Filter;
import com.darsan.game.Game.MainGameScreen;
import com.darsan.game.Game.MyGdxGame;

public class Barrier extends InteractiveTileObject {
    public Barrier(MainGameScreen screen, MapObject object) {
        super(screen, object, false, false);
        Filter filter=new Filter();
        filter.categoryBits= MyGdxGame.SENSORBIT;
        filter.maskBits=MyGdxGame.HERO_BIT;
        fixture.setFilterData(filter);
    }
   public void destroyBody(){
        if(!world.isLocked()){
            world.destroyBody(body);
            body=null;
        }
    }
}
