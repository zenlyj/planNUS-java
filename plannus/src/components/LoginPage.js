import { TextField, Typography } from '@mui/material'
import Box from '@mui/material/Box'
import Grid from '@mui/material/Grid'
import Button from '@mui/material/Button'
import React from 'react'
import api from '../api/backendInterface'
import {Redirect} from 'react-router-dom'
import Alert from '@mui/material/Alert'
import session from '../SessionUtil'

function LoginPage(props) {
    const [isCompleted, setIsCompleted] = React.useState(false)
    const [username, setUsername] = React.useState('')
    const [password, setPassword] = React.useState('')
    const [toDisplayAlert, setToDisplayAlert] = React.useState(false)
    const [alertMessage, setAlertMessage] = React.useState('')

    const login = () => {
        api.authenticateStudent(username, password)
            .then(response => {
                if (response.status === 200) {
                    cache(response)
                } else {
                    setToDisplayAlert(true)
                    setAlertMessage(response.message)
                }
            })
    }

    const cache = (response) => {
        const accessToken = response.access_token
        const refreshToken = response.refresh_token
        api.getStudent(username, accessToken)
            .then(response => {
                if (response.status === 200) {
                    const studentId = JSON.parse(response.data).id
                    session.save('access_token', accessToken)
                    session.save('refresh_token', refreshToken)
                    session.save('student_id', studentId)
                    setIsCompleted(true)
                }
            })
    }

    return (
            <Box 
                display="flex"
                justifyContent="center"
                alignItems="center"
                minHeight="100vh"
            >
                {isCompleted ? <Redirect to="/"/> :
                <Grid minHeight="70vh">
                    <Grid item xs="auto">
                        <Typography variant="h4"> Login </Typography>
                    </Grid>
                    <Grid item xs="auto" sx={{marginTop:'20%'}}>
                        <TextField 
                            id="standard-username-input"
                            label="Username"
                            variant="standard"
                            sx={{width:'35em'}}
                            onChange={e => setUsername(e.target.value)}
                        />
                    </Grid>
                    <Grid item xs="auto" sx={{marginTop:'10%'}}>
                        <TextField
                            id="standard-password-input"
                            label="Password"
                            type="password"
                            variant="standard"
                            sx={{width:'35em'}}
                            onChange={e => setPassword(e.target.value)}
                        />
                    </Grid>
                    <Grid item xs="auto" textAlign={'right'} sx={{marginTop:'5%'}}>
                        <Button onClick={() => login()}>
                            Sign In
                        </Button>
                        {toDisplayAlert ? <Alert severity='error'> {alertMessage} </Alert> : null}
                    </Grid>
                </Grid>
                }
            </Box>
    )
}

export default LoginPage