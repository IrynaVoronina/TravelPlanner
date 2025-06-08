import React, { useEffect, useRef, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { getRoutesByTripId } from '../../services/routeService';
import type { RouteResponseDto } from '../../dto/route/RouteResponseDto';
import {
    Box,
    Container,
    Typography,
    Paper,
    Stack,
    Divider,
    CircularProgress,
} from '@mui/material';
import {TravelModeLabels} from "../../enums/TravelMode.ts";

const GOOGLE_MAPS_API_KEY = 'GOOGLE_MAPS_API_KEY';

declare global {
    interface Window {
        initMap: () => void;
    }
}

const Route = () => {
    const location = useLocation();
    const tripId = location.state?.tripId;
    const mapRefs = useRef<(HTMLDivElement | null)[]>([]);
    const [routes, setRoutes] = useState<RouteResponseDto[]>([]);
    const [loading, setLoading] = useState(true);
    const [apiLoaded, setApiLoaded] = useState(false);

    useEffect(() => {
        loadGoogleMapsScript();
    }, []);

    useEffect(() => {
        if (tripId && apiLoaded) {
            fetchRoutes(tripId);
        }
    }, [tripId, apiLoaded]);

    const loadGoogleMapsScript = () => {
        if (typeof window.google === 'undefined') {
            const script = document.createElement('script');
            script.src = `https://maps.googleapis.com/maps/api/js?key=${GOOGLE_MAPS_API_KEY}`;
            script.async = true;
            script.defer = true;
            script.onload = () => setApiLoaded(true);
            document.head.appendChild(script);
        } else {
            setApiLoaded(true);
        }
    };

    const fetchRoutes = async (tripId: number) => {
        try {
            const data = await getRoutesByTripId(tripId);
            setRoutes(data);
        } catch (error) {
            console.error('Failed to load routes', error);
        } finally {
            setLoading(false);
        }
    };
    const formatDuration = (seconds: number): string => {
        const h = Math.floor(seconds / 3600);
        const m = Math.floor((seconds % 3600) / 60);
        const s = seconds % 60;

        const parts = [];
        if (h > 0) parts.push(`${h} год`);
        if (m > 0) parts.push(`${m} хв`);
        if (s > 0 || (h === 0 && m === 0)) parts.push(`${s} сек`);
        return parts.join(' ');
    };

    const renderMap = (route: RouteResponseDto, index: number) => {
        if (!window.google || !mapRefs.current[index]) return;

        const map = new window.google.maps.Map(mapRefs.current[index]!, {
            center: {
                lat: route.startPlace.latitude,
                lng: route.startPlace.longitude,
            },
            zoom: 13,
        });

        const bounds = new window.google.maps.LatLngBounds();

        const startMarker = new window.google.maps.Marker({
            position: {
                lat: route.startPlace.latitude,
                lng: route.startPlace.longitude,
            },
            map,
            label: 'A',
        });

        const endMarker = new window.google.maps.Marker({
            position: {
                lat: route.endPlace.latitude,
                lng: route.endPlace.longitude,
            },
            map,
            label: 'B',
        });

        bounds.extend(startMarker.getPosition()!);
        bounds.extend(endMarker.getPosition()!);

        const segmentPath = route.segments.map(seg => ({
            lat: seg.startPoint.latitude,
            lng: seg.startPoint.longitude,
        })).concat({
            lat: route.endPlace.latitude,
            lng: route.endPlace.longitude,
        });

        const polyline = new window.google.maps.Polyline({
            path: segmentPath,
            geodesic: true,
            strokeColor: '#2196f3',
            strokeOpacity: 1.0,
            strokeWeight: 3,
        });

        polyline.setMap(map);
        segmentPath.forEach(p => bounds.extend(p));
        map.fitBounds(bounds);
    };

    useEffect(() => {
        if (routes.length && apiLoaded) {
            routes.forEach((route, i) => {
                setTimeout(() => renderMap(route, i), 100); // slight delay
            });
        }
    }, [routes, apiLoaded]);

    return (
        <Container maxWidth="md" sx={{ py: 5 }}>
            <Typography variant="h4" fontWeight="bold" color="primary" sx={{ mb: 3 }}>
                Маршрути
            </Typography>

            {loading ? (
                <Box textAlign="center" mt={4}>
                    <CircularProgress />
                    <Typography mt={2}>Завантаження...</Typography>
                </Box>
            ) : routes.length === 0 ? (
                <Typography textAlign="center">Немає маршрутів для цієї подорожі.</Typography>
            ) : (
                <Stack spacing={4}>
                    {routes.map((route, index) => (
                        <Paper key={route.id} elevation={3} sx={{ p: 3, borderRadius: 2 }}>
                            <Typography variant="h6" fontWeight="bold" color="primary">
                                Маршрут {index + 1}
                            </Typography>
                            <Typography variant="body2" sx={{ mb: 1 }}>
                                Тривалість: {formatDuration(route.totalDuration)}
                            </Typography>
                            <Box
                                ref={(el) => (mapRefs.current[index] = el)}
                                sx={{
                                    width: '100%',
                                    height: 300,
                                    mt: 2,
                                    borderRadius: 2,
                                    border: '1px solid #ccc',
                                }}
                            />
                            <Divider sx={{ my: 2 }} />
                            <Typography variant="subtitle1" fontWeight="bold">
                                Відрізки маршруту:
                            </Typography>
                            {route.segments.map((seg, i) => (
                                <Typography key={i} variant="body2">
                                    {i + 1}. {TravelModeLabels[seg.travelMode]}, {formatDuration(seg.staticDuration)}, {seg.distanceMeters} м
                                </Typography>
                            ))}
                        </Paper>
                    ))}
                </Stack>
            )}
        </Container>
    );
};

export default Route;
