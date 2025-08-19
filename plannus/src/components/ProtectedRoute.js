import React from 'react';
import {Route, Redirect} from 'react-router-dom';
import session from '../SessionUtil';

export const ProtectedRoute = ({component:Component, ...rest}) => {
    return (
      <Route 
      {...rest}
      render = {(props) => session.studentId() ? (<Component {...props}/>) : (<Redirect to="/login"/>)}
      />
    )
  }