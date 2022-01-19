package lazy.loom.engine.render;

import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static lazy.loom.engine.util.Assert.assertOr;
import static lazy.loom.engine.util.Assert.tryCatch;
import static lazy.loom.engine.util.Log.log;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private int programID = 0;
    private int vertexShaderID = 0;
    private int fragShaderID = 0;

    private Map<String, Integer> uniformLocations = new HashMap<>();

    public Shader(String vertex, String fragment) {
        createShaders(vertex, fragment);
        linkShadersToProgram();
        collectUniformLocations();
    }

    public void bind() {
        glUseProgram(programID);
    }

    public void unbind() {
        glUseProgram(0);
    }

    private void collectUniformLocations() {
        int[] uniformCount = new int[1];
        glGetProgramiv(this.programID, GL_ACTIVE_UNIFORMS, uniformCount);
        for (int i = 0; i < uniformCount[0]; i++) {
            var size = BufferUtils.createIntBuffer(4);
            var type = BufferUtils.createIntBuffer(4);
            var uniformName = glGetActiveUniform(programID, i, 512, size, type);
            uniformLocations.put(uniformName, i);
        }
    }

    private void createShaders(String vertex, String fragment) {
        programID = glCreateProgram();
        assertOr(programID != 0, "Couldn't create the shader program.");

        log("Creating vertex shader.");

        tryCatch(() -> {
            var verStr = Files.readString(Paths.get(vertex));

            vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
            assertOr(vertexShaderID != 0, "Couldn't create Vertex Shader.");

            glShaderSource(vertexShaderID, verStr);
            glCompileShader(vertexShaderID);

            assertOr(glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) != 0, "Something went wrong while compiling " + vertex + ".\n" + glGetShaderInfoLog(vertexShaderID));

            glAttachShader(programID, vertexShaderID);
        });

        log("Creating fragment shader.");

        tryCatch(() -> {
            var fragStr = Files.readString(Paths.get(fragment));
            fragShaderID = glCreateShader(GL_FRAGMENT_SHADER);
            assertOr(fragShaderID != 0, "Couldn't create Fragment Shader.");

            glShaderSource(fragShaderID, fragStr);
            glCompileShader(fragShaderID);

            assertOr(glGetShaderi(fragShaderID, GL_COMPILE_STATUS) != 0, "Something went wrong while compiling " + fragment + ".\n" + glGetShaderInfoLog(fragShaderID));

            glAttachShader(programID, fragShaderID);
        });
    }

    private void linkShadersToProgram() {
        log("Linking shaders to program.");

        glLinkProgram(programID);
        assertOr(glGetProgrami(programID, GL_LINK_STATUS) != 0, "Something went wrong while linking shaders.\n${glGetProgramInfoLog(programID)}");

        glValidateProgram(programID);
        assertOr(glGetProgrami(programID, GL_VALIDATE_STATUS) != 0, "Something went wrong while validating shaders.\n${glGetProgramInfoLog(programID)}");
    }

    public void dispose() {
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragShaderID);
        glDeleteProgram(programID);
    }

    public void vec2f(String name, Vector2f vec2) {
        glUniform2f(uniformLocations.get(name), vec2.x, vec2.y);
    }

    public void vec3f(String name, Vector3f vec3) {
        glUniform3f(uniformLocations.get(name), vec3.x, vec3.y, vec3.z);
    }

    public void vec4f(String name, Vector4f vec4) {
        glUniform4f(uniformLocations.get(name), vec4.x, vec4.y, vec4.z, vec4.w);
    }

    public void vec2i(String name, Vector2i vec2) {
        glUniform2i(uniformLocations.get(name), vec2.x, vec2.y);
    }

    public void vec3i(String name, Vector3i vec3) {
        glUniform3i(uniformLocations.get(name), vec3.x, vec3.y, vec3.z);
    }

    public void vec4i(String name, Vector4i vec4) {
        glUniform4i(uniformLocations.get(name), vec4.x, vec4.y, vec4.z, vec4.w);
    }

    public void i(String name, int value) {
        glUniform1i(uniformLocations.get(name), value);
    }

    public void f(String name, float value) {
        glUniform1f(uniformLocations.get(name), value);
    }

    public void bool(String name, boolean bool) {
        glUniform1i(uniformLocations.get(name), bool ? 1 : 0);
    }

    public void intArr(String name, int[] intArray) {
        glUniform1iv(uniformLocations.get(name), intArray);
    }

    public void mat3(String name, Matrix3f mat3) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            var floatBuff = stack.mallocFloat(9);
            mat3.get(floatBuff);
            glUniformMatrix3fv(uniformLocations.get(name), false, floatBuff);
        }
    }

    public void mat4(String name, Matrix4f mat4) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            var floatBuff = stack.mallocFloat(16);
            mat4.get(floatBuff);
            glUniformMatrix4fv(uniformLocations.get(name), false, floatBuff);
        }
    }
}