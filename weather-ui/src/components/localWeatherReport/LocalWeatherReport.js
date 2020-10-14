import React, {useState, useEffect} from 'react';
import './LocalWeatherReport.scss';
import { CITIES_API_URL } from '../../utils/constants';
import { CitiesSelect, CurrentWeather, HourlyWeather, WeekWeather } from '../../components';

const LocalWeatherReport = () => {

    const [cities, setCities] = useState([]);
    const [city, setCity] = useState('');
    const [celsius, setCelsius] = useState(true);
    const [selectedDate, setSelectedDate] = useState('');

    useEffect(() => {
        const fetchCities = async () => {
            const resp = await fetch(CITIES_API_URL);
            const respCities = await resp.json();
            setCities(respCities);
        }
        fetchCities();
    }, []);

    return (
        <div className="container">
            <CitiesSelect cities={cities} onCitiesSelect={setCity} />
            {city &&
                <CurrentWeather city={city} celsius={celsius} setCelsius={setCelsius} />
            }
            {city && selectedDate && selectedDate.length > 0 &&
                <HourlyWeather city={city} date={selectedDate} celsius={celsius} />
            }
            {city &&
                <WeekWeather city={city} celsius={celsius} setDate={setSelectedDate} />
            }
        </div>
    );
}

export default LocalWeatherReport;
