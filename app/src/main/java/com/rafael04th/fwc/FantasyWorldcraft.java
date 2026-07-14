package com.rafael04th.fwc;

import android.widget.Toast;
import com.rafael04th.fwc.core.impl.GLGame;
import com.rafael04th.fwc.core.Screen;
import com.rafael04th.fwc.screens.PlayScreen;

public class FantasyWorldcraft extends GLGame
{
  public Screen getStartScreen() {
    return new PlayScreen(this);
  }

  public void toastException(final Exception e) {
    runOnUiThread(
            () -> Toast.makeText(FantasyWorldcraft.this, e.getMessage(), Toast.LENGTH_SHORT).show());
  }
}
