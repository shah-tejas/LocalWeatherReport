import React, { useState } from 'react';
import PropTypes from 'prop-types';
import './CitiesSelect.scss';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import InputLabel from '@material-ui/core/InputLabel';
import FormControl from '@material-ui/core/FormControl';

const CitiesSelect = ({ cities, onCitiesSelect }) => {
    const [selectedCity, setSelectedCity] = useState('');
    const onSelect = (event) => {
        setSelectedCity(event.target.value);
        onCitiesSelect(event.target.value);
    };
    return (
        <div className="cities-select">
            <FormControl className="select-form-control">
                <InputLabel id="select-label">Select a City</InputLabel>
                <Select
                    labelId="select-label"
                    id="select-city"
                    value={selectedCity}
                    onChange={onSelect}
                    label="Select a City"
                >
                    {cities.map(city => (
                        <MenuItem value={city.city} key={city.id}>{city.city}</MenuItem>
                    ))}
                </Select>
            </FormControl>
        </div>
    );
}

CitiesSelect.props = {
    cities: PropTypes.array.isRequired,
    onCitiesSelect: PropTypes.func
}

export default CitiesSelect;
