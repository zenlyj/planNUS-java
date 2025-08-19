import React, { Component, useEffect } from 'react'
import Box from '@mui/material/Box'
import List from '@mui/material/List'
import api from '../api/backendInterface'
import Deadline from './Deadline'
import ListItem from '@mui/material/ListItem'
import ListItemButton from '@mui/material/ListItemButton'
import ListItemText from '@mui/material/ListItemText'
import Divider from '@mui/material/Divider';

function DeadlineList(props) {  
    const list = () => {
        return (
            <List>
                {props.deadlines.map(deadline => {
                    return (
                        <div>
                            <Deadline
                                id={deadline.id} 
                                name={deadline.name}
                                module={deadline.module}
                                deadline={deadline.deadline}
                                description={deadline.description}
                                isHeader={deadline.isHeader}
                                refresh={props.refresh}
                            />
                            <Divider />
                        </div>
                    )
                })}
            </List>
        )
    }

    return <Box sx={{
                width: '100%', 
                height: '100%', 
                overflow: 'auto', 
                '&::-webkit-scrollbar': {backgroundColor: 'white'},
                '&::-webkit-scrollbar-thumb': {backgroundColor: 'white'}
            }}> 
                {list()} 
            </Box>
}

export default DeadlineList