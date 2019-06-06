package Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.stackoverflowtrio.orbit.Orbit;

import Enemy.Enemy;
import Items.Item;
import Sprites.InteractiveTileObject;
import Sprites.Mario;

public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		
		switch (cDef) {
			case Orbit.MARIO_HEAD_BIT | Orbit.BRICK_BIT:
			case Orbit.MARIO_HEAD_BIT | Orbit.COIN_BIT:
				if (fixA.getFilterData().categoryBits == Orbit.MARIO_HEAD_BIT)
					((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
				else
					((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
				break;
			case Orbit.ENEMY_HEAD_BIT | Orbit.MARIO_BIT:
				if (fixA.getFilterData().categoryBits == Orbit.ENEMY_HEAD_BIT)
					((Enemy)fixA.getUserData()).hitOnHead((Mario) fixB.getUserData());
				else if (fixB.getFilterData().categoryBits == Orbit.ENEMY_HEAD_BIT)
					((Enemy)fixB.getUserData()).hitOnHead((Mario) fixA.getUserData());
				break;
			case Orbit.ENEMY_BIT | Orbit.OBJECT_BIT:
				if (fixA.getFilterData().categoryBits == Orbit.ENEMY_BIT)
					((Enemy)fixA.getUserData()).reverseVelocity(true, false);
				else
					((Enemy)fixB.getUserData()).reverseVelocity(true, false);
				break;
			case Orbit.MARIO_BIT | Orbit.ENEMY_BIT:
				if (fixA.getFilterData().categoryBits == Orbit.MARIO_BIT)
					((Mario) fixA.getUserData()).hit();
				else
					((Mario) fixB.getUserData()).hit();
				break;
			case Orbit.ENEMY_BIT | Orbit.ENEMY_BIT:
				((Enemy)fixA.getUserData()).reverseVelocity(true, false);
				((Enemy)fixB.getUserData()).reverseVelocity(true, false);
				break;
			case Orbit.ITEM_BIT | Orbit.OBJECT_BIT:
				if (fixA.getFilterData().categoryBits == Orbit.ENEMY_BIT)
					((Item)fixA.getUserData()).reverseVelocity(true, false);
				else
					((Item)fixB.getUserData()).reverseVelocity(true, false);
				break;
			case Orbit.ITEM_BIT | Orbit.MARIO_BIT:
				if (fixA.getFilterData().categoryBits == Orbit.ENEMY_BIT)
					((Item)fixA.getUserData()).use((Mario) fixB.getUserData());
				else
					((Item)fixB.getUserData()).use((Mario) fixA.getUserData());
				break;
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
