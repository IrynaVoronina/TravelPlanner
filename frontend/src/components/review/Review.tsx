import React, { useEffect, useState } from 'react';
import {
    Box,
    Button,
    Container,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    IconButton,
    Rating,
    Stack,
    TextField,
    Typography
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { useLocation } from 'react-router-dom';
import { getAllPlacesByTripAsync } from '../../services/PlaceService';
import { createReview, getReviewsByPlaceId, updateReview, deleteReview } from '../../services/ReviewService';
import type { PlaceResponseDto } from '../../dto/places/PlaceResponseDto';
import type { ReviewRequestDto } from '../../dto/review/ReviewRequestDto';
import type { ReviewResponseDto } from '../../dto/review/ReviewResponseDto';

const Review = () => {
    const location = useLocation();
    const tripId = location.state?.tripId;

    const [places, setPlaces] = useState<PlaceResponseDto[]>([]);
    const [selectedPlaceId, setSelectedPlaceId] = useState<number | null>(null);
    const [reviewForm, setReviewForm] = useState<ReviewRequestDto>({ rating: 5, comment: '' });
    const [dialogOpen, setDialogOpen] = useState(false);
    const [editingReview, setEditingReview] = useState<ReviewResponseDto | null>(null);

    useEffect(() => {
        if (tripId) {
            refreshPlaces();
        }
    }, [tripId]);

    const refreshPlaces = async () => {
        const data = await getAllPlacesByTripAsync(tripId);
        const updated = await Promise.all(
            data.map(async (place) => {
                const reviews = await getReviewsByPlaceId(place.id);
                return { ...place, reviews };
            })
        );
        setPlaces(updated);
    };

    const handleOpenDialog = (placeId: number, review?: ReviewResponseDto) => {
        setSelectedPlaceId(placeId);
        if (review) {
            setEditingReview(review);
            setReviewForm({ rating: review.rating, comment: review.comment });
        } else {
            setEditingReview(null);
            setReviewForm({ rating: 5, comment: '' });
        }
        setDialogOpen(true);
    };

    const handleCloseDialog = () => {
        setDialogOpen(false);
        setEditingReview(null);
        setReviewForm({ rating: 5, comment: '' });
    };

    const handleSubmitReview = async () => {
        if (!selectedPlaceId) return;

        if (editingReview) {
            await updateReview(editingReview.id, reviewForm);
        } else {
            await createReview(selectedPlaceId, reviewForm);
        }

        handleCloseDialog();
        await refreshPlaces();
    };

    const handleDeleteReview = async (id: number) => {
        await deleteReview(id);
        await refreshPlaces();
    };

    const formatReviewTime = (raw: string): string => {
        if (!raw) return '';
        const [timePart] = raw.split('.');
        return timePart.slice(0, 5);
    };


    return (
        <Container maxWidth="md" sx={{ py: 4 }}>
            <Typography variant="h4" color="primary" gutterBottom>
                Відгуки до місць подорожі
            </Typography>
            <Stack spacing={3}>
                {places.map((place) => (
                    <Box
                        key={place.id}
                        sx={{ p: 3, border: '1px solid #ddd', borderRadius: 2, backgroundColor: '#f9f9f9' }}
                    >
                        <Typography variant="h6">{place.name}</Typography>
                        <Typography variant="body2" color="text.secondary">
                            {place.description || 'Опис відсутній'}
                        </Typography>
                        <Button
                            variant="outlined"
                            sx={{ mt: 1 }}
                            onClick={() => handleOpenDialog(place.id)}
                        >
                            Додати відгук
                        </Button>
                        <Stack spacing={1} sx={{ mt: 2 }}>
                            {place.reviews?.map((review: ReviewResponseDto) => (
                                <Box
                                    key={review.id}
                                    sx={{ border: '1px solid #ccc', borderRadius: 2, p: 2, bgcolor: '#fff' }}
                                >
                                    <Rating value={review.rating} readOnly size="small" />
                                    <Typography variant="body2">{review.comment}</Typography>
                                    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                        <Typography variant="caption" color="text.secondary">
                                            {formatReviewTime(review.timeDescription)}
                                        </Typography>
                                        <Box>
                                            <IconButton size="small" onClick={() => handleOpenDialog(place.id, review)}>
                                                <EditIcon fontSize="small" />
                                            </IconButton>
                                            <IconButton size="small" onClick={() => handleDeleteReview(review.id)}>
                                                <DeleteIcon fontSize="small" />
                                            </IconButton>
                                        </Box>
                                    </Box>
                                </Box>
                            ))}
                        </Stack>
                    </Box>
                ))}
            </Stack>

            <Dialog open={dialogOpen} onClose={handleCloseDialog} fullWidth maxWidth="sm">
                <DialogTitle>{editingReview ? 'Редагувати відгук' : 'Додати відгук'}</DialogTitle>
                <DialogContent>
                    <Stack spacing={2} sx={{ mt: 1 }}>
                        <Rating
                            value={reviewForm.rating}
                            onChange={(_, value) => setReviewForm({ ...reviewForm, rating: value ?? 0 })}
                        />
                        <TextField
                            label="Коментар"
                            multiline
                            rows={3}
                            fullWidth
                            value={reviewForm.comment}
                            onChange={(e) => setReviewForm({ ...reviewForm, comment: e.target.value })}
                        />
                    </Stack>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDialog}>Скасувати</Button>
                    <Button onClick={handleSubmitReview} variant="contained">
                        Зберегти
                    </Button>
                </DialogActions>
            </Dialog>
        </Container>
    );
};

export default Review;
