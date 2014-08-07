package com.plasmafx;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class Program
  {

  private int mProgramHandle;

  private FloatBuffer vertexBuffer;
  private ShortBuffer faceBuffer;

  private float vertices[] = {-1.0f, 1.0f, -0.0f, -1.0f, -1.0f, -0.0f, 1.0f,
      -1.0f, -0.0f, 1.0f, 1.0f, -0.0f};

  private short faces[] = {0, 1, 2, 0, 2, 3};
  private float[] mProjectionMatrix = new float[16];
  private float[] mViewMatrix = new float[16];
  private float[] mModelViewProjectionMatrix = new float[16];

  Program(int vertexShaderHandle, int fragmentShaderHandle)
    {
    ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
    bb.order(ByteOrder.nativeOrder());
    vertexBuffer = bb.asFloatBuffer();
    vertexBuffer.put(vertices);
    vertexBuffer.position(0);

    ByteBuffer dlb = ByteBuffer.allocateDirect(faces.length * 2);
    dlb.order(ByteOrder.nativeOrder());
    faceBuffer = dlb.asShortBuffer();
    faceBuffer.put(faces);
    faceBuffer.position(0);

    mProgramHandle = GLES20.glCreateProgram();

    GLES20.glAttachShader(mProgramHandle, vertexShaderHandle);
    GLES20.glAttachShader(mProgramHandle, fragmentShaderHandle);

    GLES20.glLinkProgram(mProgramHandle);

    final int[] linkStatus = new int[1];
    GLES20.glGetProgramiv(mProgramHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

    if(0 == linkStatus[0])
      {
      GLES20.glDeleteProgram(mProgramHandle);
      mProgramHandle = 0;
      }

    if(mProgramHandle == 0)
      {
      throw new RuntimeException("Error creating program.");
      }

    GLES20.glUseProgram(mProgramHandle);
    }

  public void draw(float time, int width, int height)
    {
    Matrix.multiplyMM(mModelViewProjectionMatrix, 0, mProjectionMatrix, 0,
        mViewMatrix, 0);

    int mModelViewProjectionMatrixHandle = GLES20.glGetUniformLocation(
        mProgramHandle, "mModelViewProjectionMatrix");
    GLES20.glUniformMatrix4fv(mModelViewProjectionMatrixHandle, 1, false,
        mModelViewProjectionMatrix, 0);

    int mVertexHandle = GLES20.glGetAttribLocation(mProgramHandle, "vertex");
    GLES20.glEnableVertexAttribArray(mVertexHandle);
    GLES20.glVertexAttribPointer(mVertexHandle, 3, GLES20.GL_FLOAT, false,
        4 * 3, vertexBuffer);

    int mTimeHandle = GLES20.glGetUniformLocation(mProgramHandle, "time");
    GLES20.glEnableVertexAttribArray(mTimeHandle);
    GLES20.glUniform1f(mTimeHandle, time);

    int mResolutionHandle = GLES20.glGetUniformLocation(mProgramHandle,
        "resolution");
    GLES20.glEnableVertexAttribArray(mResolutionHandle);
    GLES20.glUniform2i(mResolutionHandle, width, height);

    GLES20.glDrawElements(GLES20.GL_TRIANGLES, faces.length,
        GLES20.GL_UNSIGNED_SHORT, faceBuffer);

    GLES20.glDisableVertexAttribArray(mVertexHandle);
    }

  public void setProjectionMatrix(float left, float right, float bottom,
      float top, float near, float far)
    {
    Matrix.setIdentityM(mProjectionMatrix, 0);
    Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

  void setViewMatrix(float eyeX, float eyeY, float eyeZ, float centerX,
      float centerY, float centerZ, float upX, float upY, float upZ)
    {
    Matrix.setIdentityM(mViewMatrix, 0);
    Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY,
        centerZ, upX, upY, upZ);
    }
  }
