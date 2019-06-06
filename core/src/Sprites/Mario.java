package Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.stackoverflowtrio.orbit.Orbit;

import Items.Powerup;
import Screens.PlayScreen;

public class Mario extends Sprite {
	public enum State {FALLING, JUMPING, STANDING, RUNNING, AIR_RUNNING, GROWING, DEAD};
	public State currentState;
	public State previousState;
	
	public World world;
	public Body b2body;
	
	private TextureRegion marioStand;
	private Animation marioRun;
	private TextureRegion marioJump;
	private TextureRegion marioDead;
	private TextureRegion bigMarioStand;
	private TextureRegion bigMarioJump;
	private Animation bigMarioRun;
	private Animation growMario;
	
	private float stateTimer;
	private boolean runningRight;
	public boolean marioIsBig;
	private boolean runGrowAnimation;
	private boolean timeToDefineBigMario;
	private boolean timeToRedefineMario;
	private boolean marioIsDead;
	public static boolean hasJumped;
	
	public Mario(PlayScreen screen) {
		this.world = screen.getWorld();
		currentState = State.STANDING;
		previousState = State.STANDING;
		stateTimer = 0;
		runningRight = true;
		hasJumped = false;
		
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i = 1; i < 4; i++) {
			frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
		}
		marioRun = new Animation(0.1f, frames);
		
		frames.clear();
		
		for (int i = 1; i < 4; i++) {
			frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
		}
		bigMarioRun = new Animation(0.1f, frames);
		
		frames.clear();
		
		frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
		frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
		growMario = new Animation(0.2f, frames);
		
		marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
		bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);
		
		marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);
		
		marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
		bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);
		
		defineMario();
		setBounds(0, 0, 16 / Orbit.PPM, 16 / Orbit.PPM);
		setRegion(marioStand);
	}
	
	public void update(float dt) {
		if (marioIsBig)
			setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - 6 / Orbit.PPM);
		else
			setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
		setRegion(getFrame(dt));
		if (timeToDefineBigMario)
			defineBigMario();
		if (timeToRedefineMario)
			redefineMario();
	}
	
	public TextureRegion getFrame(float dt) {
		currentState = getState();
		
		TextureRegion region;
		switch(currentState) {
			case DEAD:
				region = marioDead;
				break;
			case GROWING:
				region = (TextureRegion) growMario.getKeyFrame(stateTimer);
				if (growMario.isAnimationFinished(stateTimer))
					runGrowAnimation = false;
				break;
			case JUMPING:
				hasJumped = true;
				region = marioIsBig ? bigMarioJump : marioJump;
				break;
			case AIR_RUNNING:
				region =  marioIsBig ? (TextureRegion) bigMarioRun.getKeyFrame(stateTimer, true) : (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
				break;
			case RUNNING:
				hasJumped = false;
				region =  marioIsBig ? (TextureRegion) bigMarioRun.getKeyFrame(stateTimer, true) : (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
				break;
			case FALLING:
				region = marioIsBig ? bigMarioStand : marioStand;
				break;
			case STANDING:
			default:
				hasJumped = false;
				region = marioIsBig ? bigMarioStand : marioStand;
				break;
		}
		
		if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
			region.flip(true, false);
			runningRight = false;
		}
		else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
			region.flip(true, false);
			runningRight = true;
		}
		
		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
		return region;
	}
	
	public State getState() {
		if (marioIsDead)
			return State.DEAD;
		else if (runGrowAnimation)
			return State.GROWING;
		else if (b2body.getLinearVelocity().y > 0 || b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)
			return State.JUMPING;
		else if (b2body.getLinearVelocity().y < 0)
			return State.FALLING;
		else if ((b2body.getLinearVelocity().x != 0) && (b2body.getLinearVelocity().y != 0))
			return State.AIR_RUNNING;
		else if ((b2body.getLinearVelocity().x != 0) && (b2body.getLinearVelocity().y == 0))
			return State.RUNNING;
		else 
			return State.STANDING;
	}
	
	public void grow() {
		if (marioIsBig)
			timeToDefineBigMario = false;
		else {
			runGrowAnimation = true;
			marioIsBig = true;
			timeToDefineBigMario = true;
			setBounds(getX(), getY(), getWidth(), getHeight() * 2);
		}
	}
	
	public void hit() {
		if (marioIsBig) {
			marioIsBig = false;
			timeToRedefineMario = true;
			setBounds(getX(), getY(), getWidth(), getHeight() / 2);
		}
		else {
			Orbit.manager.get("Songs/AmbientSong.mp3", Music.class).stop();
			marioIsDead = true;
			Filter filter = new Filter();
			filter.maskBits = Orbit.NOTHING_BIT;
			for(Fixture fixture : b2body.getFixtureList())
				fixture.setFilterData(filter);
			b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
		}
	}
	
	public boolean isDead() {
		return marioIsDead;
	}
	
	public float getStateTimer() {
		return stateTimer;
	}
	
	public void redefineMario() {
		Vector2 position = b2body.getPosition();
		world.destroyBody(b2body);
		
		BodyDef bdef = new BodyDef();
		bdef.position.set(position);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
		
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6 / Orbit.PPM);
		fdef.filter.categoryBits = Orbit.MARIO_BIT;
		fdef.filter.maskBits = Orbit.DEFAULT_BIT | Orbit.COIN_BIT | Orbit.BRICK_BIT | Orbit.ENEMY_BIT | Orbit.OBJECT_BIT | Orbit.ENEMY_HEAD_BIT | Orbit.ITEM_BIT;
		
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);
		
		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2 / Orbit.PPM, 6  / Orbit.PPM), new Vector2(2 / Orbit.PPM, 6 / Orbit.PPM));
		fdef.filter.categoryBits = Orbit.MARIO_HEAD_BIT;
		fdef.shape = head;
		fdef.isSensor = true;
		
		b2body.createFixture(fdef).setUserData(this);
		
		timeToRedefineMario = false;
	}
	
	public void defineBigMario() {
		Vector2 currentPosition = b2body.getPosition();
		world.destroyBody(b2body);
		
		BodyDef bdef = new BodyDef();
		bdef.position.set(currentPosition.add(0, 10 / Orbit.PPM));
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
		
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6 / Orbit.PPM);
		fdef.filter.categoryBits = Orbit.MARIO_BIT;
		fdef.filter.maskBits = Orbit.DEFAULT_BIT | Orbit.COIN_BIT | Orbit.BRICK_BIT | Orbit.ENEMY_BIT | Orbit.OBJECT_BIT | Orbit.ENEMY_HEAD_BIT | Orbit.ITEM_BIT;
		
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);
		shape.setPosition(new Vector2(0, -14 / Orbit.PPM));
		b2body.createFixture(fdef).setUserData(this);
		
		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2 / Orbit.PPM, 6  / Orbit.PPM), new Vector2(2 / Orbit.PPM, 6 / Orbit.PPM));
		fdef.filter.categoryBits = Orbit.MARIO_HEAD_BIT;
		fdef.shape = head;
		fdef.isSensor = true;
		
		b2body.createFixture(fdef).setUserData(this);
		timeToDefineBigMario = false;
	}
	
	public void defineMario() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(400 / Orbit.PPM, 400 / Orbit.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
		
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6 / Orbit.PPM);
		fdef.filter.categoryBits = Orbit.MARIO_BIT;
		fdef.filter.maskBits = Orbit.DEFAULT_BIT | Orbit.COIN_BIT | Orbit.BRICK_BIT | Orbit.ENEMY_BIT | Orbit.OBJECT_BIT | Orbit.ENEMY_HEAD_BIT | Orbit.ITEM_BIT;
		
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);
		
		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2 / Orbit.PPM, 6  / Orbit.PPM), new Vector2(2 / Orbit.PPM, 6 / Orbit.PPM));
		fdef.filter.categoryBits = Orbit.MARIO_HEAD_BIT;
		fdef.shape = head;
		fdef.isSensor = true;
		
		b2body.createFixture(fdef).setUserData(this);
	}
	
	public static void setHasJumped(boolean jumped) {
		hasJumped = jumped;
	}
	
	public static boolean getHasJumped() {
		return hasJumped;
	}
}
