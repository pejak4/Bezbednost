import React from 'react';
import {updateObject} from '../utility';
import Navbar from '../Navbar/Navbar';
import axios from '../axios-objects';

class Acl extends React.PureComponent {
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

    aclHandler = async(event) => {
        event.preventDefault();
    
        const token = sessionStorage.getItem('token');
        
        try {
            const response = await axios.get('/acl', {
                headers: {
                    'Authorization' : 'Bearer ' + token
                }
            }
);
            if (response) {
                console.log(response);            
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
                    <h3>Access Control Lists</h3>
                    <div className="login">
                        
                    <a href="/" className="btn_1" onClick={(event) => {this.aclHandler(event); } }>Execute</a>
                        
                    </div>
                
                </div>
            </div>
        );
    }
};


export default Acl;