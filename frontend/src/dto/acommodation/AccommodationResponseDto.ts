import type { AccommodationType } from '../../enums/AccommodationType';

export interface AccommodationResponseDto {
    id: number;
    name: string;
    description: string;
    accommodationType: AccommodationType;
    pricePerNight: number;
    stars: number;
    wifiAvailable: boolean;
    parkingAvailable: boolean;
    petFriendly: boolean;
    latitude: number;
    longitude: number;
}
