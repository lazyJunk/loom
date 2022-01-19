package lazy.loom.engine.render.font;

import org.joml.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static lazy.loom.engine.util.Assert.tryCatch;

public class BitmapFont {

    private final String filePath;
    private final int fontSize;

    private int width;
    private int height;
    private Map<Integer, CharInfo> charMap = new HashMap<>();

    public BitmapFont(String filePath, int fontSize) {
        this.filePath = filePath;
        this.fontSize = fontSize;
        this.generate();
    }

    private void generate() {
        var internalFont = new Font(filePath, Font.PLAIN, this.fontSize);
        int size = (int) Math.sqrt(internalFont.getNumGlyphs()) * internalFont.getSize() + 1;

        var ref = new Object() {
            BufferedImage imgBuff = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        };

        var graphics = ref.imgBuff.createGraphics();
        graphics.setFont(internalFont);

        FontMetrics fontMetrics = graphics.getFontMetrics();
        this.height = fontMetrics.getHeight();

        int x = 0;
        int y = (int) (fontMetrics.getHeight() * 1.4f);

        for (int i = 0; i < internalFont.getNumGlyphs(); i++) {
            if (internalFont.canDisplay(i)) {
                var charInfo = new CharInfo(x, y, fontMetrics.charWidth(i), fontMetrics.getHeight());
                charMap.put(i, charInfo);
                width = Math.max(x + fontMetrics.charWidth(i), width);
                x += charInfo.width;
                if (x > size) {
                    x = 0;
                    y += fontMetrics.getHeight() * 1.4f;
                    height += fontMetrics.getHeight() * 1.4f;
                }
            }
        }

        height += fontMetrics.getHeight() * 1.4f;

        graphics.dispose();

        ref.imgBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics = ref.imgBuff.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setFont(internalFont);
        graphics.setColor(Color.WHITE);
        for (int i = 0; i < internalFont.getNumGlyphs(); i++) {
            if (internalFont.canDisplay(i)) {
                var charInfo = charMap.get(i);
                Vector2f[] vector2fs = this.calcCharPosition(width, height, charInfo);
                graphics.drawString("" + (char) i, charInfo.x, charInfo.y);
            }
        }
        graphics.dispose();

        tryCatch(() -> {
            ImageIO.write(ref.imgBuff, "png", new File("./font.png"));
        });
    }

    private Vector2f[] calcCharPosition(int fontWidth, int fontHeight, CharInfo info) {
        float x0 = (float) (info.x / fontWidth);
        float x1 = (float) ((info.x + info.width) / fontWidth);
        float y0 = (float) (info.y / fontHeight);
        float y1 = (float) (info.y + info.height / fontHeight);

        return new Vector2f[]{
                new Vector2f(x0, y0),
                new Vector2f(x0, y1),
                new Vector2f(x1, y0),
                new Vector2f(x1, y1),
        };
    }

    private record CharInfo(int x, int y, int width, int height) {}
}
