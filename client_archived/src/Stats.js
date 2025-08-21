import React, { Component, useEffect } from "react";
import Button from 'react-bootstrap/Button'
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import Chart from './components/Chart'
import api from "./api/backendInterface";
import session from "./SessionUtil";
import Alert from "@mui/material/Alert";

function Stats(props) {
    const [from, setFrom] = React.useState(1)
    const [to, setTo] = React.useState(13)
    const [toShow, setToShow] = React.useState(false)
    const [expectedWorkload, setExpectedWorkload] = React.useState({})
    const [plottedWorkload, setPlottedWorkload] = React.useState({})
    const [completedWorkload, setCompletedWorkload] = React.useState({})
    const [toDisplayAlert, setToDisplayAlert] = React.useState(false)
    const [alertMessage, setAlertMessage] = React.useState('')

    useEffect(() => {
        setToDisplayAlert(false)
        setAlertMessage('')
        api.getExpectedWorkload(session.studentId())
            .then(response => {
                if (response.status === 200) {
                    setExpectedWorkload(JSON.parse(response.data))
                } else {
                    setToDisplayAlert(true)
                    setAlertMessage('Internal Server Error')
                }
            })
    }, [])

    useEffect(() => {
        api.getPlottedWorkload(session.studentId())
            .then(response => {
                if (response.status === 200) {
                    setPlottedWorkload(JSON.parse(response.data))
                } else {
                    setToDisplayAlert(true)
                    setAlertMessage('Internal Server Error')
                }
            })
    }, [])

    useEffect(() => {
        api.getCompletedWorkload(session.studentId())
            .then(response => {
                if (response.status === 200) {
                    setCompletedWorkload(JSON.parse(response.data))
                } else {
                    setToDisplayAlert(true)
                    setAlertMessage('Internal Server Error')
                }
            })
    }, [])

    const fromMenu = () => {
        let res = []
        for (let x = 1; x <= 13; x++) {
            res.push("Week " + x)
        }
        return res
    }

    const toMenu = () => {
        let res = []
        for (let x = from; x <= 13; x++) {
            res.push("Week " + x)
        }
        return res
    }

    const handleFromChange = (event)  => {
        const newWeek = event.value.length===6 ? event.value.substring(5,6) : event.value.substring(5,7)
        setFrom(parseInt(newWeek))
    }

    const handleToChange = (event) => {
        const newWeek = event.value.length===6 ? event.value.substring(5,6) : event.value.substring(5,7)
        setTo(parseInt(newWeek))
    }

    const handleShowStats = () => {
        setToShow(true)
    }

    return (
        <div>
            <div style={{marginLeft:'45%', marginTop:'2%'}}>
                <h3 style={{color:'#404040'}}> Stats </h3>
            </div>
            <div style={{marginLeft:'13%', marginTop:'3%'}}>
                <div style={{float:'left', color:'#404040', fontSize:'20px'}}> From </div>
                <div style={{float:'left', marginLeft:'4%', width:'8%'}}> 
                    {<Dropdown options={fromMenu()} value={"Week " + from} onChange={handleFromChange}/>}
                </div>

                <div style={{float:'left', color:'#404040', fontSize:'20px', marginLeft:'5%'}}> To </div> 
                <div style={{float:'left', marginLeft:'4%', width:'8%'}}> 
                    {<Dropdown options={toMenu()} value={"Week " + to} onChange={handleToChange}/>}   
                </div>
                <div style={{float:'left', marginLeft:'4%', width:'8%'}}> <Button variant="outline-dark" onClick={() => handleShowStats()}> Go </Button></div>
                {toDisplayAlert ? <Alert severity="error"> {alertMessage} </Alert> : null}
            </div>
            <div style={{width:'70%', marginLeft:'13%', marginTop:'10%'}}> {toShow ? <Chart plottedWorkload={plottedWorkload} completedWorkload={completedWorkload} expectedWorkload={expectedWorkload} from={from} to={to}/> : null} </div>
        </div>
    )
}

export default Stats