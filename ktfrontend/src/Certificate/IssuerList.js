import React from 'react';

const IssuerList = props => {

    if (props.issuerData == null)
    {
        return IssuerList;
    }
    
    const issuerData = props.issuerData.map((data, i) => {
        console.log(data);
        if(data.split("+")[3] !== 'End-entity')
            return (
                <option key={i}>{data.split("+")[0]}</option>   
            );
        else
            return null;
    });

    return issuerData
}

export default IssuerList;