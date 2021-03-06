package com.plasmafx;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer
  {

  private PlasmaFx mPlasmaFx;

  OpenGLRenderer(Context context)
    {
    mPlasmaFx = new PlasmaFx(context);
    }

  @Override
  public void onSurfaceCreated(GL10 unused, EGLConfig config)
    {
    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

    GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    GLES20.glDepthFunc(GLES20.GL_LEQUAL);
    GLES20.glDepthMask(true);

    mPlasmaFx.init();
    }

  @Override
  public void onDrawFrame(GL10 unused)
    {
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    mPlasmaFx.draw();
    }

  @Override
  public void onSurfaceChanged(GL10 unused, int width, int height)
    {
    GLES20.glViewport(0, 0, width, height);

    mPlasmaFx.setViewPort(width, height);
    }
  }