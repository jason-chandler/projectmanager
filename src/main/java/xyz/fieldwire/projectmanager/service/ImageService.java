package xyz.fieldwire.projectmanager.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;


@Service
public class ImageService {
    public final static String CONTENT_ROOT = "content" + File.separator;
    public final static String IMAGE_FORMAT = "png";


    public BufferedImage resize(BufferedImage image, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, newWidth, newHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    public Path imagePathResolver(String folder, String name) {
        return Objects.nonNull(folder) && Objects.nonNull(name) ? Path.of(CONTENT_ROOT + folder + File.separator + name + "." + IMAGE_FORMAT) : null;
    }

    public Path imagePathResolver(String uri) {
        return Objects.nonNull(uri) ? Path.of(CONTENT_ROOT + uri + "." + IMAGE_FORMAT) : null;
    }

    public Path filePathResolver(String uri) {
        return Objects.nonNull(uri) ? Path.of(CONTENT_ROOT + uri) : null;
    }

    public byte[] getImageIfExists(String uri) {
        Path path = imagePathResolver(uri);
        try {
            if (Objects.nonNull(path) && Files.exists(path) && Files.isReadable(path)) {
                return Files.readAllBytes(path);
            }
        } catch (IOException exception) {
            System.err.println("Image not found " + path);
            exception.printStackTrace();
        }
        return null;
    }

    public void writeImageToDisk(BufferedImage image, Long id, String name, Boolean overwrite) throws IOException {
        Path path = imagePathResolver(String.valueOf(id), name);
        Files.createDirectories(path.getParent());
        if (!Files.exists(path) || overwrite) {
            ImageIO.write(image, IMAGE_FORMAT, path.toFile());
        } else {
            throw new FileAlreadyExistsException(String.valueOf(path.getFileName()));
        }
    }

    public void deleteImageIfExists(String uri) throws IOException {
        Path path = imagePathResolver(uri);
        if (Objects.nonNull(path)) {
            Files.deleteIfExists(path);
        }
    }

    public void deleteFolderIfExists(String uri) throws IOException {
        Path path = filePathResolver(uri);
        if (Objects.nonNull(path)) {
            Files.deleteIfExists(path.getParent());
        }
    }
}
