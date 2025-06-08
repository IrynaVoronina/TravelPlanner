import axios from 'axios';
import type { AccommodationResponseDto } from '../dto/acommodation/AccommodationResponseDto';
import type {AccommodationRequestDto} from "../dto/acommodation/AccommodationRequestDto.ts";


const api = axios.create({
    baseURL: `http://localhost:8080/api/v1/activities/`,
});


export const getAccommodationById = async (id: number) => {
    return await api.get<AccommodationResponseDto>(`/accommodations/${id}`);
};

export const getAllAccommodationsByTrip = async (tripId: number) => {
    try {
        const response = await api.get<AccommodationResponseDto[]>(`accommodations?tripId=${tripId}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching accommodations for trip', error);
        throw error;
    }
};

export const AddAccommodationToTrip = async (
    tripId: number,
    accommodation: AccommodationRequestDto
)=> {
    try {
        const response = await api.post<AccommodationResponseDto>(`accommodations?tripId=${tripId}`, accommodation);
        return response.data;
    } catch (error) {
        console.error('Error adding accommodation', error);
        throw error;
    }
};

export const updateAccommodation = async (
    id: number,
    accommodation: AccommodationRequestDto
)=> {
    try {
        const response = await api.put<AccommodationResponseDto>(`accommodations/update/${id}`, accommodation);
        return response.data;
    } catch (error) {
        console.error('Error updating accommodation', error);
        throw error;
    }
};


// Delete accommodation
export const DeleteAccommodation = async (
    tripId: number,
    accommodationId: number
): Promise<void> => {
    try {
        await api.delete(`accommodations?tripId=${tripId}&accommodationId=${accommodationId}`);
    } catch (error) {
        console.error('Error deleting accommodation', error);
        throw error;
    }
};
