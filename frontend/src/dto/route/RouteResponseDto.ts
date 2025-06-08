import type { Location } from "./Location";
import type { RouteSegmentResponseDto } from "./RouteSegmentResponseDto";

export interface RouteResponseDto {
    id: number;
    totalDuration: number;
    startPlace: Location;
    endPlace: Location;
    segments: RouteSegmentResponseDto[];
}