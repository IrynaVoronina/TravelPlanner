import React, { useState } from 'react';
import {
    Box,
    Button,
    Container,
    Paper,
    TextField,
    Typography
} from '@mui/material';
import axios from 'axios';
import type {WeatherRecommendationDTO} from "../../dto/weather/WeatherRecommendationDTO.ts";

const WeatherRecommendationsPage: React.FC = () => {
    const [city, setCity] = useState('');
    const [date, setDate] = useState('');
    const [result, setResult] = useState<WeatherRecommendationDTO | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleCheckWeather = async () => {
        if (!city || !date) return;

        setLoading(true);
        setError('');
        setResult(null);

        try {
            const response = await axios.post<WeatherRecommendationDTO>(
                'http://localhost:8080/api/v1/weather/check',
                null,
                {
                    params: {
                        city: city,
                        date: date
                    }
                }
            );
            setResult(response.data);
        } catch (e: any) {
            setError('Не вдалося отримати рекомендації');
            console.error(e);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container maxWidth="sm" sx={{ py: 6 }}>
            <Typography variant="h4" gutterBottom color="primary" fontWeight="bold">
                Погодні рекомендації
            </Typography>

            <TextField
                label="Місто"
                fullWidth
                value={city}
                onChange={(e) => setCity(e.target.value)}
                sx={{ mb: 2 }}
            />

            <TextField
                label="Дата подорожі"
                type="date"
                fullWidth
                InputLabelProps={{ shrink: true }}
                value={date}
                onChange={(e) => setDate(e.target.value)}
                sx={{ mb: 2 }}
            />

            <Button
                variant="contained"
                onClick={handleCheckWeather}
                disabled={!city || !date || loading}
            >
                {loading ? 'Перевірка...' : 'Перевірити погоду'}
            </Button>

            {error && (
                <Typography variant="body2" color="error" sx={{ mt: 2 }}>
                    {error}
                </Typography>
            )}

            {result !== null ? (
                <Paper sx={{ mt: 4, p: 3 }}>
                    <Typography variant="h6" gutterBottom>
                        {result.message}
                    </Typography>
                    <Typography variant="body2">Хмарність: {result.cloudCover}</Typography>
                    <Typography variant="body2">Імовірність опадів: {result.precipitationProbability}%</Typography>
                    <Typography variant="body2">Імовірність грози: {result.thunderstormProbability}%</Typography>
                    <Typography variant="body2">Опис: {result.description}</Typography>
                </Paper>
            ) : null}
        </Container>
    );
};

export default WeatherRecommendationsPage;
