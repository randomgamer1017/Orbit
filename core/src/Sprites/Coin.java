package Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.stackoverflowtrio.orbit.Orbit;

import Items.ItemDef;
import Items.Powerup;
import Scenes.HUD;
import Screens.PlayScreen;

public class Coin extends InteractiveTileObject {
	private static TiledMapTileSet tileSet;
	private final int BLANK_COIN = 28;
	public Coin(PlayScreen screen, MapObject object) {
		super(screen, object);
		tileSet = map.getTileSets().getTileSet("tileset_gutter");
		fixture.setUserData(this);
		setCategoryFilter(Orbit.COIN_BIT);
	}

	@Override
	public void onHeadHit(Mario mario) {
		Gdx.app.log("Coin", "Collision");
		if(getCell().getTile().getId() == BLANK_COIN)
			Orbit.manager.get("Sound Effects/bump1.wav", Sound.class).play();
		else {
			if (object.getProperties().containsKey("mushroom")) {
				screen.spawnItems(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / Orbit.PPM), Powerup.class));
			}
			Orbit.manager.get("Sound Effects/metal.wav", Sound.class).play();
		}
		getCell().setTile(tileSet.getTile(BLANK_COIN));
		HUD.addScore(100);
	}
}
