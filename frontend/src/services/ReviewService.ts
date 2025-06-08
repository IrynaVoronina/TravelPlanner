import axios from 'axios';
import type { ReviewRequestDto } from '../dto/review/ReviewRequestDto';
import type { ReviewResponseDto } from '../dto/review/ReviewResponseDto';

const api = axios.create({
    baseURL: 'http://localhost:8080/api/v1'
});

export const getReviewsByPlaceId = async (placeId: number) => {
    const response = await api.get<ReviewResponseDto[]>(`/reviews`, {
        params: { placeId }
    });
    return response.data;
};

export const createReview = async (placeId: number, review: ReviewRequestDto) => {
    const response = await api.post<ReviewResponseDto>(`/reviews?placeId=${placeId}`, review);
    return response.data;
};

export const updateReview = async (id: number, dto: ReviewRequestDto) => {
    const res = await api.put<ReviewResponseDto>(`/reviews/${id}`, dto);
    return res.data;
};

export const deleteReview = async (id: number) => {
    await api.delete(`/reviews/${id}`);
};
