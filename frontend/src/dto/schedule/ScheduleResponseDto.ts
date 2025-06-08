import type { ActivityResponseDto } from '../activity/ActivityResponseDto';
import type { TripResponseDto } from '../trip/TripResponseDto';

export interface ScheduleResponseDto {
    id: number;
    startTime: string;
    endTime: string;  
    trip: TripResponseDto;
    activity: ActivityResponseDto;
}