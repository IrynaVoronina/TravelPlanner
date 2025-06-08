package com.example.backend.model.enums;

import lombok.Getter;

@Getter
public enum PlaceType {

    CAFE(90),

    MUSEUM(90),
    GALLERY(120),
    CHURCH(60),

    PARK(120),
    BEACH(180),

    AMUSEMENT_PARK(180),
    ZOO(150),
    SHOPPING_MALL(120),

    OTHER(60);

    private final int defaultVisitDuration;

    PlaceType(int defaultVisitDuration) {
        this.defaultVisitDuration = defaultVisitDuration;
    }
}


