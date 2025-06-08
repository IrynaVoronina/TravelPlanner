import axios from 'axios';
import type { MealBreakResponseDto } from '../dto/mealBreak/MealBreakResponseDto';
import type {MealBreakRequestDto} from "../dto/mealBreak/MealBreakRequestDto.ts";

const api = axios.create({
    baseURL: 'http://localhost:8080/api/v1/activities/',
});

// mealBreakService.ts
export const getMealBreakById = async (id: number) => {
    return await api.get<MealBreakResponseDto>(`/mealBreaks/${id}`);
};


export const getAllMealBreaksByTrip = async (tripId: number) => {
    try {
        const response = await api.get<MealBreakResponseDto[]>(`mealBreaks?tripId=${tripId}`);
        return await response.data;
    } catch (error) {
        console.error('Error fetching meal breaks by trip', error);
        throw error;
    }
};

// Add a new meal break to a trip
export const addMealBreakToTrip = async (
    tripId: number,
    mealBreakDto: MealBreakRequestDto
) => {
    try {
        const response = await api.post<MealBreakResponseDto>(`mealBreaks?tripId=${tripId}`, mealBreakDto);
        return response.data;
    } catch (error) {
        console.error('Error adding meal break', error);
        throw error;
    }
};

// Update an existing meal break
export const updateMealBreak = async (
    id: number,
    mealBreakDto: MealBreakRequestDto
) => {
    try {
        const response = await api.put<MealBreakResponseDto>(`mealBreaks/update/${id}`, mealBreakDto);
        return response.data;
    } catch (error) {
        console.error('Error updating meal break', error);
        throw error;
    }
};

// Delete a meal break from a trip
export const deleteMealBreak = async (
    tripId: number,
    mealBreakId: number
): Promise<void> => {
    try {
        await api.delete(`mealBreaks?tripId=${tripId}&mealBreakId=${mealBreakId}`);
    } catch (error) {
        console.error('Error deleting meal break', error);
        throw error;
    }
};
