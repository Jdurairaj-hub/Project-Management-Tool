import React from 'react';
import { Route, Navigate } from 'react-router-dom';

const ProtectedRoute = ({ component: Component, isLoggedIn, ...rest }) => {
  return (
    <Route
      {...rest}
      element={!isLoggedIn ? <Navigate to="/login" replace /> : <Component />}
    />
  );
};

export default ProtectedRoute;
