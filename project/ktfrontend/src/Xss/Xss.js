import React from 'react';
import {updateObject} from '../utility';
import Navbar from '../Navbar/Navbar';
import axios from '../axios-objects';


class Xss extends React.PureComponent {
    state = {
        auth: {
            email: ''
        },
        validation: {
            email: true
        },
        firstName: '',
        lastName: '',
        email: ''
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

    loginHandler = async(event) => {
        event.preventDefault();
        const {email} = this.state.auth;
        const emaill = {email};
        const token = sessionStorage.getItem('token');
        
        try {
            const response = await axios.post('/prevent', emaill, {
                headers: {
                    'Authorization' : 'Bearer ' + token
                }
            });
            if (response) {
                this.setState({firstName: response.data.firstName, lastName: response.data.lastName, email: response.data.email})
            }
        } catch(err) {
            console.log(err);
        }
    
    }

    render() {
        return (
            <div className="container_1">
            <Navbar />
                <div className="container-content">
                    <h3>XSS - Search users by email</h3>
                    <div className="login">
                        <div className="box">
                            <label>Email</label>
                            <input type="email" placeholder="Email" 
                            onChange={(event) => this.inputChangdeHandler(event, 'email')}
                            />
                        </div>
                        
                    <a href="/" className="btn_1" onClick={(event) => {this.loginHandler(event); } }>Execute</a>
                        
                    </div>
                    <div className="car-card">
                        <h3>User</h3>
                        <div className="car-card-inner">
                            <div className="right-side">
                                <div>
                                    <div>
                                        <p className="icon-text">{this.state.firstName}</p>
                                    </div>
                                    <div>
                                        <p className="icon-text">{this.state.lastName}</p>
                                    </div>
                                    <div>
                                        <p className="icon-text">{this.state.email}</p>
                                    </div>
                                    <div dangerouslySetInnerHTML={{__html: this.state.firstName}}/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
};


export default Xss;