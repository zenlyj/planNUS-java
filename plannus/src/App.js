import React, { Component } from "react";
import './App.css';
import {
  BrowserRouter as Router,
  Route,
  Switch,
  Link,
  Redirect
} from "react-router-dom";
import axios from "axios";
import Popup from "reactjs-popup";
import Home from './Home';
import Diary from './Diary';
import Stats from './Stats';
import { Settings } from './Settings';
import { NotFound } from './NotFound';
import Header from './components/Header';
import { Layout } from './components/Layout';
import { NavigationBar } from './components/NavigationBar';
import { ProtectedRoute } from "./components/ProtectedRoute";
import LoadingOverlay from 'react-loading-overlay'
import LoginPage from "./components/LoginPage";
import RegisterPage from "./components/RegisterPage"
import session from "./SessionUtil";

class App extends Component {
  constructor() {
    super();
    this.state = {
      loading: false
    };
    this.handleChange = this.handleChange.bind(this);
    this.setLoading = this.setLoading.bind(this);
  }

  handleChange(event) {
    this.setState({
      [event.target.name]: event.target.value
    })
  }

  setLoading(loading) {
    this.setState({
      loading: loading
    });
  }

  render() {
    return (
      <LoadingOverlay
          active={this.state.loading}
          styles={{overlay: (base) => ({...base,
            background:'rgba(0,0,0,0.3)'})}}
          spinner
          overlay
          text='Loading...'>
        <Header />
          <Router>
            <NavigationBar />
            <Layout>
              <Switch>
                <ProtectedRoute exact path="/" component={() => (<Home loading={this.state.loading} setLoading={this.setLoading} /> )}/>
                <ProtectedRoute path="/Diary" component={() => (<Diary />)}/>
                <ProtectedRoute path="/Stats" component={()=> (<Stats />)}/>
                <ProtectedRoute path="/Settings" component={Settings}/>
                <Route path="/Login" component={() => <LoginPage />} />
                <Route path="/Register" component={() => <RegisterPage />}/>
                <Route component={NotFound} />
              </Switch>
            </Layout>
          </Router>
      </LoadingOverlay>
    );
  }
}


export default App;
