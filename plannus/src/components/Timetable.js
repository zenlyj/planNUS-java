import React, { Component, useEffect } from 'react'
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Task from './Task'
import api from '../api/backendInterface';
import { grey } from '@mui/material/colors';

function Timetable(props) {
    const [times, setTimes] = React.useState([])
    const [dates, setDates] = React.useState([])

    useEffect(() => {
        const res = []
        const start = 8
        const end = 21
        for (let i = start; i <= end; i++) {
            res.push(i < 10 ? `0${i}00` : `${i}00`)
        }
        setTimes(res)        
    }, [])

    useEffect(() => {
        const weeks = []
        let day = new Date(2022,7,8)
        weeks.push(null)
        for (let i = 1; i <= 13; i++) {
            const week = []
            for (let j = 0; j < 7; j++) {
                const nextDay = new Date(day)
                nextDay.setDate(day.getDate()+1)
                week.push(day)
                day = nextDay
            }
            if (i === 6) {
                // recess week
                const afterReccess = new Date(day)
                afterReccess.setDate(day.getDate()+7)
                day = afterReccess
            }
            weeks.push(week)
        }
        setDates(weeks)        
    }, [])

    const findTask = (date, timeFrom) => {
        return props.tasks.find(task => task.date === date && task.timeFrom === timeFrom)
    }

    const findDate = (week, dayIndex) => {
        const taskDate = dates[week][dayIndex]
        const year = taskDate.getFullYear()
        let month = taskDate.getMonth()+1
        let day = taskDate.getDate()
        if (month < 10) {
            month = '0' + month
        }
        if (day < 10) {
            day = '0' + day
        }
        return `${year}-${month}-${day}`
    }

    const head = () => {
        const cellVal = []
        cellVal.push(
            <TableCell align="left">
                FROM <br /> TO
            </TableCell>
        )
        for (let i = 0; i < times.length-1; i++) {
            const fromHour = times[i]
            const toHour = times[i+1]
            cellVal.push(
                <TableCell align="center">
                    {fromHour} <br /> {toHour}
                </TableCell>
            )
        }
        return (
            <TableHead>
                <TableRow>
                    {cellVal}
                </TableRow>
            </TableHead>
        )
    }

    const body = () => {
        const days = ['MON', 'TUES', 'WED', 'THURS', 'FRI', 'SAT', 'SUN']
        const rows = []
        days.map(day => {
            const cells = []
            cells.push(<TableCell align="left"> {day} </TableCell>)
            for (let i = 0; i < times.length-1; i++) {
                const startTime = times[i]
                const date = findDate(props.week, days.indexOf(day))
                const task = findTask(date, startTime)
                const taskPresent = task ? true : false
                const name = taskPresent ? task.name : ''
                const module = taskPresent ? task.module : ''
                const timeFrom = startTime
                const timeTo = taskPresent ? task.timeTo : ''
                const description = taskPresent ? task.description : ''
                const colSpan = (timeTo-timeFrom)/100
                if (colSpan > 1) {
                    i += (colSpan-1)
                }
                cells.push(<TableCell sx={{borderLeft:1, borderLeftColor:'success.light', maxWidth:'8em'}} align="center" colSpan={colSpan}>
                    <Task id={taskPresent ? task.id : null} 
                            taskPresent={taskPresent} 
                            name={name} 
                            module={module} 
                            timeFrom={timeFrom} 
                            timeTo={timeTo} 
                            description={description} 
                            date={date}
                            refresh={props.refresh}
                    />
                </TableCell>)
            }
            const isEvenRow = days.indexOf(day)%2==0
            const color = isEvenRow ? 'grey.300' : 'grey.100'
            rows.push(<TableRow sx={{backgroundColor: color}}> {cells} </TableRow>)
        })
        return (
            <TableBody> {rows} </TableBody>
        )
    }
    
    return (
        <TableContainer>
            <Table>
                {head()}
                {body()}
            </Table>
        </TableContainer>
    )
}

export default Timetable