package com.rboladao.fantasyworldcraft;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
  private static final String TAG = "MainActivity.class";

  private GLSurfaceView glView;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(TAG, "onCreate");
    setContentView(R.layout.activity_main);
    
    glView = (GLSurfaceView) findViewById(R.id.gameview);
  }
  
  protected void onResume() {
    super.onResume();
    if (glView != null) {
      glView.onResume();
    }
  }
  
  protected void onPause() {
    super.onPause();
    if (glView != null) {
      glView.onPause();
    }
  }
}
