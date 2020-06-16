import axios from '../../axios-objects';
import * as actionTypes from './actionTypes';

export const initAddObject = () => {
    return {
        type: actionTypes.INIT_ADD
    };
};

export const addedObject = () => {
    return {
        type: actionTypes.ADDED_OBJECT
    };
};

export const addObject = (objData) => {
    return dispatch => {
        dispatch(initAddObject());
        axios.post('/objects.json', objData)
            .then(response => {
                console.log(response);
                dispatch(addedObject());
            })
            .catch(error => {
                console.log(error);
            });
    };
};

export const initObjects = () => {
    return {
        type: actionTypes.INIT_OBJECT
    };
};

export const successObjects = (objects) => {
    return {
        type: actionTypes.GET_OBJECTS,
        objects: objects
    }
}

export const fetchObjects = () => {
    return dispatch => {
        dispatch(initObjects());
        axios.get('/objects.json')
            .then(response => {
                const objects = [];
                for (let key in response.data) {
                    objects.push({
                        ...response.data[key],
                        id: key
                    });
                }
                dispatch(successObjects(objects));
            })
            .catch(err => {
                console.log(err);
            });
    };
};