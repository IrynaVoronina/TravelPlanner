import axios from 'axios';
import type { RouteResponseDto } from '../dto/route/RouteResponseDto';


const api = axios.create({
    baseURL: 'http://localhost:8080/api/v1',
});

export const getRoutesByTripId = async (tripId: number) => {
    const response = await api.get<RouteResponseDto[]>(`/routes`, {
        params: { tripId },
    });
    return response.data;
};
