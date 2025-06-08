import axios from 'axios';
import type {PlaceType} from "../enums/PlaceType.ts";
import type {GooglePlaceDto} from "../dto/google/GooglePlaceDto.ts";
import type {PlaceResponseDto} from "../dto/places/PlaceResponseDto.ts";;

const api = axios.create({
    baseURL: `http://localhost:8080/api/v1/activities/`,
});

export const GetPlacesNearbyAsync = async(city: string, placeType: PlaceType) => {
    try {
        const response = await api.get<GooglePlaceDto[]>('places/nearby?city='+city+'&type='+placeType.toString());

        return response.data;
    } catch (error) {
        console.error('Error fetching data', error);
        throw error;
    }
};

export const getPlaceById = async (placeId: number) => {
    return await api.get<PlaceResponseDto>(`places/${placeId}`);
};


export const getAllPlacesByTripAsync = async(tripId: number) => {
    try {
        const response = await api.get<PlaceResponseDto[]>('places?tripId='+tripId);

        return response.data;
    } catch (error) {
        console.error('Error fetching data', error);
        throw error;
    }
};

export const AddPlaceToTrip = async ( placeDto: GooglePlaceDto, tripId: number) => {
    try {
        const response = await api.post<PlaceResponseDto>('places?tripId=' + tripId, placeDto);

        return response.data;
    } catch (error) {
        console.error('Error fetching data', error);
        throw error;
    }
};

export const updatePlaceVisitDuration = async (placeId: number, visitDuration: number) => {
    try {
        const response = await api.put<PlaceResponseDto>('places?placeId=' + placeId + '&visitDuration=' + visitDuration);

        return response.data;
    } catch (error) {
        console.error('Error fetching data', error);
        throw error;
    }
};

export const DeletePlace = async (tripId: number, placeId: number) => {
    try {
        const response = await api.delete('places?tripId=' + tripId + '&placeId=' + placeId);

        return response.data;
    } catch (error) {
        console.error('Error fetching data', error);
        throw error;
    }
};