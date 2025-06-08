import type { AccommodationType } from '../../enums/AccommodationType';

export interface AccommodationRequestDto {
    name: string;
    description: string;
    accommodationType: AccommodationType;
    pricePerNight: number;
    stars: number;
    wifiAvailable: boolean;
    parkingAvailable: boolean;
    petFriendly: boolean;
}
