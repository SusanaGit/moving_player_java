package com.susanafigueroa.contactplayervillain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.susanafigueroa.player.Player;
import com.susanafigueroa.timer.Timer;
import com.susanafigueroa.villains.VillainManage;

public class ContactPlayerVillain implements ContactListener {

    private Timer timer;

    public ContactPlayerVillain(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if ((fixtureA.getUserData() instanceof Player && fixtureB.getUserData() instanceof VillainManage) ||
            (fixtureB.getUserData() instanceof Player && fixtureA.getUserData() instanceof VillainManage)) {
            Gdx.app.log("CONTACT VILLAIN WITH PLAYER", fixtureA.getUserData().toString() + fixtureB.getUserData().toString());
            timer.reduceTimer();
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
