package com.rafael04th.fwc.screens;

import android.opengl.GLES20;
import android.util.Log;
import com.rafael04th.fwc.FantasyWorldcraft;
import com.rafael04th.fwc.core.Game;
import com.rafael04th.fwc.core.Loader;
import com.rafael04th.fwc.core.Screen;
import com.rafael04th.fwc.core.impl.GLGame;
import com.rafael04th.fwc.core.impl.GLGraphics;
import com.rafael04th.fwc.entity.Player;
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
  public static final Block[] HOTBAR = new Block[]{Blocks.COBBLE, Blocks.DIRT, Blocks.SAND, Blocks.GRASS};
  private static final float PLACE_BREAK_COOLDOWN = 0.1f;

  GLGraphics glGraphics;
  private float[] scratch = new float[16];

  private Matrix4f scratchMat = new Matrix4f();

  private World world;
  private ChunkUploader uploader;
  private ChunkMesher mesher;

  private ShaderProgram cubeProgram, uiProgram, whiteProgram;
  private Texture boxTexture;
  private Camera fpsCamera;

  private Loader loader;

  private WorldRenderer worldRenderer;
  
  private VirtualJoystick joystick;
  private OrbitControl orbit;
  private VirtualButton btnBreak, btnPlace, btnJump, btnNextBlock;
  private Texture uiAtlas;
  private TextureRegion joystickBaseRegion, joystickHandleRegion, btnBreakRegion, btnPlaceRegion, btnJumpRegion;
  private SpriteBatcher spriteBatcher;
  
  private Player player;
  
  private MeshBuilder wireMesher;
  private Mesh selectMesh=null;
  
  int hotbarIndex = 0;
  float placeBreakTimer = 0;
  
  Transform selectTransform;

  public PlayScreen(Game game) {
    super(game);
    try {
      glGraphics = ((GLGame) game).getGLGraphics();
      loader = new Loader(game.getFileIO());

      fpsCamera = new Camera();
      fpsCamera.moveXZ(0, 0);
      fpsCamera.moveY(34);
      fpsCamera.turn(0, 0);

      world = new World(new RudeChunkMakerV1(System.nanoTime()), 0, 0);
      uploader = new ChunkUploader();
      mesher = new ChunkMesher(world, uploader, false, true, true, 128 / 16, 128 / 16);
      selectTransform = new Transform();
      selectTransform.scale.set(1.01f, 1.01f, 1.01f);
      
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
      
      wireMesher = new MeshBuilder(false, false, false, 1, 1);
      
    } catch (Exception e) {
      ((FantasyWorldcraft) game).toastException(e);
    }
  }

  public void resume() {
    try {
      mesher.start();
      cubeProgram = loader.loadShader("textured2.vert", "textured2.frag");
      uiProgram = loader.loadShader("textured1.vert", "textured1.frag");
      whiteProgram = loader.loadShader("justpos.vert", "justwhite.frag");
      boxTexture = loader.loadTexture("TerrainAtlas.png");
      worldRenderer = new WorldRenderer(cubeProgram, fpsCamera, boxTexture);
      
      uiAtlas = loader.loadTexture("ui_atlas.png");
      joystickBaseRegion = new TextureRegion(uiAtlas, 0, 0, 64, 64);
      joystickHandleRegion = new TextureRegion(uiAtlas, 64, 0, 16, 16);
      btnBreakRegion = new TextureRegion(uiAtlas, 64, 16, 16, 16);
      btnPlaceRegion = new TextureRegion(uiAtlas, 80, 16, 16, 16);
      btnJumpRegion = new TextureRegion(uiAtlas, 80, 0, 16, 16);
      spriteBatcher = new SpriteBatcher(1000);
      
      selectMesh = new Mesh(/*wireMesher.cube(new float[]{0,0,0}, new int[]{0,0,0}).build()*/ new WireFrameMesher().cube(new float[]{0,0,0}).build(), VoxelFormats.JUST_POSITION);

      glEnable(GL_DEPTH_TEST);
      glEnable(GL_CULL_FACE);
      glClearColor(0, 0, 0.7f, 1f);
    } catch (Exception e) {
      ((FantasyWorldcraft) game).toastException(e);
    }
  }

  public void pause() {
    mesher.stop();
  }

  public void update(float deltaTime) {
    try {
      if (placeBreakTimer > 0.001f) {
        placeBreakTimer -= deltaTime;
      }
      List<Input.TouchEvent> touches = game.getInput().getTouchEvents();
      for (Input.TouchEvent te : touches) {
        if (joystick.update(te)) {
        } else if (btnBreak.update(te)) {
          Optional<BlockHit> result = world.hit(fpsCamera, 11f);
          if (result.isPresent() && placeBreakTimer <= 0.001f) {
            BlockHit hit = result.get();
            world.set((int)hit.x, (int)hit.y, (int)hit.z, Blocks.AIR);
            placeBreakTimer = PLACE_BREAK_COOLDOWN;
          }
        } else if (btnPlace.update(te)) {
          Optional<BlockHit> result = world.hit(fpsCamera, 11f);
          if (result.isPresent() && placeBreakTimer <= 0.001f) {
            BlockHit hit = result.get();
            world.set((int)(hit.x+hit.normalX), (int)(hit.y+hit.normalY), (int)(hit.z+hit.normalZ), HOTBAR[hotbarIndex]);
            placeBreakTimer = PLACE_BREAK_COOLDOWN;
          }
        } else if (btnJump.update(te)) {
        } else if (btnNextBlock.update(te)) {
          if (te.type == Input.TouchEvent.TOUCH_DOWN) hotbarIndex = (hotbarIndex + 1) % HOTBAR.length;
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
        Optional<BlockHit> result = world.hit(fpsCamera, 30f);

        if (result.isPresent()) {
          Log.d("PlayScreen.class", "HIT!");
          BlockHit hit = result.get();
          selectTransform.translation.set(hit.x, hit.y, hit.z);
          whiteProgram.bind();
          check("whiteProgram.bind()");
          selectMesh.bind();
          check("selectMesh.bind()");
          fpsCamera.getCameraMatrix().mul(selectTransform.getTransformMatrix(), scratchMat).get(scratch);
          whiteProgram.setAttribs(selectMesh.getFormat());
          check("whiteProgram.setAttribs()");
          whiteProgram.setUniformMat4("uMVP", scratch, 0);
          check("whiteProgram.setUniformMat4()");
          whiteProgram.setUniform4f("uColor", 1f, 0, 1f, 1f);
          check("whiteProgram.setUniform4f()");
          glDisableVertexAttribArray(1);
          glDisableVertexAttribArray(2);
          glDepthMask(false);
          selectMesh.render(GL_LINES);
          check("selectMesh.render()");
          glDepthMask(true);
        }
      }

      scratchMat.identity().ortho2D(0, glGraphics.getWidth(), glGraphics.getHeight(), 0).get(scratch);
      uiProgram.bind();
      uiAtlas.bind();
      spriteBatcher.beginBatch(uiProgram, uiAtlas);
      uiProgram.setUniform1i("uTex", 0);
      uiProgram.setUniformMat4("uMVP", scratch, 0);
      {
        float diameter = joystick.radius * 2;
        spriteBatcher.drawSprite(joystick.centerX, joystick.centerY, diameter, diameter, joystickBaseRegion);
        spriteBatcher.drawSprite(joystick.currentX(), joystick.currentY(), diameter/4f, diameter/4f, joystickHandleRegion);
      }
      {
        spriteBatcher.drawSprite(btnBreak.x+btnBreak.width/2, btnBreak.y+btnBreak.height/2, btnBreak.width, btnBreak.height, btnBreakRegion);
        spriteBatcher.drawSprite(btnPlace.x+btnPlace.width/2, btnPlace.y+btnPlace.height/2, btnPlace.width, btnPlace.height, btnPlaceRegion);
        spriteBatcher.drawSprite(btnJump.x+btnJump.width/2, btnJump.y+btnJump.height/2, btnJump.width, btnJump.height, btnJumpRegion);
        spriteBatcher.drawSprite(btnNextBlock.x+btnNextBlock.width/2, btnNextBlock.y+btnNextBlock.height/2, btnNextBlock.width, btnNextBlock.height, joystickHandleRegion);
      }
      spriteBatcher.endBatch(uiProgram);
    } catch (Exception e) {
      ((FantasyWorldcraft) game).toastException(e);
    }
  }

  public void dispose() {
    if (world != null) world.dispose();
    if (cubeProgram != null) cubeProgram.dispose();
    if (boxTexture != null) boxTexture.dispose();
    if (mesher != null) mesher.stop();
  }
  
  static void check(String where) {
    int err = GLES20.glGetError();
    Log.d("PlayScreen.class", where + " : " + err);
  }
}
