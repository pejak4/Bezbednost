import React from 'react';
import './Login.css';
import {updateObject} from '../utility';
import {connect} from 'react-redux';
import * as actions from '../store/actions/index';
import Navbar from '../Navbar/Navbar';

class Login extends React.PureComponent {
    state = {
        auth: {
            email: '',
            password: ''
        },
        validation: {
            email: true,
            password: true
        }
    }

    inputChangdeHandler = (event, type) => {
        let auth = updateObject(this.state.auth, {
        [type]: event.target.value
    });
        this.setState({auth});

        if(event.target.value.length === 0) {
            let validation = updateObject(this.state.validation, {
                [type]: false
            });
            this.setState({validation});
        } else {
            let validation = updateObject(this.state.validation, {
                [type]: true
            });
            this.setState({validation});
        }
    }

    loginHandler = (event) => {
        event.preventDefault();
        if(this.state.validation.email && this.state.validation.password)
            this.props.onLogin(this.state.auth.email, this.state.auth.password);
    }

    render() {
        return (
            <div className="container_1">
            <Navbar />
                <div className="container-content">
                    <h3>Login</h3>
                    <div className="login">
                        <div className="box">
                            <label>Email</label>
                            <input type="email" placeholder="Email" 
                            onChange={(event) => this.inputChangdeHandler(event, 'email')}
                            />
                        </div>
                        <div className="box">
                            <label>Password</label>
                            <input type="password" placeholder="Password" 
                            onChange={(event) => this.inputChangdeHandler(event, 'password')}
                            />
                        </div>
                    <a href="/" className="btn_1" onClick={(event) => {this.loginHandler(event); } }>Login</a>
                        <p>
                            Click <a className="a_a" href="/registration" target="_blank" >here</a> to register
                        </p>
                    </div>
                    <div className="invalid_1">
                        {(!this.state.validation.email || !this.state.validation.password) ? 
                            <p>Enter all fields</p> : null}
                    </div>
                </div>
            </div>
        );
    }
};

const mapStateToProps = state => {
    return {
        email: state.auth.email,
        loginn: state.auth.loginn
    }
}

const mapDispatchToProps = dispatch => {
    return {
        onLogin: (email, password) => dispatch(actions.login(email, password))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Login);