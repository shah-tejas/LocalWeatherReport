import React, { useState, useEffect }  from 'react';
import PropTypes from 'prop-types';
import './WeekWeather.scss';
import { WEEK_WEATHER_API_URL, WEEK_SHORT_NAME } from '../../utils/constants';

const WeekWeather = ({ city }) => {
    const [weekWeather, setWeekWeather] = useState([]);

    useEffect(() => {
        const fetchWeekWeather = async () => {
            const resp = await fetch(`${WEEK_WEATHER_API_URL}?city=${city}`);
            const respBody = await resp.json();
            setWeekWeather(respBody);
        }
        fetchWeekWeather();
    }, [city]);

    return (
        <div>
            {weekWeather.map((weather, index) => (
                <div key={index}>
                    <p>{WEEK_SHORT_NAME[weather.dayOfWeek]}</p>
                    <p>{weather.weatherCondition}</p>
                    <p>{weather.minTemp}</p>
                    <p>{weather.maxTemp}</p>
                </div>
            ))}
        </div>
    );
};


WeekWeather.propTypes = {
    city: PropTypes.string.isRequired
};


export default WeekWeather;
