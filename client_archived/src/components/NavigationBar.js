import React from 'react';
import styled from "styled-components";
import {Router, Link} from 'react-router-dom';
import { Navbar } from 'react-bootstrap';
/* This defines the actual bar going down the screen */



export class NavigationBar extends React.Component {
  render() {
    return (
      <nav className="nav flex-column asideNav">
          <Link className="nav-link" to='/'><img src="https://img.icons8.com/officel/25/000000/calendar.png"/>Timetable</Link>
          <Link className="nav-link"to='/Diary'><img src="https://img.icons8.com/dusk/25/000000/book.png"/>Diary</Link>
          <Link className="nav-link"to='/Stats'><img src="https://img.icons8.com/plasticine/25/000000/area-chart.png"/>
Stats</Link>
          <Link className="nav-link"to='/Settings'><img src="https://img.icons8.com/flat_round/25/000000/settings--v1.png"/>Settings</Link>
      </nav>
    );
  }
}
