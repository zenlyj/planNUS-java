import { TableBody } from "@mui/material"
import React, { Component, useEffect } from "react"
import Table from 'react-bootstrap/Table'
import TableHead from '@mui/material/TableHead'
import TableRow from '@mui/material/TableRow'
import TableCell from '@mui/material/TableCell'
import TableContainer from '@mui/material/TableContainer'
import CalendarDay from './CalendarDay'

function Calendar(props) {
    const [days, setDays] = React.useState(['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'])

    const head = () => {
        return (
            <TableHead>
                <TableRow>
                    {days.map(day => <TableCell align="center"> {day} </TableCell>)}
                </TableRow>
            </TableHead>
        )
    }

    const fillBeforeMonth = (row, dayOfWeek, date) => {
        for (let i = 0; i < dayOfWeek; i++) {
            const defaultStyle = {color: '#a2b2d3', fontSize: 20, opacity:'50%', textAlign:'center'}
            const beforeDate = new Date(date)
            beforeDate.setDate(date.getDate()-(dayOfWeek-i))
            row.push(<TableCell>
                <div style={defaultStyle}> {beforeDate.getDate()} </div>
                </TableCell>
            )
        }
    }

    const fillAfterMonth = (row, dayOfWeek) => {
        for (let i = dayOfWeek; i < 7; i++) {
            const defaultStyle = {color: '#a2b2d3', fontSize: 20, opacity:'50%', textAlign:'center'}
            row.push(<TableCell>
                <div style={defaultStyle}> {i-dayOfWeek+1} </div>
                </TableCell>)
        }
    }

    const formatDate = (date) => {
        let day = date.getDate()
        if (day < 10) {
            day = '0'+day
        }
        let month = date.getMonth()+1
        if (month < 10) {
            month = '0'+month
        }
        const year = date.getFullYear()
        return `${year}-${month}-${day}`
    }

    const body = () => {
        const rows = []
        let row = []
        let date = new Date(2022, props.month-1, 1)
        fillBeforeMonth(row, date.getDay(), date)
        while (date.getMonth() < props.month) {
            if (row.length === 7) {
                rows.push(<TableRow> {row} </TableRow>)
                row = []
            }
            row.push(<TableCell>
                <CalendarDay date={formatDate(date)} dayOfWeek={days[date.getDay()]} day={date.getDate()}/>
            </TableCell>)
            const nextDay = new Date(date)
            nextDay.setDate(date.getDate()+1)
            date = nextDay
        }
        if (row.length < 7) {
            fillAfterMonth(row, date.getDay())
            rows.push(<TableRow> {row} </TableRow>)
        }
        return <TableBody> {rows} </TableBody>
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

export default Calendar