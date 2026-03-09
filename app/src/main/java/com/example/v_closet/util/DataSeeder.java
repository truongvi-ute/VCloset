package com.example.v_closet.util;

import android.content.Context;
import android.util.Log;

import com.example.v_closet.entity.Pant;
import com.example.v_closet.entity.Product;
import com.example.v_closet.entity.ProductImage;
import com.example.v_closet.entity.ProductVariant;
import com.example.v_closet.entity.Shirt;
import com.example.v_closet.entity.enums.CategoryType;
import com.example.v_closet.repository.ProductRepository;

/**
 * DataSeeder - Tạo dữ liệu mẫu cho database
 */
public class DataSeeder {
    private static final String TAG = "DataSeeder";
    private ProductRepository productRepository;

    public DataSeeder(Context context) {
        this.productRepository = new ProductRepository(context);
    }

    /**
     * Seed tất cả dữ liệu mẫu
     */
    public void seedAll() {
        // Kiểm tra đã có dữ liệu chưa
        if (productRepository.hasProducts()) {
            Log.d(TAG, "Database already has products. Skipping seed.");
            return;
        }

        Log.d(TAG, "Starting data seeding...");
        
        seedShirts();
        seedPants();
        
        Log.d(TAG, "Data seeding completed!");
    }

    /**
     * Seed dữ liệu áo
     */
    private void seedShirts() {
        String[] sizes = {"S", "M", "L", "XL"};
        
        // Shirt 1: Áo thun basic
        Shirt shirt1 = new Shirt(
            "Áo Thun Basic Unisex",
            "Áo thun cotton 100% form rộng thoải mái, phù hợp mọi phong cách",
            250000,
            CategoryType.SHIRT,
            "Cotton 100%",
            "Tay ngắn",
            "Cổ tròn"
        );
        long shirt1Id = productRepository.insertProduct(shirt1);
        
        if (shirt1Id > 0) {
            String[] shirt1Colors = {"Black", "Blue", "Gray", "White"};
            
            for (String color : shirt1Colors) {
                for (String size : sizes) {
                    ProductVariant variant = new ProductVariant(
                        (int) shirt1Id, color, size, 0
                    );
                    long variantId = productRepository.insertVariant(variant);
                    
                    if (size.equals("M")) {
                        String imageName = "shirt1_" + color.toLowerCase();
                        ProductImage image = new ProductImage(
                            (int) shirt1Id,
                            (int) variantId,
                            imageName,
                            color.equals("Black")
                        );
                        productRepository.insertImage(image);
                    }
                }
            }
        }

        // Shirt 2: Áo polo
        Shirt shirt2 = new Shirt(
            "Áo Polo Premium",
            "Áo polo cao cấp, thiết kế thanh lịch, phù hợp đi làm và dạo phố",
            350000,
            CategoryType.SHIRT,
            "Cotton pha",
            "Tay ngắn",
            "Cổ polo"
        );
        long shirt2Id = productRepository.insertProduct(shirt2);
        
        if (shirt2Id > 0) {
            String[] shirt2Colors = {"Black", "Blue", "Cream"};
            
            for (String color : shirt2Colors) {
                for (String size : sizes) {
                    ProductVariant variant = new ProductVariant(
                        (int) shirt2Id, color, size, 0
                    );
                    long variantId = productRepository.insertVariant(variant);
                    
                    if (size.equals("M")) {
                        String imageName = "shirt2_" + color.toLowerCase();
                        ProductImage image = new ProductImage(
                            (int) shirt2Id,
                            (int) variantId,
                            imageName,
                            color.equals("Black")
                        );
                        productRepository.insertImage(image);
                    }
                }
            }
        }

        // Shirt 3: Áo thun thể thao
        Shirt shirt3 = new Shirt(
            "Áo Thun Thể Thao",
            "Áo thun thể thao thoáng mát, thấm hút mồ hôi tốt",
            280000,
            CategoryType.SHIRT,
            "Polyester",
            "Tay ngắn",
            "Cổ tròn"
        );
        long shirt3Id = productRepository.insertProduct(shirt3);
        
        if (shirt3Id > 0) {
            String[] shirt3Colors = {"Blue", "Red", "White"};
            
            for (String color : shirt3Colors) {
                for (String size : sizes) {
                    ProductVariant variant = new ProductVariant(
                        (int) shirt3Id, color, size, 0
                    );
                    long variantId = productRepository.insertVariant(variant);
                    
                    if (size.equals("M")) {
                        String imageName = "shirt3_" + color.toLowerCase();
                        ProductImage image = new ProductImage(
                            (int) shirt3Id,
                            (int) variantId,
                            imageName,
                            color.equals("Blue")
                        );
                        productRepository.insertImage(image);
                    }
                }
            }
        }

        // Shirt 4: Áo sơ mi
        Shirt shirt4 = new Shirt(
            "Áo Sơ Mi Oxford",
            "Áo sơ mi Oxford cao cấp, phong cách lịch lãm",
            420000,
            CategoryType.SHIRT,
            "Cotton Oxford",
            "Tay dài",
            "Cổ bẻ"
        );
        long shirt4Id = productRepository.insertProduct(shirt4);
        
        if (shirt4Id > 0) {
            String[] shirt4Colors = {"Blue", "Pink"};
            
            for (String color : shirt4Colors) {
                for (String size : sizes) {
                    ProductVariant variant = new ProductVariant(
                        (int) shirt4Id, color, size, 0
                    );
                    long variantId = productRepository.insertVariant(variant);
                    
                    if (size.equals("M")) {
                        String imageName = "shirt4_" + color.toLowerCase();
                        ProductImage image = new ProductImage(
                            (int) shirt4Id,
                            (int) variantId,
                            imageName,
                            color.equals("Blue")
                        );
                        productRepository.insertImage(image);
                    }
                }
            }
        }

        // Shirt 5: Áo len
        Shirt shirt5 = new Shirt(
            "Áo Len Cardigan",
            "Áo len cardigan ấm áp, phong cách vintage",
            480000,
            CategoryType.SHIRT,
            "Len",
            "Tay dài",
            "Cổ tim"
        );
        long shirt5Id = productRepository.insertProduct(shirt5);
        
        if (shirt5Id > 0) {
            String[] shirt5Colors = {"Brown", "Cream", "Gray", "Green"};
            
            for (String color : shirt5Colors) {
                for (String size : sizes) {
                    ProductVariant variant = new ProductVariant(
                        (int) shirt5Id, color, size, 0
                    );
                    long variantId = productRepository.insertVariant(variant);
                    
                    if (size.equals("M")) {
                        String imageName = "shirt5_" + color.toLowerCase();
                        ProductImage image = new ProductImage(
                            (int) shirt5Id,
                            (int) variantId,
                            imageName,
                            color.equals("Brown")
                        );
                        productRepository.insertImage(image);
                    }
                }
            }
        }
        
        Log.d(TAG, "Seeded shirts successfully");
    }

    /**
     * Seed dữ liệu quần
     */
    private void seedPants() {
        String[] sizes = {"S", "M", "L"};
        
        // Pant 1: Quần jean
        Pant pant1 = new Pant(
            "Quần Jean Slim Fit",
            "Quần jean co giãn nhẹ, form slim fit ôm vừa vặn, tôn dáng",
            450000,
            CategoryType.PANT,
            "Denim",
            "Dài",
            "Slim-fit"
        );
        long pant1Id = productRepository.insertProduct(pant1);
        
        if (pant1Id > 0) {
            String[] pant1Colors = {"Black", "Blue", "Gray"};
            
            for (String color : pant1Colors) {
                for (String size : sizes) {
                    ProductVariant variant = new ProductVariant(
                        (int) pant1Id, color, size, 0
                    );
                    long variantId = productRepository.insertVariant(variant);
                    
                    if (size.equals("M")) {
                        String imageName = "pant1_" + color.toLowerCase();
                        ProductImage image = new ProductImage(
                            (int) pant1Id,
                            (int) variantId,
                            imageName,
                            color.equals("Black")
                        );
                        productRepository.insertImage(image);
                    }
                }
            }
        }

        // Pant 2: Quần kaki
        Pant pant2 = new Pant(
            "Quần Kaki Straight",
            "Quần kaki form straight thoải mái, phù hợp đi làm và đi chơi",
            380000,
            CategoryType.PANT,
            "Kaki",
            "Dài",
            "Straight"
        );
        long pant2Id = productRepository.insertProduct(pant2);
        
        if (pant2Id > 0) {
            String[] pant2Colors = {"Black", "Cream", "Gray"};
            
            for (String color : pant2Colors) {
                for (String size : sizes) {
                    ProductVariant variant = new ProductVariant(
                        (int) pant2Id, color, size, 0
                    );
                    long variantId = productRepository.insertVariant(variant);
                    
                    if (size.equals("M")) {
                        String imageName = "pant2_" + color.toLowerCase();
                        ProductImage image = new ProductImage(
                            (int) pant2Id,
                            (int) variantId,
                            imageName,
                            color.equals("Black")
                        );
                        productRepository.insertImage(image);
                    }
                }
            }
        }

        // Pant 3: Quần jogger
        Pant pant3 = new Pant(
            "Quần Jogger Thể Thao",
            "Quần jogger thể thao năng động, co giãn 4 chiều",
            320000,
            CategoryType.PANT,
            "Polyester",
            "Dài",
            "Jogger"
        );
        long pant3Id = productRepository.insertProduct(pant3);
        
        if (pant3Id > 0) {
            String[] pant3Colors = {"Black", "Gray"};
            
            for (String color : pant3Colors) {
                for (String size : sizes) {
                    ProductVariant variant = new ProductVariant(
                        (int) pant3Id, color, size, 0
                    );
                    long variantId = productRepository.insertVariant(variant);
                    
                    if (size.equals("M")) {
                        String imageName = "pant3_" + color.toLowerCase();
                        ProductImage image = new ProductImage(
                            (int) pant3Id,
                            (int) variantId,
                            imageName,
                            color.equals("Black")
                        );
                        productRepository.insertImage(image);
                    }
                }
            }
        }
        
        Log.d(TAG, "Seeded pants successfully");
    }
}
