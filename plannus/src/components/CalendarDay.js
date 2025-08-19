import React, { useEffect } from 'react'
import Popup from 'reactjs-popup'
import Task from './Task'
import Deadline from './Deadline'
import HorizontalMenu from './HorizontalMenu'
import { Button, Typography } from '@mui/material'
import Box from '@mui/material/Box'
import DialogUtils from './DialogUtils'
import DialogContent from '@mui/material/DialogContent'
import { TextareaAutosize } from '@mui/material'
import Alert from '@mui/material/Alert'
import api from '../api/backendInterface'
import session from '../SessionUtil'

function CalendarDay(props) {
    const [open, setOpen] = React.useState(false)
    const [tasks, setTasks] = React.useState([])
    const [deadlines, setDeadlines] = React.useState([])
    const [id, setId] = React.useState(-1)
    const [note, setNote] = React.useState('')
    const [toDisplayAlert, setToDisplayAlert] = React.useState(false)
    const [alertMessage, setAlertMessage] = React.useState('')

    const getData = () => {
        const studentId = session.studentId()
        api.getsertStudentDiary(studentId, props.date)
        .then(response => {
            if (response.status === 200) {
                const diary = JSON.parse(response.data)
                const tasks = diary.tasks.map(task => {
                    return (
                        <Task 
                            id = {task.id}
                            studentId = {task.studentId}
                            date = {task.date}
                            name = {task.name}
                            module = {task.module}
                            timeFrom = {task.timeFrom}
                            timeTo = {task.timeTo}
                            description = {task.description}
                            isCompleted = {task.isCompleted}
                            taskPresent = {true}
                            disabled = {true}
                            refresh = {getData}
                        />
                    )
                })
                const deadlines = diary.deadlines.map(deadline => {
                    return (
                        <Deadline
                            id = {deadline.id}
                            name = {deadline.name}
                            module = {deadline.module}
                            deadline = {deadline.deadline}
                            description = {deadline.description}
                            isHeader = {false}
                            disabled = {true}
                            refresh = {getData}
                        />
                    )
                })
                setId(diary.id)
                setTasks(tasks)
                setDeadlines(deadlines)
                setNote(diary.note)
            } else {
                setToDisplayAlert(true)
                setAlertMessage('Internal Server Error')
            }
        })
    }

    const handleClickOpen = () => {
        getData()
        setToDisplayAlert(false)
        setAlertMessage('')
        setOpen(true)
    }

    const handleClose = () => {
        const studentId = session.studentId()
        api.updateStudentDiary(id, studentId, props.date, note)
        setOpen(false)
    }

    const dateNumberButtonStyle = {   
        background: 'transparent',
        border: 0,
        width:'100%',
        height:'30px',
        color: '#404040',
        fontSize: 20
    }

    return (
        <div>
            <button style={dateNumberButtonStyle} onClick={() => handleClickOpen()}>
                {props.day}
            </button>
            <DialogUtils.BootstrapDialog
                onClose={handleClose}
                aria-labelledby="customized-dialog-title"
                open={open}
                fullWidth={true}
            >
                <DialogUtils.BootstrapDialogTitle id="customized-dialog-title" onClose={handleClose}>
                    {`${props.dayOfWeek} ${props.date}`}
                </DialogUtils.BootstrapDialogTitle>
                {toDisplayAlert ? <Alert severity='error'> {alertMessage} </Alert> : null}
                <DialogContent dividers>
                    <Box>
                        <Typography sx={{padding:'5%'}} variant="h6">
                            Tasks
                        </Typography>
                        <HorizontalMenu limit={3} items={tasks} />
                    </Box>
                    <Box>
                        <Typography sx={{padding:'5%'}} variant="h6">
                            Deadline
                        </Typography>
                        <HorizontalMenu limit={2} items={deadlines}/>
                    </Box>
                    <Box>
                        <TextareaAutosize
                            aria-label="minimum height"
                            minRows={3}
                            placeholder='Diary Entry'
                            style={{width:'60%', marginTop:'5%', marginLeft:'5%'}}
                            defaultValue={note}
                            onChange={e => setNote(e.target.value)}
                        />
                    </Box>
                </DialogContent>
            </DialogUtils.BootstrapDialog>
        </div>
    )
}

export default CalendarDay