import React from 'react';
import './Registration.css';
import {updateObject} from '../utility';
import Navbar from '../Navbar/Navbar';
import axios from '../axios-objects';

class Registration extends React.PureComponent {

    constructor(props) {
        super(props);

        this.state = {
            registration: {
                firstName: '',
                lastName: '',
                email: '',
                password: '',
                repeatPassword: ''
            },
            validation: {
                firstName: true,
                lastName: true,
                email: true,
                password: true,
                repeatPassword: true
            },
            registerValidation: true
        }
    }

    validateEmail = (email) => {
        const expression = /^[a-zA-Z0-9]+@(?:[a-zA-Z0-9]+\.)+[A-Za-z]+$/;
    
        return expression.test(String(email).toLowerCase())
    }

    inputChangeHandler = (event, type) => {
        let registration = updateObject(this.state.registration, {
            [type]: event.target.value
        });
        this.setState({registration, registerValidation: true});

        // validacija
        if(event.target.value.length === 0) {
            let validation = updateObject(this.state.validation, {
                [type]: false
            });
            this.setState({validation});
        } else {
            if (type === 'email') {
                let email = this.validateEmail(event.target.value);
                if (email) {
                    let validation = updateObject(this.state.validation, {
                        [type]: true
                    });
                    this.setState({validation});
                } else {
                    let validation = updateObject(this.state.validation, {
                        [type]: false
                    });
                    this.setState({validation});
                }
            } else if (type === 'password') {
                if (event.target.value.length >= 6) {
                    let validation = updateObject(this.state.validation, {
                        [type]: true
                    });
                    this.setState({validation});
                } else {
                    let validation = updateObject(this.state.validation, {
                        [type]: false
                    });
                    this.setState({validation});
                }
            } else if (type === 'repeatPassword') {
                if (event.target.value === this.state.registration.password) {
                    let validation = updateObject(this.state.validation, {
                        [type]: true
                    });
                    this.setState({validation});
                } else {
                    let validation = updateObject(this.state.validation, {
                        [type]: false
                    });
                    this.setState({validation});
                }
            } else {
                let validation = updateObject(this.state.validation, {
                    [type]: true
                });
                this.setState({validation});
            }
        }
    }

    registerHandler = async (event) => {
        event.preventDefault();
        if (this.state.registration.firstName !== '' && this.state.registration.lastName !== '' 
        && this.state.registration.email !== '' && this.state.registration.password !== '' && this.state.registration.repeatPassword !== '') {
            const {firstName, lastName, email, password, repeatPassword} = this.state.registration;
            const reg = {firstName, lastName, email, password, repeatPassword};

            try {
                const response = await axios.post('/registration', reg);
                if (response)
                    this.props.history.push('/login');
            } catch(err) {
                console.log(err);
            }
        } else {
            this.setState({registerValidation: false});
        }
    }

    render() {
        return (
            <div>
                <Navbar />
                <div className="container">
                    <div className="container-content">
                        <h3>Registration</h3>

                            <div className="box">
                                <label>First Name</label>
                                <input type="text" placeholder="First Name" 
                                onChange={(event) => this.inputChangeHandler(event, 'firstName')} />
                                {!this.state.validation.firstName ? <p className="invalid">Please insert your first name.</p> : null}
                            </div>
                            <div className="box">
                                <label>Last Name</label>
                                <input type="text" placeholder="Last Name"
                                onChange={(event) => this.inputChangeHandler(event, 'lastName')} />
                                {!this.state.validation.lastName ? <p className="invalid">Please insert your last name.</p> : null}
                            </div>
                            <div className="box">
                                <label>Email</label>
                                <input type="email" placeholder="Email"
                                onChange={(event) => this.inputChangeHandler(event, 'email')} />
                                {!this.state.validation.email ? <p className="invalid">Please insert valid email address.</p> : null}
                            </div>
                            <div className="box">
                                <label>Password</label>
                                <input type="password" placeholder="Password"
                                onChange={(event) => this.inputChangeHandler(event, 'password')} />
                                {!this.state.validation.password ? <p className="invalid">Minimum 6 characters is required.</p> : null}
                            </div>
                            <div className="box">
                                <label>Repeat Password</label>
                                <input type="password" placeholder="Repeat Password"
                                onChange={(event) => this.inputChangeHandler(event, 'repeatPassword')} />
                                {!this.state.validation.repeatPassword ? <p className="invalid">Please insert correct password.</p> : null}
                            </div>
                            <a href="www.google.com" target="_blank" className="btn" onClick={(event) => this.registerHandler(event)}>Register</a>
                            {!this.state.registerValidation ? <p className="invalid">Please fill all fields.</p> : null}
                    </div>
                </div>
            </div>
        );
    }
}

export default Registration;