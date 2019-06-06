package Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.stackoverflowtrio.orbit.Orbit;

import Enemy.Grunt;
import Screens.PlayScreen;
import Sprites.Brick;
import Sprites.Coin;

public class B2WorldCreator {
	private Array<Grunt> grunts;
	
	public B2WorldCreator (PlayScreen screen) {
		World world = screen.getWorld();
		TiledMap map = screen.getMap();
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;
		
		//Ground (test)
		for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / Orbit.PPM, (rect.getY() + rect.getHeight() / 2) / Orbit.PPM);
			
			body = world.createBody(bdef);
			
			shape.setAsBox(rect.getWidth() / 2 / Orbit.PPM, rect.getHeight() / 2 / Orbit.PPM);
			fdef.shape = shape;
			body.createFixture(fdef);
		}
		
		//Pipes (test)
		for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / Orbit.PPM, (rect.getY() + rect.getHeight() / 2) / Orbit.PPM);
			
			body = world.createBody(bdef);
			
			shape.setAsBox(rect.getWidth() / 2 / Orbit.PPM, rect.getHeight() / 2 / Orbit.PPM);
			fdef.shape = shape;
			fdef.filter.categoryBits = Orbit.OBJECT_BIT;
			body.createFixture(fdef);
		}
		
/*		//Coins (test)
		for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
			
			new Coin(screen, object);
		}
		
		//Bricks (test)
		for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
			
			new Brick(screen, object);
		}
		
		grunts = new Array<Grunt>();
		for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			grunts.add(new Grunt(screen, rect.getX() / Orbit.PPM, rect.getY() / Orbit.PPM));
		}*/
	}
	
	public Array<Grunt> getGrunts() {
		return grunts;
	}
}
