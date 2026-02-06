package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Flyway Java migration to insert product images into the database.
 * This migration reads image files from resources and updates products
 * with their corresponding images based on category.
 */
public class V3__insert_product_images extends BaseJavaMigration {

    // Mapping of category IDs to their image files
    private static final Map<Integer, String> CATEGORY_IMAGES = new HashMap<>();

    static {
        CATEGORY_IMAGES.put(1, "cards-image.png");      // Cards
        CATEGORY_IMAGES.put(2, "wearables-image.png");  // Wearables
        CATEGORY_IMAGES.put(3, "tags-stickers-image.png"); // Tags & Stickers
        CATEGORY_IMAGES.put(4, "desk-office-image.jpg");   // Desk & Office
        CATEGORY_IMAGES.put(5, "accessories-image.jpg");   // Accessories
    }

    @Override
    public void migrate(Context context) throws Exception {
        String updateSql = "UPDATE tb_products SET image = ?, image_name = ? WHERE category_id = ? AND image IS NULL";

        for (Map.Entry<Integer, String> entry : CATEGORY_IMAGES.entrySet()) {
            Integer categoryId = entry.getKey();
            String imageName = entry.getValue();

            byte[] imageBytes = loadImageFromResources(imageName);

            if (imageBytes != null) {
                try (PreparedStatement stmt = context.getConnection().prepareStatement(updateSql)) {
                    stmt.setBytes(1, imageBytes);
                    stmt.setString(2, imageName);
                    stmt.setInt(3, categoryId);
                    stmt.executeUpdate();
                }
            }
        }
    }

    /**
     * Load image bytes from the resources folder.
     *
     * @param imageName the name of the image file
     * @return byte array of the image, or null if not found
     */
    private byte[] loadImageFromResources(String imageName) {
        String resourcePath = "db/images/" + imageName;

        try {
            ClassPathResource resource = new ClassPathResource(resourcePath);
            try (InputStream inputStream = resource.getInputStream()) {
                return inputStream.readAllBytes();
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load image " + imageName + " from resources: " + e.getMessage());
            return null;
        }
    }
}
