package com.gruppe4.menschaergerdichnicht;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.gruppe4.menschaergerdichnicht.Fields.Field;
import com.gruppe4.menschaergerdichnicht.Fields.FieldType;
import com.gruppe4.menschaergerdichnicht.Fields.GoalField;
import com.gruppe4.menschaergerdichnicht.Fields.HomeField;
import com.gruppe4.menschaergerdichnicht.Fields.StartField;
import com.gruppe4.menschaergerdichnicht.Logic.PlaygroundModel;
import com.gruppe4.menschaergerdichnicht.Processors.MyInputProcessor;



/**
 * Created by manfrededer on 20.04.16.
 */
public class MainScreen implements Screen {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Player player;
    private MapProperties mapProperties;
    public static float MAXZOOM = 3.5f;
    public static float MINZOOM = 1f;
    private int mapHeight;
    private int mapWidth;
    private MenschAergerDIchNicht game;

    public OrthographicCamera getCamera() {
        return camera;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    MainScreen (MenschAergerDIchNicht game){
        this.game = game;
    }
    @Override
    public void show() {
        try{
            float w = Gdx.graphics.getWidth();
            float h = Gdx.graphics.getHeight();
            map = new TmxMapLoader().load("Brett.tmx");
            loadObjectsInModel(map);
            setMapProperties();
            renderer = new OrthogonalTiledMapRenderer(map);
            initCamera(w,h);
            InputMultiplexer  m = new InputMultiplexer();
            m.addProcessor(new GestureDetector(new MyInputProcessor(this)));
            Gdx.input.setInputProcessor(m);
            //Create the Player
            player = new Player(new Sprite(new Texture("fbpr.png")));
        } catch (Exception e){
            Gdx.app.log("GDX", "show fails because: "+e.getMessage());
        }

    }

    private void loadObjectsInModel(TiledMap map) {
        MapLayer objectLayer = map.getLayers().get("KegelLayer");
        if(objectLayer!=null && objectLayer.getObjects().getCount()!=0){
            MapObjects mapObjects = objectLayer.getObjects();
            String objectName;
            for (MapObject object:mapObjects){
                if(object!=null && object instanceof EllipseMapObject){
                    Ellipse el = ((EllipseMapObject)object).getEllipse();
                    objectName = object.getName();
                    if(objectName.contains("Home")){
                        PlaygroundModel.homeFields.add(new HomeField(el.x,el.y, objectName));
                    } else if(objectName.contains("Start")){
                        PlaygroundModel.startFields.add(new StartField(el.x,el.y,objectName));
                    } else if(objectName.contains("Ziel")){
                        PlaygroundModel.goalFields.add(new GoalField(el.x,el.y,objectName));
                    } else if(objectName.contains("Pos")){
                        PlaygroundModel.normalFields.add(new Field(el.x,el.y,FieldType.NormalField,objectName));
                    }
                }
            }
        }
    }


    private void initCamera(float screenWidth, float scrrenHeight) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, scrrenHeight);
        camera.zoom = MAXZOOM;
        camera.position.set(mapWidth / 2, mapHeight / 2, 0);
        camera.update();

    }


    private void setMapProperties() {
        if(map != null){
            mapProperties = map.getProperties();
            int mw = mapProperties.get("width", Integer.class);
            int mh = mapProperties.get("height", Integer.class);
            int tilePixelWidth = mapProperties.get("tilewidth", Integer.class);
            int tilePixelHeight = mapProperties.get("tileheight", Integer.class);

            mapWidth = mw * tilePixelWidth;
            mapHeight = mh * tilePixelHeight;
        }
    }

    @Override
    public void render(float delta) {
        try{
            Gdx.gl.glClearColor(0,0,0,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            camera.update();
            renderer.setView(camera);
            renderer.render();

            //Draw my Player
            /*renderer.getBatch().begin();
            player.draw(renderer.getBatch());
            renderer.getBatch().end();*/
        }catch(Exception e){
            Gdx.app.log("GDX", "render fails because: "+e.getMessage());
        }

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        //player.getTexture().dispose();
    }

    public void sendTap(){
        game.someMethod();
    }


}
