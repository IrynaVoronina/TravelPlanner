import type {PlaceType} from "../../enums/PlaceType.ts";
import type {GoogleReviewDto} from "./GoogleReviewDto.ts";

export interface GooglePlaceDto {
    placeType: PlaceType;
    name: string;
    description: string;
    rating: number;
    openingTime: string;
    closingTime: string;
    reviews: GoogleReviewDto[];
    latitude: number;
    longitude: number;
}