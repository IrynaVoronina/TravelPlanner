export enum PlaceType {
    CAFE = 'CAFE',
    MUSEUM = 'MUSEUM',
    GALLERY = 'GALLERY',
    CHURCH = 'CHURCH',
    PARK = 'PARK',
    BEACH = 'BEACH',
    AMUSEMENT_PARK = 'AMUSEMENT_PARK',
    ZOO = 'ZOO',
    SHOPPING_MALL = 'SHOPPING_MALL',
    OTHER = 'OTHER'
}

export const PlaceTypeLabels: Record<PlaceType, string> = {
    [PlaceType.CAFE]: 'Кафе',
    [PlaceType.MUSEUM]: 'Музей',
    [PlaceType.GALLERY]: 'Галерея',
    [PlaceType.CHURCH]: 'Церква',
    [PlaceType.PARK]: 'Парк',
    [PlaceType.BEACH]: 'Пляж',
    [PlaceType.AMUSEMENT_PARK]: 'Парк розваг',
    [PlaceType.ZOO]: 'Зоопарк',
    [PlaceType.SHOPPING_MALL]: 'Торговий центр',
    [PlaceType.OTHER]: 'Інше',
};
