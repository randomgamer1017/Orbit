package Screens;
import com.badlogic.gdx.Screen;

import java.util.EventListener;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.stackoverflowtrio.orbit.AppPreferences;
import com.stackoverflowtrio.orbit.Orbit;

public class SettingsScreen implements Screen {
	private Viewport viewport;
	private Stage stage;
	private Texture texture;
	private Orbit games;
	private SpriteBatch batch;
	private Label titleLabel; 
	private Label volumeMusicLabel;
	private Label volumeSoundLabel;
	private Label musicOnOffLabel;
	private Label soundOnOffLabel;
	private AppPreferences preferences = new AppPreferences();
	public SettingsScreen(Orbit game) { 
		 this.games = game;
		 stage = new Stage(new ScreenViewport());
		 Gdx.input.setInputProcessor(stage);
		 batch = new SpriteBatch();
		 texture = new Texture(Gdx.files.internal("preferences.png"));
		 
		 Table table = new Table();
		 table.center();
		 table.setFillParent(true);
		 table.setDebug(false);
		 
		 

		 
		 stage.addActor(table);
		 Skin skin = new Skin(Gdx.files.internal("settingsSkin/glassy-ui.json"));
		 TextButton returner = new TextButton("Return to Menu", skin);
		 
		 final Slider volumeMusicSlider = new Slider( 0f, 1f, 0.1f,false, skin );
	        volumeMusicSlider.setValue( games.getPreferences().getMusicVolume() );
	        final Slider volumeSoundSlider = new Slider( 0f, 1f, 0.1f,false, skin );
	        volumeSoundSlider.setValue( games.getPreferences().getSoundVolume() );
	        
	        final CheckBox musicCheckbox = new CheckBox(null, skin);
	        musicCheckbox.setChecked( games.getPreferences().isMusicEnabled() );
	        final CheckBox soundCheckbox = new CheckBox(null, skin);
	        soundCheckbox.setChecked( games.getPreferences().isSoundEffectsEnabled() );
	     titleLabel = new Label( "Preferences", skin ); 
		 volumeMusicLabel = new Label( "Music Volume", skin );
		 volumeSoundLabel = new Label( "Sound Volume", skin );
		 musicOnOffLabel = new Label( "Enable Music", skin );
		 soundOnOffLabel = new Label( "Enable Sound", skin );
		 table.add(titleLabel);
		 table.row();
		 table.add(volumeMusicLabel);
		 table.add(volumeMusicSlider);
		 table.row();
		 table.add(musicOnOffLabel);
		 table.add(musicCheckbox);
		 table.row();
		 table.add(volumeSoundLabel);
		 table.add(volumeSoundSlider);
		 table.row();
		 table.add(soundOnOffLabel);
		 table.add(soundCheckbox);
		 table.row();
		 table.add(returner);
		 musicCheckbox.addListener( new ChangeListener() {
	           	@Override
	        	public void changed(ChangeEvent event, Actor actor) {
	               	boolean enabled = musicCheckbox.isChecked();
	               	games.getPreferences().setMusicEnabled( enabled );
	        	}
	        });
		 volumeMusicSlider.addListener( new ChangeListener() {
	  		@Override
			public void changed(ChangeEvent event, Actor actor) {
	  			games.getPreferences().setMusicVolume( volumeMusicSlider.getValue() );
		}
	});
		 soundCheckbox.addListener( new ChangeListener() {
	           	@Override
	        	public void changed(ChangeEvent event, Actor actor) {
	               	boolean enabled = soundCheckbox.isChecked();
	               	games.getPreferences().setSoundEffectsEnabled( enabled );
	        	}
	        });
		 volumeSoundSlider.addListener( new ChangeListener() {
	  		@Override
			public void changed(ChangeEvent event, Actor actor) {
	  			games.getPreferences().setSoundVolume( volumeSoundSlider.getValue() );
		}
	});
		 returner.addListener(new ChangeListener() {
			 @Override
			 public void changed(ChangeEvent event, Actor actor) {
				games.changeScreen(0);
			 }
		 });
		 
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		
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
		stage.dispose();
		
	}
}
