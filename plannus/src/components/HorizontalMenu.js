import React from 'react'
import Box from '@mui/material/Box'
import Grid from '@mui/material/Grid'
import { Button, Typography } from '@mui/material'

function HorizontalMenu(props) {
    const [page, setPage] = React.useState(1)
    
    const gridItems = () => {
        const gridItems = []
        for (let i = (page-1)*props.limit; i < props.items.length && i < props.limit*page; i++) {
            gridItems.push(
                <Grid item xs={8/props.limit}> {props.items[i]} </Grid>
            )
        }
        return gridItems
    }

    const handleLeftNav = () => {
        const targetPage = page-1
        setPage(targetPage <= 0 ? 1 : targetPage)
    }

    const handleRightNav = () => {
        const targetPage = page+1
        // round up to multiple of limit
        const upper = Math.ceil(props.items.length/props.limit)*props.limit
        setPage(targetPage*props.limit > upper ? page : targetPage)
    }

    return (
        <Box textAlign='center'>
            <Grid container spacing={2}>
                <Grid item xs={2}>
                    <Button onClick={() => handleLeftNav()}> <Typography variant="button" display="block"> {'<'} </Typography> </Button>
                </Grid>                
                {gridItems()}
                <Grid item xs={2}>
                    <Button onClick={() => handleRightNav()}> <Typography variant="button" display="block"> {'>'} </Typography> </Button>
                </Grid>                
            </Grid>
        </Box>
    )
}

export default HorizontalMenu
