
import axios from 'axios';
import type { ScheduleResponseDto } from '../dto/schedule/ScheduleResponseDto';
import type { TravelMode } from '../enums/TravelMode';

const api = axios.create({
    baseURL: 'http://localhost:8080/api/v1',
    // baseURL: `${process.env.REACT_APP_URL}/api/v1/schedules`, // for prod
});

export const getSchedulesByTripId = async (tripId: number) => {
    const response = await api.get<ScheduleResponseDto[]>(`/schedules`, {
        params: { tripId }
    });
    return response.data;
};

export const resetScheduleTimes = async (tripId: number): Promise<void> => {
    try {
        await api.put('/schedules/reset-times', null, {
            params: { tripId }
        });
    } catch (error) {
        console.error('Помилка при обнуленні часу розкладу', error);
        throw error;
    }
};

export const createFullScheduleByTrip = async (
    tripId: number,
    travelMode: TravelMode,
    startTime: string // in "HH:mm" format
) => {
    try {
        const response = await api.post<ScheduleResponseDto[]>(
            `/schedules?tripId=${tripId}&travelMode=${travelMode}&startTime=${startTime}`
        );
        return response.data;
    } catch (error) {
        console.error('Error creating schedule', error);
        throw error;
    }
};
