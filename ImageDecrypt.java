
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

public class ImageDecrypt {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            // Load the encrypted image
            System.out.print("Enter the path of the encrypted image: ");
            String encryptedImagePath = scanner.nextLine();
            BufferedImage img = ImageIO.read(new File(encryptedImagePath));

            // Read the encoded message length from the first pixel
            int lengthPixel = img.getRGB(0, 0);
            int messageLength = lengthPixel & 0xFF; // Extract length from blue channel

            System.out.print("Enter the passcode for decryption: ");
            String enteredPasscode = scanner.nextLine();
            String correctPasscode = "12345"; // Must match encryption passcode

            if (!enteredPasscode.equals(correctPasscode)) {
                System.out.println("YOU ARE NOT AUTHORIZED");
                return;
            }

            StringBuilder decryptedMessage = new StringBuilder();
            int n = 0, m = 0, z = 0; // Pixel coordinates + channel selector

            // Extract message from the image
            for (int i = 0; i < messageLength; i++) {
                int pixel = img.getRGB(m, n);

                int ascii;
                if (z == 0) {
                    ascii = pixel & 0xFF; // Extract blue channel
                } else if (z == 1) {
                    ascii = (pixel >> 8) & 0xFF; // Extract green channel
                } else {
                    ascii = (pixel >> 16) & 0xFF; // Extract red channel
                }

                decryptedMessage.append((char) ascii);

                // Move diagonally
                n++;
                m++;
                z = (z + 1) % 3; // Cycle through RGB channels
            }

            System.out.println("Decrypted message: " + decryptedMessage);

        } catch (Exception e) {
            System.out.println("An error occurred during decryption: " + e.getMessage());
        }
    }
}
