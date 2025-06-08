import './App.css'
import NavBar from './components/nav-bar/NavBar'
import { Routes, Route } from 'react-router-dom';
import Trip from "./components/trip/Trip.tsx";
import TripDetails from "./components/trip/TripDetails.tsx";
import WeatherRecommendationsPage from "./components/weather/WeatherRecommendationsPage.tsx";
import Schedule from "./components/schedule/Schedule.tsx";
import RoutePage from "./components/route/Route.tsx";
import Review from "./components/review/Review.tsx";

function App() {
    return (
        <>
            <NavBar/>
            <Routes>
                <Route path="/trips" element={<Trip />} />
                <Route path="/" element={<Trip />} /> {/* Default route */}
                <Route path="/tripDetail" element={<TripDetails />} />
                <Route path="/weather-recommendations" element={<WeatherRecommendationsPage />} />
                <Route path="/schedule" element={<Schedule />} />
                <Route path="/route" element={<RoutePage />} />
                <Route path="/review" element={<Review />} />
            </Routes>
        </>
    )
}

export default App