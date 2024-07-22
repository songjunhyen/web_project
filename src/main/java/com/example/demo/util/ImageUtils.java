package com.example.demo.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtils {

    // 리사이징 메소드
    public static void resizeImage(MultipartFile file, String outputPath, int width, int height) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        File outputFile = new File(outputPath);
        ImageIO.write(bufferedImage, "jpg", outputFile); // "jpg" 또는 "png" 형식으로 저장
    }
}