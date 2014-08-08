package com.plasmafx;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

class PlasmaFX
  {

  private Program mProgram;
  private Shader mShader;
  private float time = 0.0f;
  private int width = 0;
  private int height = 0;
  private long currentTime = 0;
  private long lastTime = 0;
  private float delta = 0.0f;

  public PlasmaFX(Context context)
    {
    DisplayMetrics displayMetrics = new DisplayMetrics();

    mShader = new Shader(context.getResources().openRawResource(
        R.raw.vertexshader), context.getResources().openRawResource(
        R.raw.fragmentshader));

    WindowManager w = (WindowManager)context
        .getSystemService(Context.WINDOW_SERVICE);
    Display d = w.getDefaultDisplay();
    d.getMetrics(displayMetrics);
    width = displayMetrics.widthPixels;
    height = displayMetrics.heightPixels;
    }

  public void init()
    {
    mShader.compile();
    mProgram = new Program(mShader.getVertexShader(),
        mShader.getFragmentShader());
    }

  public void draw()
    {
    currentTime = System.nanoTime();
    if(lastTime != 0)
      {
      delta = (float)(currentTime - lastTime);
      }
    lastTime = currentTime;

    mProgram.setViewMatrix(0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
        0.0f);
    mProgram.draw(time, width, height);
    time += (delta * 0.0000000005f);
    }

  public void setViewPort(int width, int height)
    {
    mProgram.setProjectionMatrix(-1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 5.0f);
    }
  }