export enum AccommodationType {
    HOTEL = 'HOTEL',
    APARTMENT = 'APARTMENT',
    HOSTEL = 'HOSTEL',
    GUEST_HOUSE = 'GUEST_HOUSE',
    OTHER = 'OTHER'
}

export const AccommodationTypeLabels: Record<AccommodationType, string> = {
    [AccommodationType.HOTEL]: 'Готель',
    [AccommodationType.APARTMENT]: 'Апартаменти',
    [AccommodationType.HOSTEL]: 'Хостел',
    [AccommodationType.GUEST_HOUSE]: 'Гостьовий будинок',
    [AccommodationType.OTHER]: 'Інше'
};
