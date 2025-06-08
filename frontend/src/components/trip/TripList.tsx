import React, {useEffect, useState} from "react";
import {
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    Paper,
    Container,
    Typography,
    Box,
    IconButton,
    Chip
} from "@mui/material";
import type {TripRequestDto} from "../../dto/trip/TripRequestDto.ts";
import {Link} from "react-router-dom";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import {cloneTrip, CreateTrip, deleteTripById, GetAllTripsAsync, updateTrip} from "../../services/TripService.ts";
import type {TripResponseDto} from "../../dto/trip/TripResponseDto.ts";
import AddIcon from '@mui/icons-material/Add';
import VisibilityIcon from '@mui/icons-material/Visibility';
import DeleteIcon from '@mui/icons-material/Delete';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import TravelExploreIcon from '@mui/icons-material/TravelExplore';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import EditIcon from '@mui/icons-material/Edit';

function TripList() {
    const [trips, setTrips] = useState<TripResponseDto[]>([]);
    const [createTripDto, setTripCreateDto] = useState<TripRequestDto>({name: '', city: '', userId: 1});
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const [editingTrip, setEditingTrip] = useState<TripResponseDto | null>(null);
    const [editTripModalOpen, setEditTripModalOpen] = useState(false);

    const fetchData = async () => {
        const response = GetAllTripsAsync()
        response.then((data)=>{
            setTrips(data);
        }).catch((error) => {
            console.error('Error fetching data', error);
            throw error;
        });
    }

    useEffect(() => {
        fetchData();
    }, []);

    const handleCreateTrip = async () => {
        try {
            const createdTrip = await CreateTrip(createTripDto);
            await fetchData();
            setIsDialogOpen(false);
            setTripCreateDto({name: '', city: '', userId:1});
        } catch (error) {
            console.error('Помилка під час створення подорожі', error);
        }
    };

    const handleInputChange = (field: keyof TripRequestDto, value: string) => {
        setTripCreateDto(prev => ({
            ...prev,
            [field]: value
        }));
    };

    const handleDeleteTrip = async (tripId: number) => {
        try {
            await deleteTripById(tripId);
            await fetchData();
        } catch (error) {
            console.error('Помилка під час видалення подорожі:', error);
        }
    };

    const handleCloneTrip = async (tripId: number) => {
        try {
            await cloneTrip(tripId);
            await fetchData();
        } catch (error) {
            console.error('Помилка під час клонування подорожі:', error);
        }
    };

    const handleEditTrip = (trip: TripResponseDto) => {
        setEditingTrip(trip);
        setEditTripModalOpen(true);
    };

    const handleSaveTripEdit = async () => {
        if (!editingTrip) return;

        const requestDto: TripRequestDto = {
            name: editingTrip.name,
            city: editingTrip.city,
            userId: 1,
        };

        try {
            await updateTrip(editingTrip.id, requestDto);
            setEditTripModalOpen(false);
            setEditingTrip(null);
            await fetchData();
        } catch (error) {
            console.error('Помилка при збереженні подорожі:', error);
        }
    };

    return (
        <Container maxWidth="lg" sx={{ py: 4 }}>
            {/* Header Section */}
            <Box sx={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                mb: 4,
                flexWrap: 'wrap',
                gap: 2
            }}>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                    <TravelExploreIcon sx={{ fontSize: 40, color: 'primary.main' }} />
                    <Typography variant="h4" component="h1" fontWeight="bold" color="primary.main">
                        Мої подорожі
                    </Typography>
                </Box>
                <Button
                    variant="contained"
                    color="primary"
                    startIcon={<AddIcon />}
                    onClick={() => setIsDialogOpen(true)}
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
                    Створити подорож
                </Button>
            </Box>

            {/* Create Trip Dialog */}
            <Dialog
                open={isDialogOpen}
                onClose={() => setIsDialogOpen(false)}
                maxWidth="sm"
                fullWidth
                PaperProps={{
                    sx: { borderRadius: 3 }
                }}
            >
                <DialogTitle sx={{
                    pb: 1,
                    fontSize: '1.5rem',
                    fontWeight: 'bold',
                    display: 'flex',
                    alignItems: 'center',
                    gap: 1
                }}>
                    <TravelExploreIcon color="primary" />
                    Створіть нову подорож
                </DialogTitle>
                <DialogContent sx={{ pt: 2 }}>
                    <TextField
                        autoFocus
                        margin="normal"
                        label="Назва подорожі"
                        fullWidth
                        variant="outlined"
                        value={createTripDto.name}
                        onChange={(e) => handleInputChange('name', e.target.value)}
                        sx={{ mb: 2 }}
                    />
                    <TextField
                        margin="normal"
                        label="Назва міста"
                        fullWidth
                        variant="outlined"
                        value={createTripDto.city}
                        onChange={(e) => handleInputChange('city', e.target.value)}
                        InputProps={{
                            startAdornment: <LocationOnIcon sx={{ mr: 1, color: 'text.secondary' }} />
                        }}
                    />
                </DialogContent>
                <DialogActions sx={{ p: 3, gap: 1 }}>
                    <Button
                        onClick={() => {
                            setIsDialogOpen(false);
                            setTripCreateDto({name: '', city: '', userId: 1});
                        }}
                        variant="outlined"
                        sx={{ textTransform: 'none' }}
                    >
                        Скасувати
                    </Button>
                    <Button
                        onClick={handleCreateTrip}
                        variant="contained"
                        sx={{ textTransform: 'none' }}
                        disabled={!createTripDto.name || !createTripDto.city}
                    >
                        Створити
                    </Button>
                </DialogActions>
            </Dialog>

            <Dialog
                open={editTripModalOpen}
                onClose={() => setEditTripModalOpen(false)}
                maxWidth="sm"
                fullWidth
                PaperProps={{
                    sx: { borderRadius: 3 }
                }}
            >
                <DialogTitle
                    sx={{
                        pb: 1,
                        fontSize: '1.5rem',
                        fontWeight: 'bold',
                        display: 'flex',
                        alignItems: 'center',
                        gap: 1
                    }}
                >
                    <EditIcon color="primary" />
                    Редагувати подорож
                </DialogTitle>

                <DialogContent sx={{ pt: 2 }}>
                    <TextField
                        autoFocus
                        margin="normal"
                        label="Назва подорожі"
                        fullWidth
                        variant="outlined"
                        value={editingTrip?.name || ''}
                        onChange={(e) =>
                            setEditingTrip((prev) => prev ? { ...prev, name: e.target.value } : prev)
                        }
                        sx={{ mb: 2 }}
                    />
                    <TextField
                        margin="normal"
                        label="Назва міста"
                        fullWidth
                        variant="outlined"
                        value={editingTrip?.city || ''}
                        onChange={(e) =>
                            setEditingTrip((prev) => prev ? { ...prev, city: e.target.value } : prev)
                        }
                        InputProps={{
                            startAdornment: <LocationOnIcon sx={{ mr: 1, color: 'text.secondary' }} />
                        }}
                    />
                </DialogContent>

                <DialogActions sx={{ p: 3, gap: 1 }}>
                    <Button
                        onClick={() => {
                            setEditTripModalOpen(false);
                            setEditingTrip(null);
                        }}
                        variant="outlined"
                        sx={{ textTransform: 'none' }}
                    >
                        Скасувати
                    </Button>
                    <Button
                        onClick={handleSaveTripEdit}
                        variant="contained"
                        sx={{ textTransform: 'none' }}
                        disabled={!editingTrip?.name || !editingTrip?.city}
                    >
                        Зберегти
                    </Button>
                </DialogActions>
            </Dialog>

            {/* Trips Table */}
            <Paper elevation={3} sx={{ borderRadius: 3, overflow: 'hidden' }}>
                {trips.length === 0 ? (
                    <Box sx={{
                        p: 6,
                        textAlign: 'center',
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        gap: 2
                    }}>
                        <TravelExploreIcon sx={{ fontSize: 80, color: 'text.secondary', opacity: 0.3 }} />
                        <Typography variant="h6" color="text.secondary">
                            У вас поки немає подорожей
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            Створіть свою першу подорож, щоб почати планування!
                        </Typography>
                    </Box>
                ) : (
                    <Table>
                        <TableHead>
                            <TableRow sx={{ backgroundColor: 'grey.50' }}>
                                <TableCell sx={{ fontWeight: 'bold', fontSize: '1rem' }}>
                                    Назва подорожі
                                </TableCell>
                                <TableCell sx={{ fontWeight: 'bold', fontSize: '1rem' }}>
                                    Місто
                                </TableCell>
                                <TableCell align="center" sx={{ fontWeight: 'bold', fontSize: '1rem' }}>
                                    Дії
                                </TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {trips.map((trip, index) => (
                                <TableRow
                                    key={trip.id}
                                    sx={{
                                        '&:hover': {
                                            backgroundColor: 'grey.50'
                                        },
                                        '&:nth-of-type(even)': {
                                            backgroundColor: 'grey.25'
                                        }
                                    }}
                                >
                                    <TableCell sx={{ py: 2 }}>
                                        <Typography variant="body1" fontWeight="medium">
                                            {trip.name}
                                        </Typography>
                                    </TableCell>
                                    <TableCell sx={{ py: 2 }}>
                                        <Chip
                                            icon={<LocationOnIcon />}
                                            label={trip.city}
                                            variant="outlined"
                                            color="primary"
                                            size="small"
                                        />
                                    </TableCell>
                                    <TableCell align="center" sx={{ py: 2 }}>
                                        <Box sx={{ display: 'flex', gap: 1, justifyContent: 'center' }}>
                                            <Button
                                                variant="contained"
                                                color="primary"
                                                size="small"
                                                startIcon={<VisibilityIcon />}
                                                component={Link}
                                                to="/tripDetail"
                                                state={trip}
                                                sx={{
                                                    textTransform: 'none',
                                                    borderRadius: 2,
                                                    minWidth: 'auto'
                                                }}
                                            >
                                                Переглянути
                                            </Button>
                                            <IconButton
                                                edge="end"
                                                onClick={() => handleEditTrip(trip)}
                                                color="primary"
                                                sx={{ mr: 1 }}
                                            >
                                                <EditIcon />
                                            </IconButton>
                                            <IconButton
                                                edge="end"
                                                onClick={() => handleCloneTrip(trip.id)}
                                                color="info"
                                                sx={{ mr: 1 }}
                                            >
                                                <ContentCopyIcon />
                                            </IconButton>
                                            <IconButton
                                                edge="end"
                                                onClick={() => handleDeleteTrip(trip.id)}
                                                color="error"
                                            >
                                                <DeleteIcon />
                                            </IconButton>
                                        </Box>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                )}
            </Paper>
        </Container>
    );
}

export default TripList;