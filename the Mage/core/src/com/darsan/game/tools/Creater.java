package com.darsan.game.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;

import com.badlogic.gdx.maps.objects.RectangleMapObject;

import com.badlogic.gdx.maps.tiled.TiledMap;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.darsan.game.Game.MainGameScreen;
import com.darsan.game.Game.MyGdxGame;
import com.darsan.game.charecters.Archer;
import com.darsan.game.charecters.MiniBoss;
import com.darsan.game.charecters.Myhero;
import com.darsan.game.charecters.Warrior;


public class Creater {
    private static final String MYWARRIORREGION = "mywarrior";
    private static final String MYARCHERREGION="myArcher";
    Texture heart;
    private Array<Warrior> warrior;
    private Array<Archer> archer;
    public Creater(MainGameScreen screen){

        TiledMap map = screen.getMap();
        Myhero myhero;
        warrior = new Array<Warrior>();
archer=new Array<Archer>();

        Rectangle rect;
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){

             rect = ((RectangleMapObject) object).getRectangle();

            warrior.add(new Warrior(screen, rect.getX() / MyGdxGame.ppm, rect.getY() / MyGdxGame.ppm,MYWARRIORREGION));

        }

        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){

             rect = ((RectangleMapObject) object).getRectangle();

            archer.add(new Archer(screen, rect.getX() / MyGdxGame.ppm, rect.getY() / MyGdxGame.ppm,MYARCHERREGION));

        }




    }



    public Array<Warrior> getWarrior() {

        return warrior;

    }
    public Array<Archer> getArcher() {

        return archer;

    }



}