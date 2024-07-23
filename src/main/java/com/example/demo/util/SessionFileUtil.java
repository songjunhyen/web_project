package com.example.demo.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.vo.CartItem;

public class SessionFileUtil {
    private static final String SESSION_DIR = "path/to/session/files/";

    // 세션 데이터 파일 이름 생성
    private static Path getSessionFilePath(String sessionId) throws NoSuchAlgorithmException {
        String hash = hashSessionId(sessionId);
        // 첫 두 자리를 디렉토리 이름으로 사용
        String subDir = hash.substring(0, 2);
        return Paths.get(SESSION_DIR, subDir, hash + ".txt");
    }

    // 세션 ID를 해시하는 메서드
    private static String hashSessionId(String sessionId) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(sessionId.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // 세션 데이터를 파일에 저장하는 메서드
    public static void saveSession(String sessionId, List<CartItem> cartItems) throws IOException {
        Path filePath = null;
		try {
			filePath = getSessionFilePath(sessionId);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // 파일 저장을 위한 디렉토리가 존재하지 않으면 생성
        Files.createDirectories(filePath.getParent());

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (CartItem item : cartItems) {
                writer.write(item.getProductid() + ":" + item.getName() + ":" + item.getColor() + ":" +
                             item.getSize() + ":" + item.getCount() + ":" + item.getPrice());
                writer.newLine();
            }
        }
    }

    // 세션 데이터를 파일에서 읽어오는 메서드
    public static List<CartItem> loadSession(String sessionId) throws IOException, NoSuchAlgorithmException {
        Path filePath = getSessionFilePath(sessionId);
        List<CartItem> cartItems = new ArrayList<>();
        if (Files.exists(filePath)) {
            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split(":");
                    if (details.length == 6) {
                        CartItem item = new CartItem(
                            Integer.parseInt(details[0]),
                            details[1],
                            details[2],
                            details[3],
                            Integer.parseInt(details[4]),
                            Integer.parseInt(details[5])
                        );
                        cartItems.add(item);
                    }
                }
            }
        }
        return cartItems;
    }
}