import React, { useState, useEffect }  from 'react';
import PropTypes from 'prop-types';
import './WeekWeather.scss';
import { WEEK_WEATHER_API_URL, WEEK_SHORT_NAME } from '../../utils/constants';
import { WeekDayWeather } from '..';

const WeekWeather = ({ city, celsius, setDate }) => {
    const [weekWeather, setWeekWeather] = useState([]);

    useEffect(() => {
        const fetchWeekWeather = async () => {
            const resp = await fetch(`${WEEK_WEATHER_API_URL}?city=${city}`);
            const respBody = await resp.json();
            setDate(respBody[respBody.length-1].date);
            setWeekWeather(respBody);
        }
        fetchWeekWeather();
    }, [city, setDate]);

    return (
        <div className="week-weather">
            {weekWeather.map((weather, index) => (
                <WeekDayWeather
                    key={index}
                    dayOfWeek={WEEK_SHORT_NAME[weather.dayOfWeek]}
                    weatherCondition={weather.weatherCondition}
                    minTemp={parseInt(celsius ? weather.minTemp : (weather.minTemp * 1.8 + 32))}
                    maxTemp={parseInt(celsius ? weather.maxTemp : (weather.maxTemp * 1.8 + 32))}
                    date={weather.date}
                    setDate={setDate}
                />
            ))}
        </div>
    );
};


WeekWeather.propTypes = {
    city: PropTypes.string.isRequired,
    celsius: PropTypes.string,
    setDate: PropTypes.func
};


export default WeekWeather;
