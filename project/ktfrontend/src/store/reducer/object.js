import * as actionTypes from '../actions/actionTypes';
import {updateObject} from '../../utility';



const initialState = {
    objects: [],
    added: false,
    getObjects: false
};

const initObjects = (state, action) => {
    return updateObject(state, {
        getObjects: false,
        added: false
    });
};

const successObjects = (state, action) => {
    return updateObject(state, {
        objects: action.objects,
        getObjects: true,
        added: false
    });
};

const initAddObject = (state, action) => {
    return updateObject(state, {
        added: false
    });
};

const addObject = (state, action) => {
    return updateObject(state, {
        name: action.name,
        title: action.title
    });
};

const addedObject = (state, action) => {
    return updateObject(state, {added: true});
}

const reducer = (state = initialState, action) => {
    switch(action.type) {
        case actionTypes.INIT_OBJECT:
            return initObjects(state, action);
        case actionTypes.GET_OBJECTS:
            return successObjects(state, action);
        case actionTypes.INIT_ADD:
            return initAddObject(state, action);
        case actionTypes.ADD_OBJECT: 
            return addObject(state, action);
        case actionTypes.ADDED_OBJECT:
            return addedObject(state, action);
        default:
            return state;
    }
};

export default reducer;