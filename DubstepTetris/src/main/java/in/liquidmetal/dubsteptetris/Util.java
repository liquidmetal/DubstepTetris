package in.liquidmetal.dubsteptetris;

import android.opengl.GLES20;

import java.nio.ByteBuffer;

/**
 * Created by utkarsh on 26/5/13.
 */
public class Util {
    public static int loadShader(int type, String shaderCode) {
        int shaderHandle = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shaderHandle, shaderCode);
        GLES20.glCompileShader(shaderHandle);

        int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if(compileStatus[0] != GLES20.GL_TRUE) {
            String msg = GLES20.glGetShaderInfoLog(shaderHandle);
            GLES20.glDeleteProgram(shaderHandle);
            throw new RuntimeException("glCompileShader failed!");
        }

        return shaderHandle;
    }

    public static int createProgram(String vertexShaderCode, String fragmentShaderCode) {
        int vertexShader = Util.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = Util.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        int programHandle = GLES20.glCreateProgram();
        GLES20.glAttachShader(programHandle, vertexShader);
        GLES20.glAttachShader(programHandle, fragmentShader);
        GLES20.glLinkProgram(programHandle);

        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if(linkStatus[0] != GLES20.GL_TRUE) {
            String msg = GLES20.glGetProgramInfoLog(programHandle);
            GLES20.glDeleteProgram(programHandle);
            throw new RuntimeException("CreateProgram failed!");
        }

        return programHandle;
    }

    public static int createImageTexture(ByteBuffer data, int width, int height, int format) {
        int[] textureHandles = new int[1];
        int textureHandle;

        GLES20.glGenTextures(GLES20.GL_TEXTURE_2D, textureHandles, 0);
        textureHandle = textureHandles[0];

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, format, width, height, 0, format, GLES20.GL_UNSIGNED_BYTE, data);

        return textureHandle;
    }
}
