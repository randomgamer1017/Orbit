package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.stackoverflowtrio.orbit.AppPreferences;
import com.stackoverflowtrio.orbit.Orbit;

public class PauseScreen implements Screen {
	private Viewport viewport;
	private Stage stage;
	private Texture texture;
	private Orbit games;
	private SpriteBatch batch;

	private AppPreferences preferences = new AppPreferences();
	public PauseScreen(Orbit game) { 
		 this.games = game;
		 stage = new Stage(new ScreenViewport());
		 Gdx.input.setInputProcessor(stage);
		 batch = new SpriteBatch();
		 texture = new Texture(Gdx.files.internal("preferences.png"));
		 
		 Table table = new Table();
		 table.center();
		 table.setFillParent(true);
		 table.setDebug(false);
		 Skin skin = new Skin(Gdx.files.internal("settingsSkin/glassy-ui.json"));
		 TextButton gameReturner = new TextButton("Return to Game", skin);
		 TextButton menuReturner = new TextButton("Exit to Menu", skin);
		 

		 
		 stage.addActor(table);
		 table.add(gameReturner);
		 table.row().pad(10,0,10,0);
		 table.add(menuReturner);
		 gameReturner.addListener(new ChangeListener() {
			 @Override
			 public void changed(ChangeEvent event, Actor actor) {
				games.changeScreen(4);
			 }
		 });
		 menuReturner.addListener(new ChangeListener() {
			 @Override
			 public void changed(ChangeEvent event, Actor actor) {
				games.changeScreen(0);
			 }
		 });
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void render(float delta) {
		Gdx.input.setInputProcessor(stage);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(texture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		stage.draw();
		
	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}
}