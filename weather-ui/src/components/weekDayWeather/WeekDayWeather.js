import React from 'react';
import PropTypes from 'prop-types';
import './WeekDayWeather.scss';
import { WeatherConditionIcon } from '..';

const WeekDayWeather = ({ dayOfWeek, minTemp, maxTemp, weatherCondition }) => {
    return (
        <div className="week-day-container">
            <div>{dayOfWeek}</div>
            <WeatherConditionIcon conditionType={weatherCondition} />
            <div className="temp-group">
                <div>{maxTemp}°</div>
                <div>{minTemp}°</div>
            </div>
        </div>
    );
};


WeekDayWeather.propTypes = {
    dayOfWeek: PropTypes.string,
    minTemp: PropTypes.string,
    maxTemp: PropTypes.string,
    weatherCondition: PropTypes.string
};


export default WeekDayWeather;
