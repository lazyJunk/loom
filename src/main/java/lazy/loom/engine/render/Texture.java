package lazy.loom.engine.render;

import org.lwjgl.stb.STBImage;

import static lazy.loom.engine.util.Assert.assertOr;
import static lazy.loom.engine.util.Assert.notNull;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {

    private int textureID;

    public Texture(String path) {
        this.load(path);
    }

    private void load(String path) {
        int[] width = new int[1];
        int[] height = new int[1];
        int[] channels = new int[1];

        var imageData = STBImage.stbi_load(path, width, height, channels, 4);

        assertOr(imageData != null, "Image data is null");

        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width[0], height[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);
        glGenerateMipmap(GL_TEXTURE_2D);

        STBImage.stbi_image_free(notNull(imageData));
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void bind() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public int GlID() {
        return this.textureID;
    }
}
