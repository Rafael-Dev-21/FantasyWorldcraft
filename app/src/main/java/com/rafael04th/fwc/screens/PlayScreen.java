package com.rafael04th.fwc.screens;

import android.opengl.GLES20;
import android.util.Log;
import com.rafael04th.fwc.FantasyWorldcraft;
import com.rafael04th.fwc.audio.Music;
import com.rafael04th.fwc.core.Game;
import com.rafael04th.fwc.core.Loader;
import com.rafael04th.fwc.core.Screen;
import com.rafael04th.fwc.core.impl.GLGame;
import com.rafael04th.fwc.core.impl.GLGraphics;
import com.rafael04th.fwc.entity.Player;
import com.rafael04th.fwc.gameplay.BlockInteractor;
import com.rafael04th.fwc.gameplay.Hotbar;
import com.rafael04th.fwc.graphics.Camera;
import com.rafael04th.fwc.graphics.Mesh;
import com.rafael04th.fwc.graphics.MeshBuilder;
import com.rafael04th.fwc.graphics.ShaderProgram;
import com.rafael04th.fwc.graphics.Texture;
import com.rafael04th.fwc.graphics.TextureRegion;
import com.rafael04th.fwc.graphics.Transform;
import com.rafael04th.fwc.graphics.VoxelFormats;
import com.rafael04th.fwc.graphics.WireFrameMesher;
import com.rafael04th.fwc.graphics.render.SpriteBatcher;
import com.rafael04th.fwc.graphics.render.WorldRenderer;
import com.rafael04th.fwc.input.Input;
import com.rafael04th.fwc.input.OrbitControl;
import com.rafael04th.fwc.input.VirtualButton;
import com.rafael04th.fwc.input.VirtualJoystick;
import com.rafael04th.fwc.render.HudRenderer;
import com.rafael04th.fwc.render.SelectionRenderer;
import com.rafael04th.fwc.world.Block;
import com.rafael04th.fwc.world.BlockHit;
import com.rafael04th.fwc.world.Blocks;
import com.rafael04th.fwc.world.World;
import com.rafael04th.fwc.world.gen.RudeChunkMakerV1;
import com.rafael04th.fwc.world.meshing.ChunkMesher;
import com.rafael04th.fwc.world.meshing.ChunkUploader;
import java.util.List;
import java.util.Optional;
import org.joml.Matrix4f;
import static android.opengl.GLES20.*;

public class PlayScreen extends Screen {
  public static final Hotbar HOTBAR = new Hotbar(Blocks.COBBLE, Blocks.DIRT, Blocks.SAND, Blocks.GRASS);


  GLGraphics glGraphics;
  private final float[] scratch = new float[16];

  private final Matrix4f scratchMat = new Matrix4f();

  private World world;
  private ChunkUploader uploader;
  private ChunkMesher mesher;
  private BlockInteractor interactor;

  private ShaderProgram cubeProgram, uiProgram;
  private Texture boxTexture;
  private Camera fpsCamera;
  private Music bgm;

  private Loader loader;

  private WorldRenderer worldRenderer;
  
  private VirtualJoystick joystick;
  private OrbitControl orbit;
  private VirtualButton btnBreak, btnPlace, btnJump, btnNextBlock;
  private Texture uiAtlas;
  private SpriteBatcher spriteBatcher;
  private HudRenderer hudRenderer;
  
  private Player player;
  private SelectionRenderer selectionRenderer;

  public PlayScreen(Game game) {
    super(game);
    try {
      glGraphics = ((GLGame) game).getGLGraphics();
      loader = new Loader(game.getFileIO());

      fpsCamera = new Camera();
      fpsCamera.moveXZ(0, 0);
      fpsCamera.moveY(34);
      fpsCamera.turn(0, 0);
      
      bgm = game.getAudio().newMusic("orange-fields-evermore.ogg");
      bgm.setVolume(0.5f);
      bgm.setLooping(true);

      world = new World(new RudeChunkMakerV1(System.nanoTime()), 0, 0);
      uploader = new ChunkUploader();
      mesher = new ChunkMesher(world, uploader, false, true, true, 128 / 16, 128 / 16);
      interactor = new BlockInteractor(world);
      
      {
        int cx = Math.min(glGraphics.getWidth(), glGraphics.getHeight())/5;
        int cy = glGraphics.getHeight()-cx;
        float radius = cx;
        int pad = cx / 5;
        cx += pad;
        cy -= pad;
        joystick = new VirtualJoystick(cx, cy, radius);
      }
      orbit = new OrbitControl();
      {
        int len = Math.min(glGraphics.getHeight(), glGraphics.getWidth())/10;
        int pad = len / 4;
        int startX = glGraphics.getWidth()-(len+pad)*4 - pad;
        int startY = glGraphics.getHeight() -  len - pad * 3;
        int x = startX;
        int y = startY;
        
        {
          btnBreak = new VirtualButton(x, y, len, len);
          x += pad + len;
          y -= pad + len;
        }
        {
          btnPlace = new VirtualButton(x, y, len, len);
          x += pad + len;
          y -= pad + len;
        }
        {
          btnJump = new VirtualButton(x, y, len, len);
          x += pad + len;
          y -= pad + len;
        }
        {
          btnNextBlock = new VirtualButton(x, y, len, len);
          x += pad + len;
          y -= pad + len;
        }
      }

      player = world.newPlayer();
      
    } catch (Exception e) {
      ((FantasyWorldcraft) game).toastException(e);
    }
  }

  public void resume() {
    try {
      mesher.start();
      cubeProgram = loader.loadShader("textured2.vert", "textured2.frag");
      uiProgram = loader.loadShader("textured1.vert", "textured1.frag");
      boxTexture = loader.loadTexture("TerrainAtlas.png");
      worldRenderer = new WorldRenderer(cubeProgram, fpsCamera, boxTexture);
      
      uiAtlas = loader.loadTexture("ui_atlas.png");
      {
        TextureRegion joystickBaseRegion, joystickHandleRegion, btnBreakRegion, btnPlaceRegion, btnJumpRegion;

        joystickBaseRegion = new TextureRegion(uiAtlas, 0, 0, 64, 64);
        joystickHandleRegion = new TextureRegion(uiAtlas, 64, 0, 16, 16);
        btnBreakRegion = new TextureRegion(uiAtlas, 64, 16, 16, 16);
        btnPlaceRegion = new TextureRegion(uiAtlas, 80, 16, 16, 16);
        btnJumpRegion = new TextureRegion(uiAtlas, 80, 0, 16, 16);
        hudRenderer = new HudRenderer(joystickBaseRegion, joystickHandleRegion, btnBreakRegion, btnPlaceRegion, btnJumpRegion, joystickHandleRegion, joystickHandleRegion);
      }
      spriteBatcher = new SpriteBatcher(1000);

      selectionRenderer = new SelectionRenderer(loader.loadShader("justpos.vert", "justwhite.frag"));
      glEnable(GL_DEPTH_TEST);
      glEnable(GL_CULL_FACE);
      glClearColor(0, 0, 0.7f, 1f);
      bgm.play();
    } catch (Exception e) {
      ((FantasyWorldcraft) game).toastException(e);
    }
  }

  public void pause() {
    mesher.stop();
    bgm.pause();
  }

  public void update(float deltaTime) {
    try {
      interactor.update(deltaTime);
      List<Input.TouchEvent> touches = game.getInput().getTouchEvents();
      for (Input.TouchEvent te : touches) {
        if (joystick.update(te)) {
        } else if (btnBreak.update(te)) {
          interactor.breakBlock(fpsCamera);

        } else if (btnPlace.update(te)) {
          interactor.placeBlock(fpsCamera, HOTBAR.current());
        } else if (btnJump.update(te)) {
        } else if (btnNextBlock.update(te)) {
          if (te.type == Input.TouchEvent.TOUCH_DOWN) HOTBAR.next();
        } else if (orbit.update(deltaTime, te)) {}
      }
      uploader.update();
      player.update(deltaTime, joystick, orbit, btnJump, world, fpsCamera);
      world.update(player.getPosition().x, player.getPosition().z, mesher);
    } catch (Exception e) {
      ((FantasyWorldcraft) game).toastException(e);
    }
  }

  public void present(float deltaTime) {
    try {
      glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
      fpsCamera.resize(glGraphics.getWidth(), glGraphics.getHeight());
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
      worldRenderer.render(world);
      
      {
        Optional<BlockHit> result = interactor.hit(fpsCamera, 11f);
        selectionRenderer.render(fpsCamera, result);
      }

      scratchMat.identity().ortho2D(0, glGraphics.getWidth(), glGraphics.getHeight(), 0).get(scratch);
      uiProgram.bind();
      uiProgram.setUniform1i("uTex", 0);
      uiProgram.setUniformMat4("uMVP", scratch, 0 );
      uiAtlas.bind();
      hudRenderer.render(spriteBatcher, uiProgram, uiAtlas, glGraphics, joystick, btnBreak, btnPlace, btnJump, btnNextBlock);
    } catch (Exception e) {
      ((FantasyWorldcraft) game).toastException(e);
    }
  }

  public void dispose() {
    if (world != null) world.dispose();
    if (cubeProgram != null) cubeProgram.dispose();
    if (boxTexture != null) boxTexture.dispose();
    if (mesher != null) mesher.stop();
    if (bgm != null) {
      bgm.stop();
      bgm.dispose();
    }
    if (selectionRenderer != null) selectionRenderer.dispose();
    if (spriteBatcher != null) spriteBatcher.dispose();
  }
  
  static void check(String where) {
    int err = GLES20.glGetError();
    Log.d("PlayScreen.class", where + " : " + err);
  }
}
