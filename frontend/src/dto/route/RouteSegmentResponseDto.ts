import type { Location } from "./Location";
import { TravelMode } from "../../enums/TravelMode";

export interface RouteSegmentResponseDto {
    startPoint: Location;
    endPoint: Location;
    staticDuration: number;
    distanceMeters: number;
    travelMode: TravelMode;
}