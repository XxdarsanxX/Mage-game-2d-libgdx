package com.darsan.game.worldobjects;

import com.badlogic.gdx.maps.MapObject;
import com.darsan.game.Game.GameOverScreen;
import com.darsan.game.Game.MainGameScreen;

public class End extends InteractiveTileObject {
    public End(MainGameScreen screen, MapObject object) {
        super(screen, object, true, false);
        fixture.setUserData(this);
    }
    public void endGame(){
        screen.game.setScreen(new GameOverScreen(screen.game,"this was part one of my game hope u enjoyed it :D.....Made by Darsan"));
    }
}
