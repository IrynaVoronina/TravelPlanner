import React, {useState, useEffect, useRef} from 'react';
import {
    Container,
    Typography,
    Box,
    Paper,
    Button,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Select,
    MenuItem,
    FormControl,
    InputLabel,
    Card,
    CardContent,
    CardActions,
    Grid,
    Chip,
    IconButton,
    List,
    ListItem,
    ListItemText,
    ListItemSecondaryAction,
    Divider,
    Rating,
    Stack,
    FormControlLabel,
    Checkbox,
} from '@mui/material';
import {LocalizationProvider} from '@mui/x-date-pickers/LocalizationProvider';
import {AdapterDateFns} from '@mui/x-date-pickers/AdapterDateFns';
import {TimePicker} from '@mui/x-date-pickers/TimePicker';
import {useLocation} from 'react-router-dom';
import {Link} from 'react-router-dom';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import HotelIcon from '@mui/icons-material/Hotel';
import RestaurantIcon from '@mui/icons-material/Restaurant';
import PlaceIcon from '@mui/icons-material/Place';
import SearchIcon from '@mui/icons-material/Search';
import DeleteIcon from '@mui/icons-material/Delete';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import MapIcon from '@mui/icons-material/Map';
import EditIcon from '@mui/icons-material/Edit';
import VisibilityIcon from '@mui/icons-material/Visibility';

import {
    GetPlacesNearbyAsync,
    AddPlaceToTrip,
    getAllPlacesByTripAsync,
    DeletePlace,
    updatePlaceVisitDuration, getPlaceById
} from '../../services/PlaceService';
import {
    AddAccommodationToTrip,
    getAllAccommodationsByTrip,
    DeleteAccommodation,
    updateAccommodation, getAccommodationById
} from '../../services/AcommodationService.ts';
import {
    addMealBreakToTrip,
    getAllMealBreaksByTrip,
    deleteMealBreak,
    updateMealBreak, getMealBreakById
} from '../../services/MealBreakService';

import type {TripResponseDto} from '../../dto/trip/TripResponseDto.ts';
import {PlaceType, PlaceTypeLabels} from '../../enums/PlaceType';
import {AccommodationType, AccommodationTypeLabels} from '../../enums/AccommodationType';
import {MealBreakType, MealBreakTypeLabels} from '../../enums/MealBreakType';
import type {GooglePlaceDto} from '../../dto/google/GooglePlaceDto';
import type {AccommodationRequestDto} from '../../dto/acommodation/AccommodationRequestDto';
import type {AccommodationResponseDto} from '../../dto/acommodation/AccommodationResponseDto';
import type {MealBreakRequestDto} from '../../dto/mealBreak/MealBreakRequestDto';
import type {MealBreakResponseDto} from '../../dto/mealBreak/MealBreakResponseDto';
import type {PlaceResponseDto} from '../../dto/places/PlaceResponseDto';
import type {GoogleReviewDto} from '../../dto/google/GoogleReviewDto';

declare global {
    interface Window {
        google: any;
        initMap: () => void;
    }
}

function TripDetails() {
    const mapRef = useRef<HTMLDivElement | null>(null);
    const googleMapRef = useRef<any>(null);
    const markersRef = useRef<any[]>([]);
    const [showMapView, setShowMapView] = useState(false);

    const location = useLocation();
    const trip = location.state as TripResponseDto;

    const [places, setPlaces] = useState<PlaceResponseDto[]>([]);
    const [accommodations, setAccommodations] = useState<AccommodationResponseDto[]>([]);
    const [mealBreaks, setMealBreaks] = useState<MealBreakResponseDto[]>([]);

    const [addPlaceModalOpen, setAddPlaceModalOpen] = useState(false);
    const [addAccommodationModalOpen, setAddAccommodationModalOpen] = useState(false);
    const [addMealBreakModalOpen, setAddMealBreakModalOpen] = useState(false);

    const [selectedPlaceType, setSelectedPlaceType] = useState<PlaceType>(PlaceType.CAFE);
    const [nearbyPlaces, setNearbyPlaces] = useState<GooglePlaceDto[]>([]);
    const [loadingNearbyPlaces, setLoadingNearbyPlaces] = useState(false);

    const [selectedPlace, setSelectedPlace] = useState<PlaceResponseDto | null>(null);
    const [viewPlaceModalOpen, setViewPlaceModalOpen] = useState(false);

    const [editingPlace, setEditingPlace] = useState<PlaceResponseDto | null>(null);
    const [editModalOpen, setEditModalOpen] = useState(false);

    const [selectedAccommodation, setSelectedAccommodation] = useState<AccommodationResponseDto | null>(null);
    const [viewAccommodationModalOpen, setViewAccommodationModalOpen] = useState(false);

    const [editingAccommodation, setEditingAccommodation] = useState<AccommodationResponseDto | null>(null);
    const [editAccommodationModalOpen, setEditAccommodationModalOpen] = useState(false);

    const [selectedMealBreak, setSelectedMealBreak] = useState<MealBreakResponseDto | null>(null);
    const [viewMealBreakModalOpen, setViewMealBreakModalOpen] = useState(false);

    const [editingMealBreak, setEditingMealBreak] = useState<MealBreakResponseDto | null>(null);
    const [editMealBreakModalOpen, setEditMealBreakModalOpen] = useState(false);

    const [accommodationForm, setAccommodationForm] = useState<AccommodationRequestDto>({
        name: '',
        description: '',
        accommodationType: AccommodationType.HOTEL,
        pricePerNight: 0,
        stars: 5,
        wifiAvailable: false,
        parkingAvailable: false,
        petFriendly: false
    });

    const [mealBreakForm, setMealBreakForm] = useState<MealBreakRequestDto>({
        name: MealBreakType.LUNCH,
        description: '',
        startTime: '13:00',
        endTime: '14:00'
    });


    useEffect(() => {
        if (trip?.id) {
            loadTripData();
        }
    }, [trip]);

    useEffect(() => {
        const loadGoogleMaps = () => {
            if (window.google) return;

            const script = document.createElement('script');
            script.src = `https://maps.googleapis.com/maps/api/js?key=GOOGLE_MAPS_API_KEY=maps,marker&v=weekly`;
            script.async = true;
            script.defer = true;
            document.head.appendChild(script);
        };

        loadGoogleMaps();
    }, []);

    const initializeMap = () => {
        if (!mapRef.current || !window.google || nearbyPlaces.length === 0) return;

        if (googleMapRef.current) {
            googleMapRef.current = null;
            markersRef.current.forEach(marker => marker.setMap(null));
            markersRef.current = [];
        }

        const center = {
            lat: nearbyPlaces.reduce((sum, p) => sum + p.latitude, 0) / nearbyPlaces.length,
            lng: nearbyPlaces.reduce((sum, p) => sum + p.longitude, 0) / nearbyPlaces.length,
        };

        const map = new window.google.maps.Map(mapRef.current, {
            zoom: 13,
            center,
        });

        googleMapRef.current = map;

        nearbyPlaces.forEach((place) => {
            const marker = new window.google.maps.Marker({
                position: { lat: place.latitude, lng: place.longitude },
                map,
                title: place.name,
            });

            marker.addListener('click', () => {
                handleAddPlaceToTrip(place);
            });

            markersRef.current.push(marker);
        });
    };

    useEffect(() => {
        if (showMapView && nearbyPlaces.length > 0) {
            initializeMap();
        }
    }, [showMapView, nearbyPlaces]);


    const loadTripData = async () => {
        try {
            const [placesData, accommodationsData, mealBreaksData] = await Promise.all([
                getAllPlacesByTripAsync(trip.id),
                getAllAccommodationsByTrip(trip.id),
                getAllMealBreaksByTrip(trip.id)
            ]);
            setPlaces(placesData);
            setAccommodations(accommodationsData);
            setMealBreaks(mealBreaksData);
        } catch (error) {
            console.error('Error loading trip data:', error);
        }
    };

    const handleSearchNearbyPlaces = async () => {
        if (!trip?.city) return;

        setLoadingNearbyPlaces(true);
        try {
            const results = await GetPlacesNearbyAsync(trip.city, selectedPlaceType);
            setNearbyPlaces(results);
        } catch (error) {
            console.error('Error fetching nearby places:', error);
        } finally {
            setLoadingNearbyPlaces(false);
        }
    };

    const handleAddPlaceToTrip = async (place: GooglePlaceDto) => {
        try {
            await AddPlaceToTrip(place, trip.id);
            await loadTripData();
        } catch (error) {
            console.error('Error adding place to trip:', error);
        }
    };

    const handleAddAccommodation = async () => {
        try {
            await AddAccommodationToTrip(trip.id, accommodationForm);
            setAddAccommodationModalOpen(false);
            setAccommodationForm({
                name: '',
                description: '',
                accommodationType: AccommodationType.HOTEL,
                pricePerNight: 0,
                stars: 1,
                wifiAvailable: false,
                parkingAvailable: false,
                petFriendly: false
            });
            await loadTripData();
        } catch (error) {
            console.error('Error adding accommodation:', error);
        }
    };

    const handleAddMealBreak = async () => {
        try {
            await addMealBreakToTrip(trip.id, mealBreakForm);
            setAddMealBreakModalOpen(false);
            setMealBreakForm({
                name: MealBreakType.BREAKFAST,
                description: '',
                startTime: '08:00',
                endTime: '09:00'
            });
            await loadTripData();
        } catch (error) {
            console.error('Error adding meal break:', error);
        }
    };

    const handleDeletePlace = async (placeId: number) => {
        try {
            await DeletePlace(trip.id, placeId);
            await loadTripData();
        } catch (error) {
            console.error('Error deleting place:', error);
        }
    };

    const handleDeleteAccommodation = async (accommodationId: number) => {
        try {
            await DeleteAccommodation(trip.id, accommodationId);
            await loadTripData();
        } catch (error) {
            console.error('Error deleting accommodation:', error);
        }
    };

    const handleDeleteMealBreak = async (mealBreakId: number) => {
        try {
            await deleteMealBreak(trip.id, mealBreakId);
            await loadTripData();
        } catch (error) {
            console.error('Error deleting meal break:', error);
        }
    };

    const handlePlaceModalClose = async () => {
        setAddPlaceModalOpen(false);
        setShowMapView(false);
        await loadTripData();

        if (googleMapRef.current) {
            googleMapRef.current = null;
        }
        markersRef.current.forEach(marker => {
            if (marker.setMap) marker.setMap(null);
        });
        markersRef.current = [];
    };

    const handleGetPlace = async (placeId: number) => {
        try {
            const response = await getPlaceById(placeId);
            setSelectedPlace(response.data);
            setViewPlaceModalOpen(true);
        } catch (error) {
            console.error('Не вдалося отримати місце:', error);
        }
    };


    const handleEditPlace = (place: PlaceResponseDto) => {
        setEditingPlace(place);
        setEditModalOpen(true);
    };

    const handleEditSave = async () => {
        if (!editingPlace) return;
        try {
            await updatePlaceVisitDuration(editingPlace.id, editingPlace.visitDuration || 0);
            setEditModalOpen(false);
            await loadTripData();
        } catch (error) {
            console.error('Error updating place visit duration:', error);
        }
    };

    const handleGetAccommodation = async (accommodationId: number) => {
        try {
            const response = await getAccommodationById(accommodationId);
            console.log('Accommodation from API:', response.data);
            setSelectedAccommodation(response.data);
            setViewAccommodationModalOpen(true);
        } catch (error) {
            console.error('Не вдалося отримати дані про житло:', error);
        }
    };

    const handleEditAccommodation = (acc: AccommodationResponseDto) => {
        setEditingAccommodation(acc);
        setEditAccommodationModalOpen(true);
    };

    const handleEditAccommodationSave = async () => {
        if (!editingAccommodation) return;

        const requestDto: AccommodationRequestDto = {
            name: editingAccommodation.name,
            description: editingAccommodation.description,
            accommodationType: editingAccommodation.accommodationType,
            pricePerNight: editingAccommodation.pricePerNight,
            stars: editingAccommodation.stars,
            wifiAvailable: editingAccommodation.wifiAvailable,
            parkingAvailable: editingAccommodation.parkingAvailable,
            petFriendly: editingAccommodation.petFriendly
        };

        try {
            await updateAccommodation(editingAccommodation.id, requestDto);
            setEditAccommodationModalOpen(false);
            await loadTripData();
        } catch (error) {
            console.error('Error updating accommodation:', error);
        }
    };

    const handleGetMealBreak = async (mealBreakId: number) => {
        try {
            const response = await getMealBreakById(mealBreakId);
            console.log('MealBreak from API:', response.data);
            setSelectedMealBreak(response.data);
            setViewMealBreakModalOpen(true);
        } catch (error) {
            console.error('Не вдалося отримати дані про прийом їжі:', error);
        }
    };


    const handleEditMealBreak = (mealBreak: MealBreakResponseDto) => {
        setEditingMealBreak(mealBreak);
        setEditMealBreakModalOpen(true);
    };

    const handleEditMealBreakSave = async () => {
        if (!editingMealBreak) return;

        const requestDto: MealBreakRequestDto = {
            name: editingMealBreak.name as MealBreakType,
            description: editingMealBreak.description,
            startTime: formatTime(editingMealBreak.startTime),
            endTime: formatTime(editingMealBreak.endTime)
        };

        try {
            await updateMealBreak(editingMealBreak.id, requestDto);
            setEditMealBreakModalOpen(false);
            await loadTripData();
        } catch (error) {
            console.error('Error updating meal break:', error);
        }
    };
    const formatTime = (time: string) => time.split(':').slice(0, 2).join(':');


    if (!trip) {
        return (
            <Container>
                <Typography variant="h5" color="error">
                    Подорож не знайдена
                </Typography>
            </Container>
        );
    }

    return (
        <LocalizationProvider dateAdapter={AdapterDateFns}>
            <Container maxWidth="lg" sx={{py: 4}}>
                {/* Header */}
                <Box sx={{mb: 4}}>
                    <Typography variant="h3" component="h1" gutterBottom>
                        {trip.name}
                    </Typography>
                    <Chip
                        icon={<LocationOnIcon/>}
                        label={trip.city}
                        color="primary"
                        size="large"
                    />
                </Box>

                {/* Action Buttons */}
                <Box sx={{
                    mb: 4,
                    display: 'flex',
                    justifyContent: 'space-between',
                    flexWrap: 'wrap',
                    alignItems: 'center',
                    gap: 2
                }}>
                    <Box sx={{display: 'flex', gap: 2, flexWrap: 'wrap'}}>
                        <Button
                            variant="contained"
                            startIcon={<PlaceIcon/>}
                            onClick={() => setAddPlaceModalOpen(true)}
                            color="primary"
                        >
                            Додати місце
                        </Button>
                        <Button
                            variant="contained"
                            startIcon={<HotelIcon/>}
                            onClick={() => setAddAccommodationModalOpen(true)}
                            color="secondary"
                        >
                            Додати житло
                        </Button>
                        <Button
                            variant="contained"
                            startIcon={<RestaurantIcon/>}
                            onClick={() => setAddMealBreakModalOpen(true)}
                            color="success"
                        >
                            Додати прийом їжі
                        </Button>
                    </Box>

                    <Button
                        variant="contained"
                        component={Link}
                        to="/schedule"
                        state={{tripId: trip.id}}
                        color="primary"
                        sx={{
                            borderRadius: 2,
                            px: 3,
                            py: 1.5,
                            textTransform: 'none',
                            fontSize: '1rem',
                            boxShadow: 2,
                            '&:hover': {
                                boxShadow: 4
                            }
                        }}
                    >
                        Розклад
                    </Button>
                </Box>

                <Grid container spacing={4}>
                    <Grid size={12}>
                        <Paper elevation={3} sx={{p: 3, borderRadius: 2}}>
                            <Typography variant="h5" gutterBottom sx={{display: 'flex', alignItems: 'center', gap: 1}}>
                                <PlaceIcon color="primary"/>
                                Місця ({places.length})
                            </Typography>
                            <List>
                                {places.map((place) => (
                                    <ListItem key={place.id} divider>
                                        <ListItemText
                                            primary={place.name}
                                            secondary={
                                                <Box>
                                                    <Typography variant="body2" color="text.secondary">
                                                        {PlaceTypeLabels[place.placeType]}
                                                    </Typography>
                                                    <Typography variant="body2" color="text.secondary">
                                                        {place.description}
                                                    </Typography>
                                                    <Rating value={place.rating} readOnly size="small"/>
                                                </Box>
                                            }
                                        />

                                        <ListItemSecondaryAction>
                                            <IconButton
                                                edge="end"
                                                onClick={() => handleGetPlace(place.id)}
                                                color="primary"
                                                sx={{mr: 1}}>
                                                <VisibilityIcon/>
                                            </IconButton>
                                            <IconButton
                                                edge="end"
                                                onClick={() => handleEditPlace(place)}
                                                color="default"
                                                sx={{mr: 1}}>
                                                <EditIcon/>
                                            </IconButton>
                                            <IconButton
                                                edge="end"
                                                onClick={() => handleDeletePlace(place.id)}
                                                color="error">
                                                <DeleteIcon/>
                                            </IconButton>
                                        </ListItemSecondaryAction>
                                    </ListItem>
                                ))}
                                {places.length === 0 && (
                                    <Typography variant="body2" color="text.secondary" textAlign="center" py={2}>
                                        Місця ще не додані
                                    </Typography>
                                )}
                            </List>
                        </Paper>
                    </Grid>

                    <Grid size={12}>
                        <Paper elevation={3} sx={{p: 3, borderRadius: 2}}>
                            <Typography variant="h5" gutterBottom sx={{display: 'flex', alignItems: 'center', gap: 1}}>
                                <HotelIcon color="secondary"/>
                                Житло ({accommodations.length})
                            </Typography>
                            <List>
                                {accommodations.map((accommodation) => (
                                    <ListItem key={accommodation.id} divider>
                                        <ListItemText
                                            primary={accommodation.name}
                                            secondary={
                                                <Box>
                                                    <Typography variant="body2">
                                                        {AccommodationTypeLabels[accommodation.accommodationType]}
                                                    </Typography>
                                                    <Typography variant="body2">
                                                        {accommodation.description}
                                                    </Typography>
                                                </Box>
                                            }
                                        />
                                        <ListItemSecondaryAction>
                                            <IconButton
                                                edge="end"
                                                onClick={() => handleGetAccommodation(accommodation.id)}
                                                color="primary"
                                                sx={{mr: 1}}
                                            >
                                                <VisibilityIcon/>
                                            </IconButton>

                                            <IconButton
                                                edge="end"
                                                onClick={() => handleEditAccommodation(accommodation)}
                                                color="default"
                                                sx={{mr: 1}}
                                            >
                                                <EditIcon/>
                                            </IconButton>
                                            <IconButton
                                                edge="end"
                                                onClick={() => handleDeleteAccommodation(accommodation.id)}
                                                color="error">
                                                <DeleteIcon/>
                                            </IconButton>
                                        </ListItemSecondaryAction>
                                    </ListItem>
                                ))}
                                {accommodations.length === 0 && (
                                    <Typography variant="body2" color="text.secondary" textAlign="center" py={2}>
                                        Житло ще не додане
                                    </Typography>
                                )}
                            </List>
                        </Paper>
                    </Grid>

                    <Grid size={12}>
                        <Paper elevation={3} sx={{p: 3, borderRadius: 2}}>
                            <Typography variant="h5" gutterBottom sx={{display: 'flex', alignItems: 'center', gap: 1}}>
                                <RestaurantIcon color="success"/>
                                Прийоми їжі ({mealBreaks.length})
                            </Typography>
                            <List>
                                {mealBreaks.map((mealBreak) => (
                                    <ListItem key={mealBreak.id} divider>
                                        <ListItemText
                                            primary={MealBreakTypeLabels[mealBreak.name]}
                                            secondary={
                                                <Box>
                                                    <Box sx={{display: 'flex', alignItems: 'center', gap: 0.5}}>
                                                        <AccessTimeIcon fontSize="small"/>
                                                        <Typography variant="body2">
                                                            {mealBreak.startTime} - {mealBreak.endTime}
                                                        </Typography>
                                                    </Box>
                                                </Box>
                                            }
                                        />
                                        <ListItemSecondaryAction>
                                            <IconButton
                                                edge="end"
                                                onClick={() => handleGetMealBreak(mealBreak.id)}
                                                color="primary"
                                                sx={{mr: 1}}>
                                                <VisibilityIcon/>
                                            </IconButton>
                                            <IconButton
                                                edge="end"
                                                onClick={() => handleEditMealBreak(mealBreak)}
                                                color="default"
                                                sx={{mr: 1}}>
                                                <EditIcon/>
                                            </IconButton>
                                            <IconButton
                                                edge="end"
                                                onClick={() => handleDeleteMealBreak(mealBreak.id)}
                                                color="error">
                                                <DeleteIcon/>
                                            </IconButton>
                                        </ListItemSecondaryAction>
                                    </ListItem>
                                ))}
                                {mealBreaks.length === 0 && (
                                    <Typography variant="body2" color="text.secondary" textAlign="center" py={2}>
                                        Прийоми їжі ще не додані
                                    </Typography>
                                )}
                            </List>
                        </Paper>
                    </Grid>
                </Grid>

                <Dialog open={addPlaceModalOpen} onClose={handlePlaceModalClose} maxWidth="lg" fullWidth>
                    <DialogTitle>
                        <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                            Додати місце до подорожі
                            <Box sx={{display: 'flex', gap: 1}}>
                                <Button
                                    variant={!showMapView ? "contained" : "outlined"}
                                    size="small"
                                    onClick={() => setShowMapView(false)}>
                                    Список
                                </Button>
                                <Button
                                    variant={showMapView ? "contained" : "outlined"}
                                    size="small"
                                    startIcon={<MapIcon/>}
                                    onClick={() => setShowMapView(true)}>
                                    Карта
                                </Button>
                            </Box>
                        </Box>
                    </DialogTitle>
                    <DialogContent>
                        <Box sx={{display: 'flex', gap: 2, mb: 3, alignItems: 'center'}}>
                            <FormControl sx={{minWidth: 200}}>
                                <InputLabel>Тип місця</InputLabel>
                                <Select
                                    value={selectedPlaceType}
                                    onChange={(e) => setSelectedPlaceType(e.target.value as PlaceType)}
                                    label="Тип місця">
                                    {Object.values(PlaceType).map((type) => (
                                        <MenuItem key={type} value={type}>
                                            {PlaceTypeLabels[type]}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                            <Button
                                variant="contained"
                                startIcon={<SearchIcon/>}
                                onClick={handleSearchNearbyPlaces}
                                disabled={loadingNearbyPlaces}>
                                {loadingNearbyPlaces ? 'Пошук...' : 'Знайти поблизу'}
                            </Button>
                        </Box>

                        {showMapView ? (
                            <Box>
                                <Box
                                    ref={mapRef}
                                    sx={{
                                        height: 400,
                                        width: '100%',
                                        borderRadius: 2,
                                        border: '1px solid #ddd'
                                    }}
                                />
                                <Typography variant="body2" color="text.secondary" sx={{mt: 1}}>
                                    Натисніть на маркер, щоб додати місце до подорожі
                                </Typography>
                            </Box>
                        ) : (
                            <Grid container spacing={2}>
                                {nearbyPlaces.map((place, index) => (
                                    <Grid size={12} key={index}>
                                        <Card>
                                            <CardContent>
                                                <Typography variant="h6" gutterBottom>
                                                    {place.name}
                                                </Typography>
                                                <Typography variant="body2" color="text.secondary" gutterBottom>
                                                    {place.description}
                                                </Typography>
                                                <Box sx={{display: 'flex', alignItems: 'center', gap: 1, mb: 1}}>
                                                    <Rating value={place.rating} readOnly size="small"/>
                                                    <Typography variant="body2">
                                                        ({place.rating})
                                                    </Typography>
                                                </Box>
                                                <Typography variant="body2">
                                                    Відкрито: {place.openingTime} - {place.closingTime}
                                                </Typography>
                                            </CardContent>
                                            <CardActions>
                                                <Button
                                                    size="small"
                                                    variant="contained"
                                                    onClick={() => handleAddPlaceToTrip(place)}>
                                                    Додати до подорожі
                                                </Button>
                                            </CardActions>
                                        </Card>
                                    </Grid>
                                ))}
                            </Grid>
                        )}
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handlePlaceModalClose}>Закрити</Button>
                    </DialogActions>
                </Dialog>

                <Dialog open={addAccommodationModalOpen} onClose={() => setAddAccommodationModalOpen(false)}
                        maxWidth="sm" fullWidth>
                    <DialogTitle>Додати житло</DialogTitle>
                    <DialogContent>
                        <Stack spacing={3} sx={{mt: 1}}>
                            <TextField
                                label="Назва житла"
                                fullWidth
                                value={accommodationForm.name}
                                onChange={(e) => setAccommodationForm({...accommodationForm, name: e.target.value})}
                            />
                            <TextField
                                label="Адреса"
                                fullWidth
                                multiline
                                rows={3}
                                value={accommodationForm.description}
                                onChange={(e) => setAccommodationForm({
                                    ...accommodationForm,
                                    description: e.target.value
                                })}
                            />
                            <FormControl fullWidth>
                                <InputLabel>Тип житла</InputLabel>
                                <Select
                                    value={accommodationForm.accommodationType}
                                    onChange={(e) =>
                                        setAccommodationForm({
                                            ...accommodationForm,
                                            accommodationType: e.target.value as AccommodationType
                                        })
                                    }
                                    label="Тип житла">
                                    {Object.values(AccommodationType).map((type) => (
                                        <MenuItem key={type} value={type}>
                                            {AccommodationTypeLabels[type]}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                            <TextField
                                label="Ціна за ніч"
                                type="number"
                                fullWidth
                                value={accommodationForm.pricePerNight}
                                onChange={(e) => setAccommodationForm({
                                    ...accommodationForm,
                                    pricePerNight: Number(e.target.value)
                                })}
                            />
                            <FormControl fullWidth>
                                <InputLabel>Зірки</InputLabel>
                                <Select
                                    value={accommodationForm.stars}
                                    onChange={(e) => setAccommodationForm({
                                        ...accommodationForm,
                                        stars: Number(e.target.value)
                                    })}
                                    label="Зірки">
                                    {[1, 2, 3, 4, 5].map((star) => (
                                        <MenuItem key={star} value={star}>
                                            {star}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                            <Box>
                                <Typography variant="subtitle2" gutterBottom>Додаткові послуги:</Typography>
                                <FormControlLabel
                                    control={
                                        <Checkbox
                                            checked={accommodationForm.wifiAvailable}
                                            onChange={(e) => setAccommodationForm({
                                                ...accommodationForm,
                                                wifiAvailable: e.target.checked
                                            })}
                                        />
                                    }
                                    label="Wi-Fi"
                                />
                                <FormControlLabel
                                    control={
                                        <Checkbox
                                            checked={accommodationForm.parkingAvailable}
                                            onChange={(e) => setAccommodationForm({
                                                ...accommodationForm,
                                                parkingAvailable: e.target.checked
                                            })}
                                        />
                                    }
                                    label="Парковка"
                                />
                                <FormControlLabel
                                    control={
                                        <Checkbox
                                            checked={accommodationForm.petFriendly}
                                            onChange={(e) => setAccommodationForm({
                                                ...accommodationForm,
                                                petFriendly: e.target.checked
                                            })}
                                        />
                                    }
                                    label="Дозволені домашні тварини"
                                />
                            </Box>
                        </Stack>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={() => setAddAccommodationModalOpen(false)}>Скасувати</Button>
                        <Button onClick={handleAddAccommodation} variant="contained">Додати</Button>
                    </DialogActions>
                </Dialog>

                <Dialog open={addMealBreakModalOpen} onClose={() => setAddMealBreakModalOpen(false)} maxWidth="sm"
                        fullWidth>
                    <DialogTitle>Додати прийом їжі</DialogTitle>
                    <DialogContent>
                        <Stack spacing={3} sx={{mt: 1}}>
                            <FormControl fullWidth>
                                <InputLabel>Тип прийому їжі</InputLabel>
                                <Select
                                    value={mealBreakForm.name}
                                    onChange={(e) =>
                                        setMealBreakForm({...mealBreakForm, name: e.target.value as MealBreakType})
                                    }
                                    label="Тип прийому їжі">
                                    {Object.values(MealBreakType).map((type) => (
                                        <MenuItem key={type} value={type}>
                                            {MealBreakTypeLabels[type]}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                            <TextField
                                label="Опис"
                                fullWidth
                                multiline
                                rows={2}
                                value={mealBreakForm.description}
                                onChange={(e) => setMealBreakForm({...mealBreakForm, description: e.target.value})}
                            />
                            <TextField
                                label="Час початку"
                                type="time"
                                fullWidth
                                value={mealBreakForm.startTime}
                                onChange={(e) => setMealBreakForm({...mealBreakForm, startTime: e.target.value})}
                                InputLabelProps={{shrink: true}}
                            />
                            <TextField
                                label="Час закінчення"
                                type="time"
                                fullWidth
                                value={mealBreakForm.endTime}
                                onChange={(e) => setMealBreakForm({...mealBreakForm, endTime: e.target.value})}
                                InputLabelProps={{shrink: true}}
                            />
                        </Stack>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={() => setAddMealBreakModalOpen(false)}>Скасувати</Button>
                        <Button onClick={handleAddMealBreak} variant="contained">Додати прийом їжі</Button>
                    </DialogActions>
                </Dialog>

                <Dialog open={editModalOpen} onClose={() => setEditModalOpen(false)}>
                    <DialogTitle>Редагувати тривалість візиту</DialogTitle>
                    <DialogContent>
                        <TextField
                            label="Тривалість відвідування (хв)"
                            type="number"
                            fullWidth
                            value={editingPlace?.visitDuration || 0}
                            onChange={(e) =>
                                setEditingPlace({
                                    ...editingPlace!,
                                    visitDuration: parseInt(e.target.value, 10),
                                })
                            }
                            sx={{mt: 2}}
                            inputProps={{min: 1}}
                        />
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={() => setEditModalOpen(false)}>Скасувати</Button>
                        <Button variant="contained" onClick={handleEditSave}>Зберегти</Button>
                    </DialogActions>
                </Dialog>

                <Dialog open={editAccommodationModalOpen} onClose={() => setEditAccommodationModalOpen(false)}
                        maxWidth="sm" fullWidth>
                    <DialogTitle>Редагувати житло</DialogTitle>
                    <DialogContent>
                        <Stack spacing={3} sx={{mt: 1}}>
                            <TextField
                                label="Назва"
                                fullWidth
                                value={editingAccommodation?.name || ''}
                                onChange={(e) => setEditingAccommodation({
                                    ...editingAccommodation!,
                                    name: e.target.value
                                })}
                            />
                            <TextField
                                label="Опис"
                                fullWidth
                                multiline
                                rows={2}
                                value={editingAccommodation?.description || ''}
                                onChange={(e) => setEditingAccommodation({
                                    ...editingAccommodation!,
                                    description: e.target.value
                                })}
                            />
                            <FormControl fullWidth>
                                <InputLabel>Тип житла</InputLabel>
                                <Select
                                    value={editingAccommodation?.accommodationType || ''}
                                    onChange={(e) =>
                                        setEditingAccommodation({
                                            ...editingAccommodation!,
                                            accommodationType: e.target.value as AccommodationType
                                        })
                                    }
                                    label="Тип житла">
                                    {Object.values(AccommodationType).map((type) => (
                                        <MenuItem key={type} value={type}>
                                            {AccommodationTypeLabels[type]}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                            <TextField
                                label="Ціна за ніч"
                                type="number"
                                fullWidth
                                value={editingAccommodation?.pricePerNight || 0}
                                onChange={(e) => setEditingAccommodation({
                                    ...editingAccommodation!,
                                    pricePerNight: Number(e.target.value)
                                })}
                            />
                            <FormControl fullWidth>
                                <InputLabel>Зірки</InputLabel>
                                <Select
                                    value={editingAccommodation?.stars || 1}
                                    onChange={(e) => setEditingAccommodation({
                                        ...editingAccommodation!,
                                        stars: Number(e.target.value)
                                    })}
                                    label="Зірки">
                                    {[1, 2, 3, 4, 5].map((star) => (
                                        <MenuItem key={star} value={star}>
                                            {star}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                            <Box>
                                <Typography variant="subtitle2" gutterBottom>Додаткові послуги:</Typography>
                                <FormControlLabel
                                    control={<Checkbox checked={editingAccommodation?.wifiAvailable || false}
                                                       onChange={(e) => setEditingAccommodation({
                                                           ...editingAccommodation!,
                                                           wifiAvailable: e.target.checked
                                                       })}/>}
                                    label="Wi-Fi"
                                />
                                <FormControlLabel
                                    control={<Checkbox checked={editingAccommodation?.parkingAvailable || false}
                                                       onChange={(e) => setEditingAccommodation({
                                                           ...editingAccommodation!,
                                                           parkingAvailable: e.target.checked
                                                       })}/>}
                                    label="Парковка"
                                />
                                <FormControlLabel
                                    control={<Checkbox checked={editingAccommodation?.petFriendly || false}
                                                       onChange={(e) => setEditingAccommodation({
                                                           ...editingAccommodation!,
                                                           petFriendly: e.target.checked
                                                       })}/>}
                                    label="Дозволені домашні тварини"
                                />
                            </Box>
                        </Stack>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={() => setEditAccommodationModalOpen(false)}>Скасувати</Button>
                        <Button onClick={handleEditAccommodationSave} variant="contained">Зберегти</Button>
                    </DialogActions>
                </Dialog>

                <Dialog open={editMealBreakModalOpen} onClose={() => setEditMealBreakModalOpen(false)} maxWidth="sm"
                        fullWidth>
                    <DialogTitle>Редагувати прийом їжі</DialogTitle>
                    <DialogContent>
                        <Stack spacing={3} sx={{mt: 1}}>
                            <FormControl fullWidth>
                                <InputLabel>Тип прийому їжі</InputLabel>
                                <Select
                                    value={editingMealBreak?.name || ''}
                                    onChange={(e) =>
                                        setEditingMealBreak({
                                            ...editingMealBreak!,
                                            name: e.target.value as MealBreakType
                                        })
                                    }
                                    label="Тип прийому їжі">
                                    {Object.values(MealBreakType).map((type) => (
                                        <MenuItem key={type} value={type}>
                                            {MealBreakTypeLabels[type]}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </FormControl>
                            <TextField
                                label="Опис"
                                fullWidth
                                multiline
                                rows={2}
                                value={editingMealBreak?.description || ''}
                                onChange={(e) =>
                                    setEditingMealBreak({...editingMealBreak!, description: e.target.value})
                                }
                            />
                            <TextField
                                label="Час початку"
                                type="time"
                                fullWidth
                                value={editingMealBreak?.startTime || ''}
                                onChange={(e) =>
                                    setEditingMealBreak({...editingMealBreak!, startTime: e.target.value})
                                }
                                InputLabelProps={{shrink: true}}
                            />
                            <TextField
                                label="Час закінчення"
                                type="time"
                                fullWidth
                                value={editingMealBreak?.endTime || ''}
                                onChange={(e) =>
                                    setEditingMealBreak({...editingMealBreak!, endTime: e.target.value})
                                }
                                InputLabelProps={{shrink: true}}
                            />
                        </Stack>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={() => setEditMealBreakModalOpen(false)}>Скасувати</Button>
                        <Button variant="contained" onClick={handleEditMealBreakSave}>Зберегти</Button>
                    </DialogActions>
                </Dialog>


                <Dialog
                    open={viewPlaceModalOpen}
                    onClose={() => setViewPlaceModalOpen(false)}
                    maxWidth="sm"
                    fullWidth
                    PaperProps={{sx: {borderRadius: 3, p: 1.5}}}
                >
                    <DialogTitle
                        sx={{
                            fontWeight: 'bold',
                            fontSize: '1.5rem',
                            display: 'flex',
                            alignItems: 'center',
                            gap: 1,
                            color: 'primary.main',
                            mb: 1
                        }}>
                        <VisibilityIcon/>
                        Перегляд місця
                    </DialogTitle>

                    <DialogContent dividers sx={{pt: 2}}>
                        {selectedPlace ? (
                            <Paper
                                variant="outlined"
                                sx={{
                                    p: 3,
                                    borderRadius: 2,
                                    backgroundColor: 'grey.50',
                                    display: 'flex',
                                    flexDirection: 'column',
                                    gap: 2
                                }}
                            >
                                <Typography variant="h6" sx={{fontWeight: 'bold', color: 'purple'}}>
                                    {selectedPlace.name}
                                </Typography>

                                <Typography variant="body2" color="text.secondary">
                                    <strong>Тип:</strong> {PlaceTypeLabels[selectedPlace.placeType]}
                                </Typography>

                                <Typography variant="body2" color="text.secondary">
                                    <strong>Адреса:</strong> {selectedPlace.description}
                                </Typography>

                                <Typography variant="body2">
                                    <strong>Рейтинг:</strong> {selectedPlace.rating} ⭐
                                </Typography>

                                {selectedPlace.openingTime && selectedPlace.closingTime && (
                                    <Typography variant="body2">
                                        <strong>Години
                                            роботи:</strong> {selectedPlace.openingTime} – {selectedPlace.closingTime}
                                    </Typography>
                                )}

                                {selectedPlace.visitDuration && (
                                    <Typography variant="body2">
                                        <strong>Тривалість візиту:</strong> {selectedPlace.visitDuration} хв
                                    </Typography>
                                )}
                            </Paper>
                        ) : (
                            <Typography variant="body2" color="text.secondary" sx={{py: 2}}>
                                Дані про місце не знайдено.
                            </Typography>
                        )}
                    </DialogContent>

                    <DialogActions sx={{p: 3}}>
                        <Button
                            onClick={() => setViewPlaceModalOpen(false)}
                            variant="outlined"
                            sx={{textTransform: 'none'}}
                        >
                            Закрити
                        </Button>
                    </DialogActions>
                </Dialog>


                <Dialog
                    open={viewMealBreakModalOpen}
                    onClose={() => setViewMealBreakModalOpen(false)}
                    maxWidth="sm"
                    fullWidth
                    PaperProps={{sx: {borderRadius: 3, p: 1.5}}}
                >
                    <DialogTitle
                        sx={{
                            fontWeight: 'bold',
                            fontSize: '1.5rem',
                            display: 'flex',
                            alignItems: 'center',
                            gap: 1,
                            color: 'primary.main',
                            mb: 1
                        }}
                    >
                        <VisibilityIcon/>
                        Перегляд прийому їжі
                    </DialogTitle>

                    <DialogContent dividers sx={{pt: 1.5}}>
                        {selectedMealBreak ? (
                            <Paper
                                variant="outlined"
                                sx={{
                                    p: 3,
                                    borderRadius: 2,
                                    backgroundColor: 'grey.50',
                                    display: 'flex',
                                    flexDirection: 'column',
                                    gap: 2
                                }}
                            >
                                <Typography variant="h6" color="secondary" sx={{fontWeight: 'bold'}}>
                                    🍽️ {MealBreakTypeLabels?.[selectedMealBreak.name] || selectedMealBreak.name}
                                </Typography>

                                {selectedMealBreak.description && (
                                    <Typography variant="body2" color="text.secondary">
                                        <strong>Опис:</strong> {selectedMealBreak.description}
                                    </Typography>
                                )}

                                <Box display="flex" gap={1} alignItems="center">
                                    <AccessTimeIcon fontSize="small" color="action"/>
                                    <Typography variant="body2">
                                        <strong>Час:</strong> {selectedMealBreak.startTime} – {selectedMealBreak.endTime}
                                    </Typography>
                                </Box>
                            </Paper>
                        ) : (
                            <Typography variant="body2" color="text.secondary" sx={{py: 2}}>
                                Дані про прийом їжі не знайдено.
                            </Typography>
                        )}
                    </DialogContent>

                    <DialogActions sx={{p: 3}}>
                        <Button
                            onClick={() => setViewMealBreakModalOpen(false)}
                            variant="outlined"
                            color="primary"
                            sx={{textTransform: 'none'}}
                        >
                            Закрити
                        </Button>
                    </DialogActions>
                </Dialog>


                <Dialog
                    open={viewAccommodationModalOpen}
                    onClose={() => setViewAccommodationModalOpen(false)}
                    maxWidth="sm"
                    fullWidth
                    PaperProps={{sx: {borderRadius: 3, p: 1.5}}}
                >
                    <DialogTitle
                        sx={{
                            fontWeight: 'bold',
                            fontSize: '1.5rem',
                            display: 'flex',
                            alignItems: 'center',
                            gap: 1,
                            color: 'primary.main',
                            mb: 1
                        }}
                    >
                        <VisibilityIcon/>
                        Перегляд житла
                    </DialogTitle>

                    <DialogContent dividers sx={{pt: 1.5}}>
                        {selectedAccommodation ? (
                            <Paper
                                variant="outlined"
                                sx={{
                                    p: 3,
                                    borderRadius: 2,
                                    backgroundColor: 'grey.50',
                                    display: 'flex',
                                    flexDirection: 'column',
                                    gap: 2
                                }}
                            >
                                <Typography variant="h6" sx={{fontWeight: 'bold', color: 'purple'}}>
                                    {selectedAccommodation.name}
                                </Typography>

                                <Typography variant="body2" color="text.secondary">
                                    <strong>Тип
                                        житла:</strong> {AccommodationTypeLabels[selectedAccommodation.accommodationType]}
                                </Typography>

                                <Typography variant="body2" color="text.secondary">
                                    <strong>Адреса:</strong> {selectedAccommodation.description}
                                </Typography>

                                <Typography variant="body2">
                                    <strong>Ціна за ніч:</strong> {selectedAccommodation.pricePerNight} ₴
                                </Typography>

                                <Typography variant="body2">
                                    <strong>Зірки:</strong>{' '}
                                    {'⭐'.repeat(selectedAccommodation.stars)}
                                </Typography>

                                <Box sx={{display: 'flex', flexDirection: 'column', gap: 1, mt: 1}}>
                                    <Typography variant="body2">
                                        <strong>Wi-Fi:</strong> {selectedAccommodation.wifiAvailable ? 'Так' : 'Ні'}
                                    </Typography>
                                    <Typography variant="body2">
                                        <strong>Паркінг:</strong> {selectedAccommodation.parkingAvailable ? 'Так' : 'Ні'}
                                    </Typography>
                                    <Typography variant="body2">
                                        <strong>Можна з
                                            тваринами:</strong> {selectedAccommodation.petFriendly ? 'Так' : 'Ні'}
                                    </Typography>
                                </Box>
                            </Paper>

                        ) : (
                            <Typography variant="body2" color="text.secondary" sx={{py: 2}}>
                                Дані про житло не знайдено.
                            </Typography>
                        )}
                    </DialogContent>

                    <DialogActions sx={{p: 3}}>
                        <Button
                            onClick={() => setViewAccommodationModalOpen(false)}
                            variant="outlined"
                            color="primary"
                            sx={{textTransform: 'none'}}
                        >
                            Закрити
                        </Button>
                    </DialogActions>
                </Dialog>


            </Container>
        </LocalizationProvider>
    );
}

export default TripDetails;