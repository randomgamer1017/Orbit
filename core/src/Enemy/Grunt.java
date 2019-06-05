package Enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.stackoverflowtrio.orbit.Orbit;

import Screens.PlayScreen;
import Sprites.Mario;

public class Grunt extends Enemy{

	private float stateTime;
	private Animation walkAnimation;
	private Array<TextureRegion> frames;
	private boolean setToDestroy;
	private boolean destroyed;
	
	public Grunt(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		frames = new Array<TextureRegion>();
		for (int i = 0; i < 2; i++)
			frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
		walkAnimation = new Animation(0.4f, frames);
		stateTime = 0;
		setBounds(getX(), getY(), 16 / Orbit.PPM, 16 / Orbit.PPM);
		setToDestroy = false;
		destroyed = false;
		// TODO Auto-generated constructor stub
	}

	public void update(float dt) {
		stateTime += dt;
		if (setToDestroy && !destroyed) {
			world.destroyBody(b2body);
			destroyed = true;
			setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
			stateTime = 0;
		}
		else if (!destroyed) {
			b2body.setLinearVelocity(velocity);
			setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
			setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
		}
	}
	
	@Override
	protected void defineEnemy() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(getX(), getY());
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
		
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6 / Orbit.PPM);
		fdef.filter.categoryBits = Orbit.ENEMY_BIT;
		fdef.filter.maskBits = Orbit.DEFAULT_BIT | Orbit.COIN_BIT | Orbit.BRICK_BIT | Orbit.ENEMY_BIT | Orbit.OBJECT_BIT | Orbit.MARIO_BIT;
		
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);
		
		//Create the Head Here
		PolygonShape head = new PolygonShape();
		Vector2[] vertice = new Vector2[4];
		vertice[0] = new Vector2(-5, 8).scl(1 / Orbit.PPM);
		vertice[1] = new Vector2(5, 8).scl(1 / Orbit.PPM);
		vertice[2] = new Vector2(-3, 3).scl(1 / Orbit.PPM);
		vertice[3] = new Vector2(3, 3).scl(1 / Orbit.PPM);
		head.set(vertice);
		
		fdef.shape = head;
		fdef.restitution = 0.5f;
		fdef.filter.categoryBits = Orbit.ENEMY_HEAD_BIT;
		b2body.createFixture(fdef).setUserData(this);
	}
	
	public void draw(Batch batch) {
		if (!destroyed || stateTime < 1)
			super.draw(batch);
	}
	
	public void hitOnHead(Mario mario) {
		setToDestroy = true;
	}
	
}
