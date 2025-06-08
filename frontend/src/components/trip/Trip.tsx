import Box from '@mui/material/Box'
import Typography from '@mui/material/Typography'
import TripList from "./TripList.tsx";

function Trip() {
    return (
        <Box sx={{
            minHeight: 'calc(100vh - 64px)',
            width: '100%',
            margin: 0,
            padding: 0,
            backgroundColor: '#f5f5f5'
        }}>
            <Box display="flex" justifyContent="center" mt={2}>
                <Typography variant="body1">
                    <TripList/>
                </Typography>
            </Box>
        </Box>
    )
}

export default Trip