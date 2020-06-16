import * as actionTypes from '../actions/actionTypes';
import { addObject } from '../actions';

const initialState = {
    email: null,
    loginn: null,
    role: '',
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.LOG_IN:
            return {
                ...state,
                email: action.email,
                loginn: true,
                role: action.role,
            }
        case actionTypes.LOG_IN_NOT:
            return {
                ...state,
                loginn: false,
                role: ''

            }
        case actionTypes.ADDED_OBJECT:
            return addObject();
        default:
            return state;
    }
}

export default reducer;