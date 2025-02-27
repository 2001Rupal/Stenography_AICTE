import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

public class ImageEncrypt {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            // Load the image
            System.out.print("Enter the path of the image to encrypt: ");
            String imagePath = scanner.nextLine();
            BufferedImage img = ImageIO.read(new File("hello_world.png"));

            System.out.print("Enter the secret message: ");
            String message = scanner.nextLine();

            System.out.print("Enter a passcode: ");
            String password = scanner.nextLine();

            int width = img.getWidth();
            int height = img.getHeight();

            int n = 0, m = 0, z = 0; // Pixel coordinates + color channel index

            // Encode the message length into the first pixel
            int msgLength = message.length();
            img.setRGB(0, 0, (msgLength << 16) | (msgLength << 8) | msgLength); 

            // Encode the message characters
            for (int i = 0; i < message.length(); i++) {
                int pixel = img.getRGB(m, n);
                int charValue = message.charAt(i);

                int newPixel;
                if (z == 0) {
                    newPixel = (pixel & 0xFFFF00) | charValue; // Modify blue
                } else if (z == 1) {
                    newPixel = (pixel & 0xFF00FF) | (charValue << 8); // Modify green
                } else {
                    newPixel = (pixel & 0x00FFFF) | (charValue << 16); // Modify red
                }

                img.setRGB(m, n, newPixel);

                // Move diagonally in the image
                n++;
                m++;
                z = (z + 1) % 3; // Cycle through RGB channels
            }

            // Save the encrypted image
            File output = new File("encryptedImage.png");
            ImageIO.write(img, "png", output);

            System.out.println("Encryption successful! Encrypted image saved as encryptedImage.png");

        } catch (Exception e) {
            System.out.println("An error occurred during encryption: " + e.getMessage());
        }
    }
}
