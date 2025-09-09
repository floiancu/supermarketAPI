package util;

import com.supermarket.dto.ItemRequest;

import java.time.LocalDate;

public class TestUtils {

    public static ItemRequest buildItemRequestWithAllFields() {
        return new ItemRequest("Apple", 20.0, 2, 15.0,
                LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 7));
    }

    public static ItemRequest buildItemRequestWithPrice(Double price) {
        return new ItemRequest("Apple", price, 2, 15.0,
                LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 7));
    }

    public static ItemRequest buildItemRequestWithoutOffer() {
        return new ItemRequest("Apricot", 10.0, null, null, null, null);
    }

    public static ItemRequest buildItemRequestWithOfferFields(Integer offerQuantity, Double offerPrice, LocalDate offerStartDate, LocalDate offerEndDate) {
        return new ItemRequest("Apple", 20.0, offerQuantity, offerPrice, offerStartDate, offerEndDate);
    }
}
