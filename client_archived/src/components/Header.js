import React from 'react';
import { Nav, Navbar } from 'react-bootstrap';
import styled from 'styled-components';
import {Redirect} from 'react-router-dom'
import { useEffect } from 'react';
import session from '../SessionUtil';

function Header(props) {

  return (
      <Navbar expand="lg" className="header">
        <Navbar.Brand href="/">PlanNUS</Navbar.Brand>
          <Nav className="ml-auto" href="/register">
            <Nav.Link href="/register"> Register </Nav.Link>
            {session.studentId() ? <Nav.Link href="/login" onClick={() => session.clear()}> Logout </Nav.Link> : <Nav.Link href="/login"> Login </Nav.Link>}
          </Nav>
      </Navbar>
  )
}

export default Header