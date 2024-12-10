package com.susanafigueroa.contactplayermagicalobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.susanafigueroa.magicalobjects.chandelier.ChandelierManage;
import com.susanafigueroa.magicalobjects.chest.Chest;
import com.susanafigueroa.magicalobjects.chest.ChestManage;
import com.susanafigueroa.player.Player;
import com.susanafigueroa.timer.Timer;

public class ContactPlayerMagicalObject implements ContactListener {
    private Timer timer;
    private ChandelierManage chandelierManage;
    private ChestManage chestManage;

    public ContactPlayerMagicalObject(Timer timer, ChandelierManage chandelierManage, ChestManage chestManage) {
        this.timer = timer;
        this.chandelierManage = chandelierManage;
        this.chestManage = chestManage;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if ((fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof ChandelierManage) ||
            (fixtureB.getUserData() instanceof Player && fixtureA.getUserData() instanceof ChandelierManage)
        ) {
            Gdx.app.log("CONTACT PLAYER WITH CHANDELIER", fixtureA.getUserData().toString() + " | " + fixtureB.getUserData().toString());

            timer.plusTimer();
        } else if ((fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof ChestManage) ||
                    (fixtureB.getUserData() instanceof Player && fixtureA.getUserData() instanceof ChestManage)
        ) {
            Gdx.app.log("CONTACT PLAYER WITH CHEST", fixtureA.getUserData().toString() + " | " + fixtureB.getUserData().toString());

            timer.plusTimer();
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
