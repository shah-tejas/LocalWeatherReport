import React, { useState } from 'react';
import PropTypes from 'prop-types';
import './CitiesSelect.scss';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';

const CitiesSelect = ({ cities, onCitiesSelect }) => {
    const [selectedCity, setSelectedCity] = useState('');
    const onSelect = (event) => {
        setSelectedCity(event.target.value);
        onCitiesSelect(event.target.value);
    };
    return (
        <div>
            <Select
                id="select-city"
                value={selectedCity}
                onChange={onSelect}
            >
                {cities.map(city => (
                    <MenuItem value={city.city} key={city.id}>{city.city}</MenuItem>
                ))}
            </Select>
        </div>
    );
}

CitiesSelect.props = {
    cities: PropTypes.array.isRequired,
    onCitiesSelect: PropTypes.func
}

export default CitiesSelect;
