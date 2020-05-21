import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Registration from './Registration/Registration';
import Login from './Login/Login';
import HomePage from './HomePage/HomePage';
import Certificate from './Certificate/Certificate';
import PrivateRouteAdmin from './PrivateRoute/PrivateRouteAdmin';

class App extends React.PureComponent {
  render() {
    return (
      <div>
        <BrowserRouter>
          <Switch>
            <Route path="/" exact component={HomePage} />
            <Route path="/registration" exact component={Registration} />
            <Route path="/login" exact component={Login} />
            <PrivateRouteAdmin path="/certificate" exact component={Certificate} />
         </Switch>
        </BrowserRouter>
      </div>
    );
  }
}

export default App;