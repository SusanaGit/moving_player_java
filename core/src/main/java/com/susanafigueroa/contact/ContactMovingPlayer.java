package com.susanafigueroa.contact;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.susanafigueroa.bullet.Bullet;
import com.susanafigueroa.magicalobjects.chandelier.Chandelier;
import com.susanafigueroa.magicalobjects.chandelier.ChandelierManage;
import com.susanafigueroa.magicalobjects.chest.Chest;
import com.susanafigueroa.magicalobjects.chest.ChestManage;
import com.susanafigueroa.player.Player;
import com.susanafigueroa.timer.Timer;
import com.susanafigueroa.villains.Villain;
import com.susanafigueroa.villains.VillainManage;

public class ContactMovingPlayer implements ContactListener {

    private Timer timer;
    private ChandelierManage chandelierManage;
    private ChestManage chestManage;
    private VillainManage villainManage;

    public ContactMovingPlayer(Timer timer, ChandelierManage chandelierManage, ChestManage chestManage, VillainManage villainManage) {
        this.timer = timer;
        this.chandelierManage = chandelierManage;
        this.chestManage = chestManage;
        this.villainManage = villainManage;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if ((fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Villain) ||
            (fixtureB.getUserData() instanceof Player && fixtureA.getUserData() instanceof Villain)) {
            Gdx.app.log("CONTACT VILLAIN WITH PLAYER", fixtureA.getUserData().toString() + fixtureB.getUserData().toString());
            timer.reduceTimer();

        } else if ((fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Chandelier) ||
            (fixtureB.getUserData() instanceof Player && fixtureA.getUserData() instanceof Chandelier)
        ) {
            Gdx.app.log("CONTACT PLAYER WITH CHANDELIER", fixtureA.getUserData().toString() + " | " + fixtureB.getUserData().toString());

            if (fixtureA.getUserData() instanceof Chandelier) {
                chandelierManage.removeChandelier((Chandelier) fixtureA.getUserData());
            } else {
                chandelierManage.removeChandelier((Chandelier) fixtureB.getUserData());
            }

            timer.plusTimer();

        } else if ((fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof Chest) ||
            (fixtureB.getUserData() instanceof Player && fixtureA.getUserData() instanceof Chest)
        ) {
            Gdx.app.log("CONTACT PLAYER WITH CHEST", fixtureA.getUserData().toString() + " | " + fixtureB.getUserData().toString());

            if (fixtureA.getUserData() instanceof Chest) {
                chestManage.removeChest((Chest) fixtureA.getUserData());
            } else {
                chestManage.removeChest((Chest) fixtureB.getUserData());
            }

            timer.plusTimer();

        } else if ((fixtureA.getUserData() instanceof Bullet && fixtureB.getUserData() instanceof Villain) ||
            (fixtureB.getUserData() instanceof Bullet && fixtureA.getUserData() instanceof Villain)
        ) {
            Gdx.app.log("CONTACT BULLET WITH VILLAIN ", fixtureA.getUserData().toString() + " | " + fixtureB.getUserData().toString());
            if (fixtureA.getUserData() instanceof Villain) {
                villainManage.removeVillain((Villain) fixtureA.getUserData());
            } else {
                villainManage.removeVillain((Villain) fixtureB.getUserData());
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
