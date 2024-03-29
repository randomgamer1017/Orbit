package Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.stackoverflowtrio.orbit.Orbit;

import Screens.PlayScreen;
import Sprites.Mario;

public class Powerup extends Item{
	public Powerup(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
		velocity = new Vector2(0.7f, 0);
	}

	@Override
	public void defineItem() {
		// TODO Auto-generated method stub
		BodyDef bdef = new BodyDef();
		bdef.position.set(getX(), getY());
		bdef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bdef);
		
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6 / Orbit.PPM);
		fdef.filter.categoryBits = Orbit.ITEM_BIT;
		fdef.filter.maskBits = Orbit.MARIO_BIT | Orbit.OBJECT_BIT | Orbit.DEFAULT_BIT | Orbit.COIN_BIT | Orbit.BRICK_BIT;
		
		fdef.shape = shape;
		body.createFixture(fdef).setUserData(this);
	}

	@Override
	public void use(Mario mario) {
		// TODO Auto-generated method stub
		destroy();
		mario.grow();
	}
	
	public void update(float dt) {
		super.update(dt);
		setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
		body.setLinearVelocity(velocity);
		velocity.y = body.getLinearVelocity().y;
		body.setLinearVelocity(velocity);
	}
}
