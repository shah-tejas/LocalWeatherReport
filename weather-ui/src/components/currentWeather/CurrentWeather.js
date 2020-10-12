import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import './CurrentWeather.scss';
import { CURRENT_WEATHER_API_URL } from '../../utils/constants';

const CurrentWeather = ({ city }) => {
    const [temp, setTemp] = useState('');
    const [day, setDay] = useState('');
    const [condition, setCondition] = useState('');

    useEffect(() => {
        const fetchCurrentWeather = async () => {
            const resp = await fetch(`${CURRENT_WEATHER_API_URL}?city=${city}`);
            const respBody = await resp.json();
            setTemp(respBody.temperature);
            setDay(respBody.day);
            setCondition(respBody.weatherCondition);
        }
        fetchCurrentWeather();
    }, [city]);

    return (
        <div>
            <p>{city}</p>
            <p>{day}</p>
            <p>{condition}</p>
            <p>{temp}</p>
        </div>
    );
};


CurrentWeather.propTypes = {
    city: PropTypes.string.isRequired
};


export default CurrentWeather;
