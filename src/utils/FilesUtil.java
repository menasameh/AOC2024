package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesUtil {
    public static String getContentOf(String fileName) {
        Path filePath = Path.of(fileName);
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            return null;
        }
    }

    public static void writeToFile(String fileName, String content) {
        try {
            Files.write(Paths.get(fileName), content.getBytes());
        } catch (IOException _) {
        }
    }

    public static void writeImage(String fileName, BufferedImage image) {
        File outputfile = new File(fileName);
        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException _) {
        }
    }
}
