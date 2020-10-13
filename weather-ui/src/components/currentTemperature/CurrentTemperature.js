import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import './CurrentTemperature.scss';

const CurrentTemperature = ({ temperature, celsius, setCelsius }) => {

    const [temperatureInt, setTemperatureInt] = useState();

    useEffect(() => {
        setTemperatureInt(celsius ? parseInt(temperature) : parseInt(temperature * 1.8 + 32));
    }, [temperature, celsius]);

    const onClickHandler = (e) => {
        setCelsius(!celsius);
    }

    return (
        <div className="temperature-wrapper">
            <p className="temp">{temperatureInt}</p>
            <pre>{celsius}</pre>
            {!celsius 
                && (<div className="temperature-unit">
                        <span className="clickable" onClick={onClickHandler}>°C</span>
                        <span>{' '}|{' '}</span>
                        <span>°F</span>
                    </div>)
            }
            {celsius 
                && (<div className="temperature-unit">
                        <span>°C</span>
                        <span>{' '}|{' '}</span>
                        <span className="clickable" onClick={onClickHandler}>°F</span>
                    </div>)
            }
        </div>
    );
};


CurrentTemperature.propTypes = {
    temperature: PropTypes.string,
    celsius: PropTypes.string,
    setCelsius: PropTypes.func
};


export default CurrentTemperature;
