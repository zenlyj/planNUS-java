import React, { Component, useEffect } from 'react'
import ListItem from '@mui/material/ListItem'
import ListItemButton from '@mui/material/ListItemButton'
import ListItemText from '@mui/material/ListItemText'
import api from '../api/backendInterface'
import DialogUtils from './DialogUtils'
import TextField from '@mui/material/TextField'
import DialogContent from '@mui/material/DialogContent'
import DialogActions from '@mui/material/DialogActions'
import { Button, Typography } from '@mui/material'
import Alert from '@mui/material/Alert'
import session from '../SessionUtil'

function Deadline(props) {
    const [open, setOpen] = React.useState(false)
    const [id, setId] = React.useState(-1)
    const [name, setName] = React.useState('')
    const [module, setModule] = React.useState('')
    const [deadline, setDeadline] = React.useState('')
    const [description, setDescription] = React.useState('')
    const [toDisplayAlert, setToDisplayAlert] = React.useState(false)
    const [alertMessage, setAlertMessage] = React.useState('')

    const handleClickOpen = () => {
        setId(props.id)
        setName(props.name)
        setModule(props.module)
        setDeadline(props.deadline)
        setDescription(props.description)
        setOpen(true)
        setToDisplayAlert(false)
        setAlertMessage('')
    } 

    const handleClose = () => {
        setOpen(false)
        props.refresh()
    }
    
    const handleSave = () => {
        const studentId = session.studentId()
        let response = null
        if (props.isHeader) {
            response = api.addDeadline(studentId, name, module, deadline, description)
        } else {
            response = api.updateDeadline(studentId, id, name, module, deadline, description)
        }
        response.then(response => {
            if (response.status === 200) {
                handleClose()
            } else if (response.status === 500) {
                setToDisplayAlert(true)
                setAlertMessage('Internal Server Error')
            } else {
                setToDisplayAlert(true)
                setAlertMessage(response.message)
            }
        })
    }

    const handleDelete = () => {
        api.deleteDeadline(id).then(response => {
            if (response.status === 200) {
                handleClose()
            }
            console.log(response.message)
        })
    }

    const listItem = () => {
        const primaryText = <Typography noWrap={true} variant="button" display="block"> {props.isHeader ? 'Deadlines' : props.name} </Typography>
        let secondaryText = <div>
            <Typography noWrap={true} variant="button" display="block"> {props.module} </Typography>
            <Typography variant="button" display="block"> {props.deadline} </Typography>
        </div>
        if (props.isHeader) {
            secondaryText = <Typography variant="button" textAlign={'right'} display="block"> + </ Typography>
        }

        return <ListItem>
            <ListItemButton onClick={() => handleClickOpen()}>  
                <ListItemText primary={primaryText} secondary={secondaryText}/>
            </ListItemButton>
        </ListItem>
    }

    return (
        <div>
            {listItem()}
            <DialogUtils.BootstrapDialog
                onClose={handleClose}
                aria-labelledby="customized-dialog-title"
                open={open}
            >
                <DialogUtils.BootstrapDialogTitle id="customized-dialog-title" onClose={handleClose}>
                    { props.isHeader ? 'New Deadline' : props.name }
                </DialogUtils.BootstrapDialogTitle>
                {toDisplayAlert ? <Alert severity='error'> {alertMessage} </Alert> : null}
                <DialogContent dividers>
                    <TextField 
                        autoFocus
                        margin="dense"
                        id="name"
                        label="Name"
                        type="name"
                        fullWidth
                        variant="standard"
                        onChange={e => setName(e.target.value)}
                        disabled={props.disabled}
                        defaultValue={props.name}
                    />
                    <TextField 
                        autoFocus
                        margin="dense"
                        id="module"
                        label="Module"
                        type="module"
                        fullWidth
                        variant="standard"
                        onChange={e => setModule(e.target.value)}
                        disabled={props.disabled}
                        defaultValue={props.module}
                    />
                    <TextField 
                        autoFocus
                        margin="dense"
                        id="deadline"
                        label="Deadline"
                        type="deadline"
                        fullWidth
                        variant="standard"
                        onChange={e => setDeadline(e.target.value)}
                        disabled={props.disabled}
                        defaultValue={props.deadline}
                    />
                    <TextField 
                        autoFocus
                        margin="dense"
                        id="description"
                        label="Description"
                        type="description"
                        fullWidth
                        variant="standard"
                        onChange={e => setDescription(e.target.value)}
                        disabled={props.disabled}
                        defaultValue={props.description}
                    />                    
                </DialogContent>
                {
                    props.disabled ? null :
                    <DialogActions>
                        <Button onClick={() => handleSave()}>
                            Save
                        </Button>
                        {
                            props.isHeader ? null : 
                                <Button onClick={() => handleDelete()}> 
                                    Delete 
                                </Button>
                        }
                    </DialogActions>
                }
            </DialogUtils.BootstrapDialog>
        </div>
    ) 
}

export default Deadline