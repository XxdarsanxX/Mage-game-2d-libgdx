package com.darsan.game.detecters;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.darsan.game.Game.MyGdxGame;
import com.darsan.game.charecters.Archer;
import com.darsan.game.charecters.MiniBoss;
import com.darsan.game.charecters.Myhero;
import com.darsan.game.charecters.Warrior;
import com.darsan.game.spells.Arrow;
import com.darsan.game.spells.GreenBall;
import com.darsan.game.spells.ShadowBall;
import com.darsan.game.worldobjects.Castle;
import com.darsan.game.worldobjects.Chest;
import com.darsan.game.worldobjects.End;
import com.darsan.game.worldobjects.Ground;
import com.darsan.game.worldobjects.Ladder;
import com.darsan.game.worldobjects.Lava;
import com.darsan.game.worldobjects.MovingObject;
import com.darsan.game.worldobjects.Save;
import com.darsan.game.worldobjects.Stairs;

public class WorldContactListner implements ContactListener {

public WorldContactListner(){

}
    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if (fa == null || fb == null) {
            return;
        }
        if (fb.getUserData() == null || fa.getUserData() == null) {
            return;
        }

        if (fa.getUserData() instanceof Myhero || fb.getUserData() instanceof Myhero) {
            if (!(fa.getUserData() instanceof Myhero)) {
                fa = contact.getFixtureB();
                fb = contact.getFixtureA();
            }
           Myhero myhero= (Myhero) fa.getUserData();
            if (fa.getUserData() instanceof Myhero && isAsolid(fb)) {
                System.out.println("ucan jump");
                myhero.setcanjump(true);
            }
            else if( fa.getUserData()instanceof Myhero&& fb.getUserData()instanceof Stairs){
                System.out.println("hit stairs");
                myhero.onstairs=true;
            }else if(fb.getUserData()instanceof End){
                ((End) fb.getUserData()).endGame();
            }
            else if(fb.getUserData()instanceof ShadowBall){
                ((ShadowBall) fb.getUserData()).setColided(true);

            }
            else if(fb.getUserData()instanceof Arrow){
                ((Arrow) fb.getUserData()).setColided(true);
                Myhero.damage(1);
            }
            if(fb.getUserData() instanceof Lava){
                myhero.currentState= Myhero.State.DEAD;
            }else if(fb.getUserData()instanceof Ladder){
                System.out.println("bzhy");
               myhero.setOnLadder(true);
            }else if(fb.getUserData()instanceof Chest){
                myhero.setIsonChest(true);
                myhero.setChest((Chest) fb.getUserData());
                System.out.println("onchest");
            }else if(fb.getUserData()instanceof Save){
                myhero.onsavepoint=true;
            }

        } else if (fa.getUserData() instanceof ShadowBall || fb.getUserData() instanceof ShadowBall) {
            if ((!(fa.getUserData() instanceof ShadowBall))) {
                fa = contact.getFixtureB();
                fb = contact.getFixtureA();
            }

             if (fa.getUserData() instanceof ShadowBall) {
                ShadowBall shadowBall = (ShadowBall) fa.getUserData();
                if(fb.getFilterData().categoryBits== MyGdxGame.ENEMY_BIT) {
                    if(fb.getUserData()instanceof Warrior) {
                        Warrior warrior = (Warrior) fb.getUserData();
                        warrior.hitByHero();
                        shadowBall.setColided(true);
                    }
                    else if(fb.getUserData()instanceof Archer){
                        Archer archer = (Archer) fb.getUserData();
                        archer.hitByHero();
                        shadowBall.setColided(true);
                    }
                    else if(fb.getUserData()instanceof ShadowBall){
                        ((ShadowBall) fb.getUserData()).setColided(true);
                    }else if(fb.getUserData()instanceof MiniBoss){
                        MiniBoss miniBoss= (MiniBoss) fb.getUserData();
                        miniBoss.hitByHero();
                        shadowBall.setColided(true);
                    }



                }else if(fb.getFilterData().categoryBits==MyGdxGame.DEFAULT_BIT){
                   shadowBall.setColided(true);
                }


            }
        }else if(fa.getUserData()instanceof Arrow||fb.getUserData()instanceof Arrow){

            if(!(fa.getUserData()instanceof Arrow)){
               fa= contact.getFixtureB();
               fb=contact.getFixtureA();
            }if(fb.getUserData()instanceof Stairs||fb.getUserData()instanceof Ground||fb.getUserData()instanceof Castle||fb.getUserData()instanceof MiniBoss) {
                Arrow arrow = (Arrow) fa.getUserData();
                arrow.setColided(true);
            }
        }



        if ((fa.getUserData()instanceof ShadowBall||fa.getUserData() instanceof Myhero )&& fb.getUserData() instanceof Warrior) {
            Warrior warrior= (Warrior) fb.getUserData();
switch (fb.getFilterData().categoryBits){
    case 3:
        warrior.right=true;
        warrior.left=false;
        break;
    case 5:
        warrior.left=true;
        warrior.right=false;
        break;

            case MyGdxGame.ENEMY_BIT:
                if(fa.getUserData()instanceof Myhero) {
                    warrior.setAtacking(true);
                }
        break;
}

            }
        else  if (fa.getUserData() instanceof Myhero && fb.getUserData() instanceof Archer) {
            Archer archer= (Archer) fb.getUserData();
            int cat=fb.getFilterData().categoryBits;
            switch (cat) {
                case 3:
archer.isaimingRight=true;
archer.sawEnemy=true;
                    break;
                case 5:
archer.isaimingRight=false;
archer.sawEnemy=true;
                    break;

                case MyGdxGame.ENEMY_BIT:

                    break;
            }
        }else if(fa.getUserData()instanceof MiniBoss||fb.getUserData()instanceof MiniBoss){
            if (!(fa.getUserData() instanceof MiniBoss)) {
                fa = contact.getFixtureB();
                fb = contact.getFixtureA();
            }

            if(fb.getUserData()instanceof Ground){
                MiniBoss min = (MiniBoss) fa.getUserData();
                System.out.println("hitground");
                min.setHitground(!min.isHitground());
            }
        }else if(fa.getUserData()instanceof GreenBall||fb.getUserData()instanceof GreenBall){
            if(!(fa.getUserData()instanceof GreenBall)){
                fa=contact.getFixtureB();
                fb=contact.getFixtureA();
            }
            if(fa.getUserData()instanceof GreenBall){
                GreenBall greenBall= (GreenBall) fa.getUserData();
                if(fb.getUserData()instanceof Ground){
                    greenBall.setColided(true);
                }else if(fb.getUserData()instanceof Myhero){
                    greenBall.setColided(true);
                    Myhero.damage(1);
                }else if(fb.getUserData()instanceof Castle||fb.getUserData()instanceof Stairs){
                    greenBall.setColided(true);
                }
            }
        }else if(fa.getUserData()instanceof MovingObject||fb.getUserData()instanceof MovingObject){
            if(!(fa.getUserData()instanceof MovingObject)){
                fa=contact.getFixtureB();
                fb=contact.getFixtureA();
            }
            if(fa.getUserData()instanceof MovingObject){
               MovingObject object= (MovingObject) fa.getUserData();
                if(fb.getUserData()instanceof Stairs){
                    object.setHitSensor(!object.gethitSensor());
                }
            }
        }else if(fa.getUserData()instanceof Warrior||fb.getUserData()instanceof Warrior){
            if(!(fa.getUserData()instanceof Warrior)){
                fa=contact.getFixtureB();
                fb=contact.getFixtureA();
            }
            Warrior warrior= (Warrior) fa.getUserData();
            if(fb.getUserData()instanceof Lava){
                warrior.health=0;
            }
        }


    }

    private boolean isAsolid( Fixture fb) {
        if(fb.getUserData() instanceof Ground) {
            return true;
        }else {
            return false;
        }
    }
    @Override
    public void endContact(Contact contact) {
        Fixture fa=contact.getFixtureA();
        Fixture fb=contact.getFixtureB();
        if(fa==null||fb==null){
            return;}
        if(fb.getUserData()==null||fa.getUserData()==null){
            return;}
        if(fb.getUserData()==null||fa.getUserData()==null){
            return;}
        if(fa.getUserData() instanceof Myhero ||fb.getUserData() instanceof Myhero) {
            if (!(fa.getUserData() instanceof Myhero)) {
                fa = contact.getFixtureB();
                fb = contact.getFixtureA();
            }
            if( fa.getUserData()instanceof Myhero){
                Myhero myhero= (Myhero) fa.getUserData();

                if(fb.getUserData()instanceof Stairs){
                myhero.onstairs=false;
            }else if(fb.getUserData()instanceof MiniBoss){

                }else if(fb.getUserData()instanceof Ladder){

                    myhero.setOnLadder(false);
                }else if(fb.getUserData()instanceof Chest){
                    System.out.println("offchest");

                    myhero.setIsonChest(false);

                }else if(fb.getUserData()instanceof Save){
                    myhero.onsavepoint=false;
                }

            }

        }

        if(isAsolid(fb)&&fa.getUserData() instanceof Myhero){
            Myhero myhero= (Myhero) fa.getUserData();
            myhero.setcanjump(false);
        }
        if ((fa.getUserData()instanceof ShadowBall||fa.getUserData() instanceof Myhero )&& fb.getUserData() instanceof Warrior) {
            Warrior warrior= (Warrior) fb.getUserData();

            switch (fb.getFilterData().categoryBits){

                case MyGdxGame.ENEMY_BIT:
                    if(fa.getUserData()instanceof Myhero) {
                        warrior.setAtacking(false);
                    }
                    }


        }
        if (fa.getUserData() instanceof Myhero && fb.getUserData() instanceof Archer) {
            Archer archer= (Archer) fb.getUserData();
            int cat=fb.getFilterData().categoryBits;
            switch (cat) {
                case 3:
                    archer.sawEnemy=false;
                    break;
                case 5:
                    archer.sawEnemy=false;
                    break;

                case MyGdxGame.ENEMY_BIT:

                    break;
            }
        }


}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
