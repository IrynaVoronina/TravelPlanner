import React, { useEffect, useState } from 'react';
import {
    createFullScheduleByTrip,
    getSchedulesByTripId, resetScheduleTimes
} from '../../services/scheduleService';
import type { ScheduleResponseDto } from '../../dto/schedule/ScheduleResponseDto';
import {TravelMode, TravelModeLabels} from '../../enums/TravelMode';
import {
    Box,
    Button,
    Container,
    Paper,
    Stack,
    Typography,
    Divider,
    CircularProgress,
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    TextField
} from '@mui/material';
import DirectionsIcon from '@mui/icons-material/Directions';
import ScheduleIcon from '@mui/icons-material/Schedule';
import { useLocation } from 'react-router-dom';
import { Link } from 'react-router-dom';

const Schedule = () => {
    const location = useLocation();
    const tripId = location.state?.tripId;
    const [schedules, setSchedules] = useState<ScheduleResponseDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [startTime, setStartTime] = useState('11:00');
    const [travelMode, setTravelMode] = useState<TravelMode>(TravelMode.TRANSIT);
    const [hasValidTimes, setHasValidTimes] = useState(false);

    useEffect(() => {
        if (tripId) {
            loadSchedules(tripId);
        } else {
            setLoading(false);
        }
    }, [tripId]);

    const loadSchedules = async (tripId: number) => {
        try {
            const data = await getSchedulesByTripId(tripId);
            setSchedules(data);
            const allHaveTimes = data.every(s => s.startTime && s.endTime);
            setHasValidTimes(allHaveTimes);
        } catch (error) {
            console.error('Не вдалося завантажити розклад', error);
        } finally {
            setLoading(false);
        }
    };

    const handleGenerateSchedule = async () => {
        if (!tripId) return;
        try {
            setLoading(true);
            const data = await createFullScheduleByTrip(tripId, travelMode, startTime);
            setSchedules(data);
            setHasValidTimes(true);
        } catch (error) {
            console.error('Не вдалося згенерувати розклад', error);
        } finally {
            setLoading(false);
        }
    };

    const handleResetTime = async () => {
        if (!tripId) return;
        try {
            await resetScheduleTimes(tripId);
        } catch (error) {
            console.error('Не вдалося скинути часи для розкладу', error);
        }
    };

    return (
        <Container maxWidth="md" sx={{ py: 5 }}>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 4 }}>
                <ScheduleIcon color="primary" sx={{ fontSize: 40 }} />
                <Typography variant="h4" fontWeight="bold" color="primary">
                    Розклад подорожі
                </Typography>
            </Box>
            <Box
                sx={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'flex-start',
                    flexWrap: 'wrap',
                    gap: 3,
                    mb: 4
                }}
            >
                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                    <Button
                        variant="contained"
                        component={Link}
                        to="/trips"
                        color="primary"
                        onClick={handleResetTime}
                        sx={{
                            borderRadius: 2,
                            px: 3,
                            py: 1.5,
                            textTransform: 'none',
                            fontSize: '0.9rem',
                            boxShadow: 2,
                            '&:hover': { boxShadow: 4 }
                        }}
                    >
                        Редагувати подорож
                    </Button>
                    {hasValidTimes && (
                        <Button
                            variant="outlined"
                            color="primary"
                            component={Link}
                            to="/review"
                            state={{ tripId }}
                            sx={{
                                borderRadius: 2,
                                px: 3,
                                py: 1.5,
                                textTransform: 'none',
                                fontSize: '0.9rem'
                            }}
                        >
                            Залишити відгук
                        </Button>
                    )}
                </Box>

                <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: 2 }}>
                    <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap', justifyContent: 'flex-end' }}>
                        <TextField
                            label="Час початку"
                            type="time"
                            value={startTime}
                            onChange={(e) => setStartTime(e.target.value)}
                            InputLabelProps={{ shrink: true }}
                        />
                        <FormControl>
                            <InputLabel>Тип пересування</InputLabel>
                            <Select
                                value={travelMode}
                                label="Тип пересування"
                                onChange={(e) => setTravelMode(e.target.value as TravelMode)}
                            >
                                {Object.values(TravelMode).map(mode => (
                                    <MenuItem key={mode} value={mode}>
                                        {TravelModeLabels[mode]}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                        <Button
                            variant="contained"
                            startIcon={<DirectionsIcon />}
                            onClick={handleGenerateSchedule}
                            sx={{ textTransform: 'none', borderRadius: 2 }}
                        >
                            Згенерувати розклад
                        </Button>

                    </Box>

                    {hasValidTimes && (
                        <Button
                            variant="contained"
                            component={Link}
                            to="/route"
                            state={{ tripId }}
                            color="primary"
                            sx={{
                                borderRadius: 2,
                                px: 3,
                                py: 1.5,
                                textTransform: 'none',
                                fontSize: '0.9rem',
                                boxShadow: 2,
                                '&:hover': { boxShadow: 4 }
                            }}
                        >
                            Переглянути маршрути
                        </Button>
                    )}
                </Box>
            </Box>

            {loading ? (
                <Box textAlign="center" mt={4}>
                    <CircularProgress />
                    <Typography mt={2}>Завантаження...</Typography>
                </Box>
            ) : tripId && schedules.length > 0 && hasValidTimes ? (
                <Stack spacing={3}>
                    {schedules.map((s, index) => (
                        <Paper key={s.id} elevation={3} sx={{ p: 3, borderRadius: 2 }}>
                            <Typography variant="h6" fontWeight="bold" color="primary">
                                {index + 1}. {s.activity.name}
                            </Typography>
                            <Divider sx={{ my: 1 }} />
                            <Typography variant="body2">
                                <strong>Час:</strong> {s.startTime} – {s.endTime}
                            </Typography>
                            {s.activity.description && (
                                <Typography variant="body2" sx={{ mt: 1 }}>
                                    <strong>Інформація:</strong> {s.activity.description}
                                </Typography>
                            )}
                        </Paper>
                    ))}
                </Stack>
            ) : (
                <Box textAlign="center">
                    <Typography variant="body1" sx={{ mb: 2 }}>
                        Розклад ще не створено.
                    </Typography>
                </Box>
            )}
        </Container>
    );
};

export default Schedule;