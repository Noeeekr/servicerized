package com.github.noeeekr.servicerized.product.repository.models;

import java.util.Random;
import com.github.f4b6a3.uuid.UuidCreator;
import com.github.noeeekr.servicerized.product.repository.models.entities.ProductEntity;

public class ProductEntityTest {
    public class Mock {
        public static ProductEntity createSingle() {
            String productDescription = RandomUtil.getFourByteLongString("product-description");
            String productName = RandomUtil.getFourByteLongString("product-name");

            Integer productPrice = new Random().nextInt(0, 1000);


            return new ProductEntity(UuidCreator.getTimeOrderedEpochFast(),
                    UuidCreator.getTimeOrderedEpochFast(), productName, productPrice,
                    productDescription);

        }
    }


    public class RandomUtil {
        /**
         * getFourByteLongString() creates a four byte long string, containing random letters plus
         * the identifier in the beginning or the string. Take in mind, the bigger the identifier
         * is, the less space there is for random letters.
         * 
         * @param identifier A identifier to improve value readability. Ommited if "".
         * @return a 4 byte long string.
         */
        public static String getFourByteLongString(String identifier) {
            StringBuilder stringBuilder = new StringBuilder();

            if (identifier != "") {
                identifier = identifier + "-";
                stringBuilder.append(identifier);
            }

            Integer variadicLength = 64 - identifier.length();
            for (int i = 0; i < variadicLength; i++) {
                Integer letter = new Random().nextInt(88, 115);
                stringBuilder.append(letter);
            }
            return stringBuilder.toString();
        }

        /**
         * getFourByteLongString() creates a four byte long string, in the following format:
         * "random-...".
         * 
         * @return a 4 byte long string.
         */
        public static String getFourByteLongString() {
            return RandomUtil.getFourByteLongString("random");
        }
    }
}
