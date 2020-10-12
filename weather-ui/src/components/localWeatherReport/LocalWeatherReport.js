import React, {useState, useEffect} from 'react';
import './LocalWeatherReport.scss';
import { CITIES_API_URL } from '../../utils/constants';
import CitiesSelect from '../citiesSelect/CitiesSelect';

const LocalWeatherReport = () => {

    const [cities, setCities] = useState([]);
    const [city, setCity] = useState('');

    useEffect(() => {
        const fetchCities = async () => {
            const resp = await fetch(CITIES_API_URL);
            const respCities = await resp.json();
            setCities(respCities);
        }
        fetchCities();
    }, []);

    return (
        <div>
            <CitiesSelect cities={cities} onCitiesSelect={setCity} />
            {city}
        </div>
    );
}

export default LocalWeatherReport;
