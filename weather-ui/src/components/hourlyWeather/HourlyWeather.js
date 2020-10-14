import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import './HourlyWeather.scss';
import { HOUR_WEATHER_API_URL } from '../../utils/constants';
import Chart from "react-google-charts";

const HourlyWeather = ({ city, date, celsius }) => {
    const [hourlyWeather, setHourlyWeather] = useState([]);
    const [chartData, setChartData] = useState([['Time', 'Temperature']]);

    const formatHour = (hour) => {
        let hourNum = (((hour+1) % 12) === 0) ? 12 : ((hour+1) % 12);
        let pm = (hour >= 11 && hour < 23);
        return `${hourNum} ${pm ? 'PM' : 'AM'}`;
    }

    useEffect(() => {
        setChartData([['Time', 'Temperature']]);
        hourlyWeather.forEach(weather => {
            const temp = celsius ? weather.temp : parseInt(weather.temp * 1.8 + 32);
            const hour = formatHour(weather.hour);
            setChartData(chartData => [...chartData, [hour, temp]]);
        });
    }, [hourlyWeather, celsius]);

    useEffect(() => {
        const fetchHourlyWeather = async () => {
            const resp = await fetch(`${HOUR_WEATHER_API_URL}?city=${city}&date=${date}`);
            const json = await resp.json();
            setHourlyWeather(json);
        };
        if(date) {
            fetchHourlyWeather();
        }
    }, [city, date]);
    return (
        <div>
            {chartData && chartData.length > 1 &&
                <Chart
                    chartType="LineChart"
                    loader={<div>Loading Chart</div>}
                    data={chartData}
                    options={{
                        hAxis: {
                            showTextEvery : 2,
                        },
                        curveType: 'function',
                        axisTitlesPosition: 'none',
                        backgroundColor: '#DCDCDC',
                        chartArea: {
                            left: 0, 
                            top: 0, 
                            width: '100%', 
                            height: '50%',
                        },
                        pointsVisible: true,
                        
                    }}
                />
            }
        </div>
    );
};


HourlyWeather.propTypes = {
    city: PropTypes.string,
    date: PropTypes.string,
    celsius: PropTypes.bool
};


export default HourlyWeather;
