import React from 'react';
import PropTypes from 'prop-types';
import './WeatherConditionIcon.scss';

const WeatherConditionIcon = ({ conditionType }) => {

    const icon = require(`../../assets/${conditionType}.svg`);

    return (
        <div className="weather-condition-wrapper">
            <img src={icon} alt={conditionType} />
        </div>
    );
};


WeatherConditionIcon.propTypes = {
    conditionType: PropTypes.string.isRequired
};


export default WeatherConditionIcon;
