import React, { Component } from "react";
import Button from 'react-bootstrap/Button'
import Calendar from './components/Calendar'

function Diary(props) {
    const [monthNum, setMonth] = React.useState(8)
    const [months, setMonths] = React.useState(['August', 'September', 'October', 'November'])

    const navMonth = (diff) => {
        const outOfBound = monthNum+diff < 8 || monthNum+diff > 11
        const newMonth =  outOfBound ? monthNum : monthNum+diff
        setMonth(newMonth)
    }

    return (
        <React.Fragment>
            <div style={{marginTop:'2%', marginLeft:'40%', paddingBottom:'1.5%'}}> 
                <Button variant="outline-dark" style={{float:'left', marginRight:"4%"}} onClick={()=>navMonth(-1)}> {'<'} </Button>
                <h3 style={{float:'left', color:'#404040', width:'13%', textAlign:'center'}}> {months[monthNum-8]} </h3> 
                <Button variant="outline-dark" style={{float:'left', marginLeft:'4%'}} onClick={()=>navMonth(1)}> {'>'} </Button>
            </div>
            <div style={{marginLeft:'13%', marginTop:'3%'}}>
                <Calendar month={monthNum} />
            </div>
        </React.Fragment>
    )
}

export default Diary