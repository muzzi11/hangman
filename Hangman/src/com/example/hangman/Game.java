package com.example.hangman;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

public class Game extends BaseGameActivity
{
	final int cameraWidth = 320;
	final int cameraHeight = 480;
	public Scene scene;
	
	@Override
	public EngineOptions onCreateEngineOptions()
	{
		Camera camera = new Camera(0, 0, cameraWidth, cameraHeight);
		final EngineOptions options = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, 
				new RatioResolutionPolicy(cameraWidth, cameraHeight), camera);
		return options;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) throws Exception
	{
		onCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback onCreateSceneCallback) throws Exception
	{
		scene = new Scene();
		onCreateSceneCallback.onCreateSceneFinished(scene);
	}

	@Override
	public void onPopulateScene(Scene sceneIn, OnPopulateSceneCallback onPopulateSceneCallback) throws Exception
	{
		onPopulateSceneCallback.onPopulateSceneFinished();
	}
}
