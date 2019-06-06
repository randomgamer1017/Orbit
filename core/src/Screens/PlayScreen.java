package Screens;

import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.msg.PriorityQueue;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.stackoverflowtrio.orbit.Orbit;

import Enemy.Enemy;
import Enemy.Grunt;
import Items.Item;
import Items.ItemDef;
import Items.Powerup;
import Scenes.HUD;
import Sprites.Mario;
import Tools.B2WorldCreator;
import Tools.WorldContactListener;

public class PlayScreen implements Screen{
	private Orbit game;
	private TextureAtlas atlas;
	
	private OrthographicCamera gamecam;
	private Viewport gamePort;
	private HUD hud;
	
	private TmxMapLoader maploader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	
	//Box2d Variables
	private World world;
	private Box2DDebugRenderer b2dr;
	private B2WorldCreator creator;
	
	private Mario player;
	private Powerup powerup;
	
	private Music music;
	
	private Array<Item> items;
	private LinkedBlockingQueue<ItemDef> itemsToSpawn;
	
	public PlayScreen(Orbit game) {
		atlas = new TextureAtlas("Mario_and_Enemies.pack");
		
		this.game = game;
		
		gamecam = new OrthographicCamera();
		
		gamePort = new FitViewport(Orbit.V_WIDTH / Orbit.PPM, Orbit.V_HEIGHT / Orbit.PPM, gamecam);
		
		hud = new HUD(game.batch);
		
		maploader = new TmxMapLoader();
		map = maploader.load("Maps/HomeBaseTest.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / Orbit.PPM);
		
		gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		
		world = new World(new Vector2(0, -10), true);
		b2dr = new Box2DDebugRenderer();
		
		creator = new B2WorldCreator(this);
	
		player = new Mario(this);
		
		world.setContactListener(new WorldContactListener());
		
		music = Orbit.manager.get("Songs/AmbientSong.mp3", Music.class);
		music.setLooping(true);
		music.setVolume((float) 0.1);
		music.play();
		
		items = new Array<Item>();
		itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
	}
	
	public void spawnItems(ItemDef idef) {
		itemsToSpawn.add(idef);
	}
	
	public void handleSpawningItems() {
		if (!itemsToSpawn.isEmpty()) {
			ItemDef idef = itemsToSpawn.poll();
			if (idef.type == Powerup.class) {
				items.add(new Powerup(this, idef.position.x, idef.position.y));
			}
		}
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	public void handleInput(float dt) {
		if (player.currentState != Mario.State.DEAD) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && !Mario.getHasJumped()) {
				player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
				Mario.setHasJumped(true);
			}
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) 
				player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
				player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				game.changeScreen(0);
				music.stop();
			}
		}	
	}	
	
	public void update(float dt) {
		handleInput(dt);
		handleSpawningItems();
		
		world.step(1/60f, 6, 2);
		
		player.update(dt);
/*		for (Enemy enemy : creator.getGrunts()) {
			enemy.update(dt);
			if (enemy.getX() < player.getX() + 224 / Orbit.PPM)
				enemy.b2body.setActive(true);
		}
		
		for(Item item : items)
			item.update(dt);*/
		
		hud.update(dt);
		if (player.currentState != Mario.State.DEAD) {
			gamecam.position.x = player.b2body.getPosition().x;
		}
		
		gamecam.update();
		renderer.setView(gamecam);
	}
	
	public TextureAtlas getAtlas() {
		// TODO Auto-generated method stub
		return atlas;
	}
	
	@Override
	public void render(float delta) {
		update(delta);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		renderer.render();
		
		b2dr.render(world, gamecam.combined);
		
		game.batch.setProjectionMatrix(gamecam.combined);
		game.batch.begin();
		player.draw(game.batch);
/*		for (Enemy enemy : creator.getGrunts())
			enemy.draw(game.batch);
		for (Item item : items)
			item.draw(game.batch);*/
		game.batch.end();
		
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();

		if (gameOver()) {
			game.setScreen(new GameOverScreen(game));
			dispose();
		}
	}

	public boolean gameOver() {
		if (player.currentState == Mario.State.DEAD && player.getStateTimer() > 3)
			return true;
		else
			return false;
	}
	
	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
		// TODO Auto-generated method stub
		
	}

	public TiledMap getMap() {
		return map;
	}
	
	public World getWorld() {
		return world;
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		world.dispose();
		b2dr.dispose();
		hud.dispose();
	}
}
