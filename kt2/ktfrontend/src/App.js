import React from 'react';
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import Registration from './Registration/Registration';
import Login from './Login/Login';
import HomePage from './HomePage/HomePage';
import PrivateRouteAdmin from './PrivateRoute/PrivateRouteAdmin';
import Sqlinject from './Sqlinjection/Sqlinject';
import Xss from './Xss/Xss';
import Acl from './Acl/Acl';

class App extends React.PureComponent {
  render() {
    return (
      <div>
        <BrowserRouter>
          <Switch>
            <Route path="/" exact component={HomePage} />
            <Route path="/registration" exact component={Registration} />
            <Route path="/login" exact component={Login} />
            <Route path="/sqll" exact component={Sqlinject} />
            <PrivateRouteAdmin path="/xss" exact component={Xss} />
            <Route path="/acl" exact component={Acl} />
         </Switch>
        </BrowserRouter>
      </div>
    );
  }
}

export default App;