import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import { Link } from 'react-router-dom';

function NavBar() {
    return (
        <>
            <AppBar position="fixed">
                <Toolbar>
                    <IconButton
                        size="large"
                        edge="start"
                        color="inherit"
                        aria-label="menu"
                        sx={{ mr: 2 }}
                    >
                        <MenuIcon />
                    </IconButton>
                    <Typography variant="h6" component="div" sx={{ mr: 2 }}>
                        Планувальник подорожей
                    </Typography>
                    <Button color="inherit" component={Link} to="/trips">Подорожі</Button>
                    <Button color="inherit" component={Link} to="/weather-recommendations">
                        Погодні рекомендації
                    </Button>
                    <Box sx={{ flexGrow: 1 }} />
                    <Button color="inherit" component={Link} to="/login">Логін</Button>
                    <Button color="inherit" component={Link} to="/sign-up">Реєстрація</Button>
                </Toolbar>
            </AppBar>
            <Toolbar />
        </>
    );
}

export default NavBar;
