package com.tagme.tagme_store_back.domain.mapper;

import com.tagme.tagme_store_back.domain.dto.ShippingInfoDto;
import com.tagme.tagme_store_back.domain.model.ShippingInfo;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ShippingInfoMapper Tests")
class ShippingInfoMapperTest {

    @Nested
    @DisplayName("fromShippingInfoDtoToShippingInfo Tests")
    class FromShippingInfoDtoToShippingInfoTests {
        @Test
        @DisplayName("Debería devolver null cuando el DTO es null")
        void fromShippingInfoDtoToShippingInfo_NullInput_ReturnsNull() {
            assertNull(ShippingInfoMapper.fromShippingInfoDtoToShippingInfo(null));
        }

        @Test
        @DisplayName("Debería mapear correctamente DTO a modelo")
        void fromShippingInfoDtoToShippingInfo_ShouldMapCorrectly() {
            ShippingInfoDto dto = Instancio.of(ShippingInfoDto.class)
                    .withSeed(10)
                    .create();

            ShippingInfo shippingInfo = ShippingInfoMapper.fromShippingInfoDtoToShippingInfo(dto);

            assertNotNull(shippingInfo);
            assertAll(
                    () -> assertEquals(dto.id(), shippingInfo.getId()),
                    () -> assertEquals(dto.orderId(), shippingInfo.getOrderId()),
                    () -> assertEquals(dto.firstName(), shippingInfo.getFirstName()),
                    () -> assertEquals(dto.lastName(), shippingInfo.getLastName()),
                    () -> assertEquals(dto.email(), shippingInfo.getEmail()),
                    () -> assertEquals(dto.address(), shippingInfo.getAddress()),
                    () -> assertEquals(dto.city(), shippingInfo.getCity()),
                    () -> assertEquals(dto.postalCode(), shippingInfo.getPostalCode()),
                    () -> assertEquals(dto.country(), shippingInfo.getCountry()),
                    () -> assertEquals(dto.createdAt(), shippingInfo.getCreatedAt())
            );
        }
    }

    @Nested
    @DisplayName("fromShippingInfoToShippingInfoDto Tests")
    class FromShippingInfoToShippingInfoDtoTests {
        @Test
        @DisplayName("Debería devolver null cuando el modelo es null")
        void fromShippingInfoToShippingInfoDto_NullInput_ReturnsNull() {
            assertNull(ShippingInfoMapper.fromShippingInfoToShippingInfoDto(null));
        }

        @Test
        @DisplayName("Debería mapear correctamente modelo a DTO")
        void fromShippingInfoToShippingInfoDto_ShouldMapCorrectly() {
            ShippingInfo shippingInfo = Instancio.of(ShippingInfo.class)
                    .withSeed(10)
                    .create();

            ShippingInfoDto dto = ShippingInfoMapper.fromShippingInfoToShippingInfoDto(shippingInfo);

            assertNotNull(dto);
            assertAll(
                    () -> assertEquals(shippingInfo.getId(), dto.id()),
                    () -> assertEquals(shippingInfo.getOrderId(), dto.orderId()),
                    () -> assertEquals(shippingInfo.getFirstName(), dto.firstName()),
                    () -> assertEquals(shippingInfo.getLastName(), dto.lastName()),
                    () -> assertEquals(shippingInfo.getEmail(), dto.email()),
                    () -> assertEquals(shippingInfo.getAddress(), dto.address()),
                    () -> assertEquals(shippingInfo.getCity(), dto.city()),
                    () -> assertEquals(shippingInfo.getPostalCode(), dto.postalCode()),
                    () -> assertEquals(shippingInfo.getCountry(), dto.country()),
                    () -> assertEquals(shippingInfo.getCreatedAt(), dto.createdAt())
            );
        }
    }
}
