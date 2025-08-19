import React from 'react'
import { BarChart, Bar, Cell, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, Label } from 'recharts'

function Chart(props) {
    const bars = () => {
        const bars = []
        bars.push(<Bar dataKey="expected" fill="#8884d8"/>)
        bars.push(<Bar dataKey="completed" stackId="a" fill="#82ca9d"/>)
        bars.push(<Bar dataKey="plotted" stackId="a" fill="#ffc658" />)
        return bars
    }

    const data = () => {
        const numWeeks = props.to-props.from+1
        const expected = new Map()
        const plotted = new Map()
        const completed = new Map()
        for (const [key, value] of Object.entries(props.expectedWorkload)) {
            expected.set(key, value*numWeeks)
        }
        for (const [key, value] of Object.entries(props.plottedWorkload)) {
            let weekHours = 0
            for (let i = props.from-1; i < props.to; i++) {
                weekHours += value[i]
            }
            plotted.set(key, weekHours)
        }
        for (const [key, value] of Object.entries(props.completedWorkload)) {
            let weekHours = 0
            for (let i = props.from-1; i < props.to; i++) {
                weekHours += value[i]
            }
            completed.set(key, weekHours)
        }
        const data = []
        for (const [key, value] of expected) {
            const expectedHours = value
            const plottedHours = plotted.get(key)
            const completedHours = completed.get(key)
            const dataEntry = {
                name: key,
                'expected': expectedHours,
                'plotted': plottedHours,
                'completed': completedHours
            }
            data.push(dataEntry)
        }
        return data
    }

    return (
        <ResponsiveContainer width="100%" height={500}>
        <BarChart
          width={500}
          height={300}
          data={data()}
          margin={{
            top: 20,
            right: 30,
            left: 20,
            bottom: 5,
          }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis> 
            <Label
                position={'insideLeft'}
                angle={270} 
                value={"Workload (hour)"} 
            /> 
          </YAxis>
          <Tooltip />
          <Legend />
          {bars()}
        </BarChart>
      </ResponsiveContainer>
    )
}

export default Chart