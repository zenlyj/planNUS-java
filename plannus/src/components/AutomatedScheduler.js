import React, { Component } from 'react'
import PropTypes from "prop-types";
import Popup from 'reactjs-popup'
import Button from 'react-bootstrap/Button'
import Tabs from 'react-bootstrap/Tabs'
import Tab from 'react-bootstrap/Tab'
import InputRange from 'react-input-range'
import AutoComplete from './AutoComplete'
import PulseLoader from 'react-spinners/PulseLoader'

class FormTab extends Component {
    constructor(props) {
        super(props)
    }
    render() {
        return (
        <Tabs defaultActiveKey="Monday" id="uncontrolled-tab-example">
            {this.props.formTabData.days.map(day => {
                return (
                    <Tab key={day} eventKey={day} title={day}>
                    <table align="left" className="AutomatedForm">
                        <tbody>
                            <tr>
                                <td colSpan='1'>Start</td>
                                <td colSpan='4'><input name={[day] + '_start'} value={this.props.formTabData[day + "_start"]} onChange={(e) => this.props.updateState({
                                        [day + "_start"]: e.target.value
                                    })}/></td>
                                <td colSpan='1'></td>
                                <td colSpan='1'>End</td>
                                <td colSpan='4'><input name={[day] + '_end'} value={this.props.formTabData[day + "_end"]} onChange={(e) => this.props.updateState({
                                        [day + "_end"]: e.target.value
                                    })}/></td>
                            </tr>
                            <tr className="timeSlider">
                                <td colSpan='1'>Workload (hrs):</td>
                                    <td colSpan='10' style={{padding: 20 + 'px'}}>
                                    <InputRange className="timeSlider" maxValue={20} minValue={0} value={this.props.formTabData[day]} onChange={(value) => this.props.updateState({
                                        [day]: value
                                    })} />
                                </td>
                                <td colSpan='1'><input type='hidden' name={[day] + '-hours'} value={this.props.formTabData[day]} /></td>
                            </tr>
                            <tr>
                                <td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
                            </tr>
                        </tbody>
                    </table>
                    </Tab>
                )
            })}
        </Tabs>
        )
    }
}

class AddedModules extends Component {
    constructor(props) {
        super(props);
        this.handleChange = this.handleChange.bind(this)
    }

    handleChange(e) {
        this.props.onChange(e.currentTarget.innerText);
    }

    render() {
        return this.props.modules.map((mc) =>
        <Button className="btn-success modules" key={mc} onClick={this.handleChange}>{mc}</Button>
      )
    }
}

class AutomatedScheduler extends Component {
    static propTypes = {
        defaultModules: PropTypes.instanceOf(Array)
    };

    static defaultProps = {
        defaultModules: []
    };

    constructor(props) {
        super(props);
        this.state = {
            modules: [],
            distinctmodules: [],
            week: this.props.id,
            open: false,
            loading: false,
            calculatedWorkload: 0,
            formTabData: {
                days: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
                Monday: 8,
                Monday_start: '0900',
                Monday_end: '1900',
                Tuesday: 8,
                Tuesday_start: '0900',
                Tuesday_end: '1900',
                Wednesday: 8,
                Wednesday_start: '0900',
                Wednesday_end: '1900',
                Thursday: 8,
                Thursday_start: '0900',
                Thursday_end: '1900',
                Friday: 8,
                Friday_start: '0900',
                Friday_end: '1900',
                Saturday: 7,
                Saturday_start: '0900',
                Saturday_end: '1900',
                Sunday: 3,
                Sunday_start: '0900',
                Sunday_end: '1900'
            }
        };
        this.handleChange = this.handleChange.bind(this);
        this.addModule = this.addModule.bind(this);
        this.removeModule = this.removeModule.bind(this);
        this.openModal = this.openModal.bind(this);
        this.closeModal = this.closeModal.bind(this);
        this.updateState = this.updateState.bind(this);
        this.resetState = this.resetState.bind(this);
        this.handleAutomate = this.handleAutomate.bind(this);
    }
    
    handleAutomate() {
        this.props.automateSchedule(this.state);
        this.resetState();
        this.setState({open: false});
        
    }

    resetState() {
        this.setState({
            modules: [],
            week: this.props.id,
            open: true,
            calculatedWorkload: 0,
            formTabData: {
                days: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
                Monday: 8,
                Monday_start: '0900',
                Monday_end: '1900',
                Tuesday: 8,
                Tuesday_start: '0900',
                Tuesday_end: '1900',
                Wednesday: 8,
                Wednesday_start: '0900',
                Wednesday_end: '1900',
                Thursday: 8,
                Thursday_start: '0900',
                Thursday_end: '1900',
                Friday: 8,
                Friday_start: '0900',
                Friday_end: '1900',
                Saturday: 7,
                Saturday_start: '0900',
                Saturday_end: '1900',
                Sunday: 3,
                Sunday_start: '0900',
                Sunday_end: '1900'
            }
        });
    }
    
    openModal() {
        this.setState({loading:true});
        // nusmodsAPI.retrieveDistinctModule(this.state.week == undefined ? 1 : this.state.week).then(result => this.setState({distinctmodules: result, loading:false}));
        this.setState({ open: true });
    }
    
    closeModal() {
        this.setState({ open: false });
    }

    handleChange(event) {

    }

    addModule(mc) {
        this.setState({loading:true});
        if (this.state.modules.indexOf(mc) > -1) {
            
        } else {
            let newModules = this.state.modules;
            newModules.push(mc);
            this.setState({
                modules: newModules
            });
        }
        // nusmodsAPI.calculateWorkload(this.state.week, this.state.modules).then(hrs => {this.setState({calculatedWorkload: hrs, loading: false})});
    }

    removeModule(mc) {
        this.setState({loading:true});
        let newModules = this.state.modules.filter(p => p != mc);
        this.setState({
            modules: newModules
        });
        // nusmodsAPI.calculateWorkload(this.state.week, newModules).then(hrs => this.setState({calculatedWorkload: hrs, loading: false}));
    }

    updateState(newState) {
        let newFormTabData = this.state.formTabData;
        for (var key in newState) {
            newFormTabData[key] = newState[key];
        }
        this.setState({formTabData: newFormTabData});
    }

    render() {
        const buttonStyle = {   
            background: 'transparent',
            border: 0,
            width:'100%',
            height:'30px',
            color: 'white',
            fontSize: 20
        }

        const modalStyle = {
            font: '12px'
        }
        
        const headerStyle = {
            color: 'black',
            width: '100%',
            borderBottom: '1px solid gray',
            fontSize: '18px',
            textAlign: 'center',
            padding: '5px'
        }

        const contentStyle = {
            color: 'black',
            width: '100%',
            padding: '10px 50px'
        }

        const closeStyle = {
            color: 'black',
            cursor: 'pointer',
            position: 'absolute',
            display: 'block',
            padding: '2px 5px',
            lineHeight: '20px',
            right: '-10px',
            top: '-10px',
            fontSize: '24px',
            background: '#ffffff',
            borderRadius: '18px',
            border: '1px solid #cfcece'
        }

        return (
            <div>
                <Button className="btn-home" onClick={this.openModal}>Automated Scheduler</Button>
                <Popup 
                    open={this.state.open}
                    closeOnDocumentClick
                    modal
                    onClose={this.closeModal}
                >
                    <div>
                        <a style={closeStyle} onClick={this.closeModal}>
                            &times;
                        </a>
                        <div style={headerStyle}> Automated Scheduler </div>
                        <div style={contentStyle}>
                            <form id="autoschedule">
                                <table align="left" className="AutomatedForm">
                                    <tbody>
                                        <tr><td colSpan='1'></td><td colSpan='1'>Calculated Workload:</td><td colSpan='4'>{!this.state.loading ? <input value={this.state.calculatedWorkload} readOnly />: <PulseLoader color={"#acacac"} loading={this.state.loading} />}</td><td colSpan='6'></td></tr>
                                        <tr><td colSpan='1'></td><td colSpan='1'>Modules:</td><td colSpan='4'><AutoComplete suggestions={this.state.distinctmodules} onChange={this.addModule} /></td><td colSpan='6'></td></tr>
                                        <tr><td colSpan='2'></td><td colSpan='9'><AddedModules modules={this.state.modules} onChange={this.removeModule}/></td><td><input type="hidden" name="modules" value={this.state.modules} /></td></tr>
                                        <tr><td colSpan='1'></td><td colSpan='10'><FormTab formTabData={this.state.formTabData} updateState={this.updateState}/></td><td colSpan='1'></td></tr>
                                        <tr><td colSpan='7'></td><td colSpan='2'><Button onClick={this.resetState} className="fullButton btn-secondary">Reset</Button></td><td colSpan='2'><Button onClick={this.handleAutomate} className="fullButton btn-success">Automate</Button></td><td colSpan='1'></td></tr>
                                    </tbody>
                                </table>
                            </form>
                        </div>
                        
                    </div>

                </Popup>
            </div>
        )
    }
}

export default AutomatedScheduler