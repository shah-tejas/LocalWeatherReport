import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import './CurrentWeather.scss';
import { CURRENT_WEATHER_API_URL } from '../../utils/constants';
import { CurrentTemperature, WeatherConditionIcon } from '../../components';

const CurrentWeather = ({ city, celsius, setCelsius }) => {
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
        <div className="current-weather-container">
            <p className="city-section">{city}</p>
            <p>{day}</p>
            <p>{condition}</p>
            <div className="condition-and-temp">
                {condition 
                    && <WeatherConditionIcon conditionType={condition} />
                }
                {temp
                    && <CurrentTemperature celsius={celsius} setCelsius={setCelsius} temperature={temp} />
                }
            </div>
            
        </div>
    );
};


CurrentWeather.propTypes = {
    city: PropTypes.string.isRequired,
    celsius: PropTypes.string,
    setCelsius: PropTypes.func
};


export default CurrentWeather;
