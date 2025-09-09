package com.supermarket.util;

import com.supermarket.exception.SupermarketException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import util.TestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ItemRequestValidatorTest {

    @Test
    void validate_validRequestWithOffer_ok() {
        assertDoesNotThrow(() -> ItemRequestValidator.validate(List.of(TestUtils.buildItemRequestWithAllFields())));
    }

    @Test
    void validate_validRequestWithoutOffer_ok() {
        assertDoesNotThrow(() -> ItemRequestValidator.validate(List.of(TestUtils.buildItemRequestWithoutOffer())));
    }

    @Test
    void validate_nullQuantity_error() {
        SupermarketException supermarketException = assertThrows(SupermarketException.class,
                () -> ItemRequestValidator.validate(List.of(TestUtils.buildItemRequestWithOfferFields(null, 15.0,
                LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 7)))));
        assertEquals("Apple" + ItemRequestValidator.OFFER_ERROR_MESSAGE, supermarketException.getMessage());
    }

    @Test
    void validate_nullOfferPrice_error() {
        SupermarketException supermarketException = assertThrows(SupermarketException.class,
                () -> ItemRequestValidator.validate(List.of(TestUtils.buildItemRequestWithOfferFields(3, null,
                        LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 7)))));
        assertEquals("Apple" + ItemRequestValidator.OFFER_ERROR_MESSAGE, supermarketException.getMessage());
    }

    @Test
    void validate_nullOfferStartDate_error() {
        SupermarketException supermarketException = assertThrows(SupermarketException.class,
                () -> ItemRequestValidator.validate(List.of(TestUtils.buildItemRequestWithOfferFields(3, 15.0,
                        null, LocalDate.of(2025, 9, 7)))));
        assertEquals("Apple" + ItemRequestValidator.OFFER_ERROR_MESSAGE, supermarketException.getMessage());
    }

    @Test
    void validate_nullOfferEndDate_error() {
        SupermarketException supermarketException = assertThrows(SupermarketException.class,
                () -> ItemRequestValidator.validate(List.of(TestUtils.buildItemRequestWithOfferFields(3, 15.0,
                        LocalDate.of(2025, 9, 1), null))));
        assertEquals("Apple" + ItemRequestValidator.OFFER_ERROR_MESSAGE, supermarketException.getMessage());
    }

    @Test
    void validate_nullOfferEndDateSameAsStartDate_error() {
        SupermarketException supermarketException = assertThrows(SupermarketException.class,
                () -> ItemRequestValidator.validate(List.of(TestUtils.buildItemRequestWithOfferFields(3, 15.0,
                        LocalDate.now(), LocalDate.now()))));
        assertEquals("Apple" + ItemRequestValidator.OFFER_ERROR_MESSAGE, supermarketException.getMessage());
    }

    @Test
    void validate_nullOfferEndDateBeforeStartDate_error() {
        SupermarketException supermarketException = assertThrows(SupermarketException.class,
                () -> ItemRequestValidator.validate(List.of(TestUtils.buildItemRequestWithOfferFields(3, 15.0,
                        LocalDate.now(), LocalDate.now().minusDays(1)))));
        assertEquals("Apple" + ItemRequestValidator.OFFER_ERROR_MESSAGE, supermarketException.getMessage());
    }

    @Test
    void validate_oneValidAndOneInvalidItem_error() {
        SupermarketException supermarketException = assertThrows(SupermarketException.class,
                () -> ItemRequestValidator.validate(List.of(
                        TestUtils.buildItemRequestWithoutOffer(),
                        TestUtils.buildItemRequestWithOfferFields(null, 15.0,
                        LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 7)))));
        assertEquals("Apple" + ItemRequestValidator.OFFER_ERROR_MESSAGE, supermarketException.getMessage());
    }


}