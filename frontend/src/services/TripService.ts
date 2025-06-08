import axios from 'axios';
import type {TripRequestDto} from "../dto/trip/TripRequestDto.ts";

const api = axios.create({
    baseURL: `http://localhost:8080/api/v1/trips`,
});

export const GetAllTripsAsync = async () => {
    try {
        const response = await api.get('');

        return await response.data;
    } catch (error) {
        console.error('Error fetching data', error);
        throw error;
    }
};


export const CreateTrip = async (createDTO: TripRequestDto) => {
    try {
        const response = await api.post('', createDTO);

        return response.data;
    } catch (error) {
        console.error('Error fetching data', error);
        throw error;
    }
};

export const updateTrip = async (tripId: number, updatedTrip: TripRequestDto) => {
    return await api.put('' + tripId,updatedTrip);
};

export const cloneTrip = async (tripId: number) => {
    return await api.post('/clone?tripId='+tripId);
};



export const deleteTripById = async (tripId: number) => {
    return await api.delete('' + tripId);
};