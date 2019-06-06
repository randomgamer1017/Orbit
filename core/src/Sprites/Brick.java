package Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.stackoverflowtrio.orbit.Orbit;
import Scenes.HUD;
import Screens.PlayScreen;

public class Brick extends InteractiveTileObject{
	public Brick(PlayScreen screen, MapObject object) {
		super(screen, object);
		fixture.setUserData(this);
		setCategoryFilter(Orbit.BRICK_BIT);
	}

	@Override
	public void onHeadHit(Mario mario) {
		if (mario.marioIsBig) {
			Gdx.app.log("Brick", "Collision");
			setCategoryFilter(Orbit.DESTROYED_BIT);
			getCell().setTile(null);
			HUD.addScore(200);
			Orbit.manager.get("Sound Effects/break.wav", Sound.class).play();
		}
		else
			Orbit.manager.get("Sound Effects/bump1.wav", Sound.class).play();
	}
}
