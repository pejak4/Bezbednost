import * as actionTypes from '../actions/actionTypes';
import axios from '../../axios-objects';
import jwt_decode from 'jwt-decode';

export const loginSuccess = (email, role) => {
    return {
        type: actionTypes.LOG_IN,
        email: email,
        role: role,
    }
}

export const loginNotSuccess = () => {
    return {
        type: actionTypes.LOG_IN_NOT,
    }
}

export const login = (email, password) => {
    return dispatch => {
        const user = {
            email: email,
            password: password
        };
        const url = '/login';

        axios.post(url, user)
            .then(response => {
                dispatch(loginSuccess(response.data.email, response.data.role));
                const expirationDate = new Date(new Date().getTime() + response.data.expiresIn * 1000);
                sessionStorage.setItem('token', response.data.accessToken);
                const jwtToken = jwt_decode(response.data.accessToken);
                sessionStorage.setItem('role', jwtToken.role);
                sessionStorage.setItem('expirationDate', expirationDate);
                window.location.href = "/";
            })
            .catch(err => {
                dispatch(loginNotSuccess());
            });
    };
};