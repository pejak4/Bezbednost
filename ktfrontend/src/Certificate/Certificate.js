import React from 'react';
import Navbar from '../Navbar/Navbar';
import './Certificate.css';
import DownBar from '../Navbar/DownBar';
import {updateObject} from '../utility';
import axios from '../axios-objects';
import IssuerList from './IssuerList';

class Certificate extends React.PureComponent {

    state = {
        view: '',
        auth: {
            fullname: '',
            surname: '',
            givenname: '',
            e: '',
            uid: '',
            speciality: 'CA',
            issuerData: 'Pera Peric(UID=0001)',
            startDate: '',
            endDate: '',
            alias: ''
        },
        checkResponse: null,
        checkUid: null,
        rootCertificate: false,
        startDate: '2020-05-19',
        endDate: '2020-05-31',
        validationUid: true,
        validationAlias: true,
        validationStartDate: true,
        validationStartIssuerDate: true,
        validationEndDate: true,
        validationEndIssuerDate: true,
        validationEqualDate: true,
        certificatesUID: [],
        issuerData: [],
        filteredCertificates: [],
        aliases: [],
        certificatesForCardList: [],
        todayDate: '2020-05-19',

        
        api_text_render: '',
        api_uid:'',
        api_style: 'white'

    }

    componentDidMount = async() => {
        try {
            const response = await axios.get('/getAllCertificates');
            if (response) {
                this.setState({issuerData: response.data});
                this.sendCertificateForValidation(response.data);
                let uids = [];
                for(let i = 0; i < response.data.length; i++) {
                    uids.push(response.data[i].split("(")[1].split("=")[1].split(")")[0].toString());
                }
                this.setState({certificatesUID: uids});
            }
        } catch(err) {
            console.log(err);
        }

        try{
            const response = await axios.get('/getAllAliases');
            if(response)
                this.setState({aliases: response.data});
        }catch(err) {
            console.log(err);
        }

        try{
            const response = await axios.get('/getAllCertificatesForCardList');
            if(response)
                this.formatDataForCardListCert(response.data);
        }catch(err) {
            console.log(err);
        }
    }

    sendCertificateForValidation = async(data) => {
        let filteredCertificates = [];
        for(let i = 0; i < data.length; i++) {
            const uidd = data[i].split("+")[0].split("=")[1].split(")")[0] + "=/";
            try {
                const response = await axios.post('/checkCertificateOcsp', uidd);
                if(response) {
                    filteredCertificates.push(response.data);
                }
            } catch(err) {
                console.log(err);
            }
        }
        
        let filtCert = [];
        for(let i = 0; i < filteredCertificates.length; i++) {
            for(let j = 0; j < this.state.issuerData.length; j++) {
                if(!filteredCertificates[i].check && filteredCertificates[i].uid === this.state.issuerData[j].split("+")[0].split("=")[1].split(")")[0]) {
                    const date = this.dateFormatHandler(this.state.issuerData[j].split("+")[1]);
                    if(date <= this.state.todayDate) {
                        filtCert.push(this.state.issuerData[j]);
                    }
                }
            }
        }
        this.setState({filteredCertificates: filtCert});
    }

     formatDataForCardListCert = (data) => {
        let pom = [];
        for (let i=0; i<data.length; i++) {
            pom.push(data[i].split(',')[0] + "+" + 
            data[i].split(',')[1] + "+" + 
            data[i].split(',')[5].split('+')[0] + "+" + 
            this.dateFormatHandler(data[i].split(',')[5].split('+')[1]) + "+" + 
            this.dateFormatHandler(data[i].split(',')[5].split('+')[2]));
        }
        this.setState({certificatesForCardList: pom});
     }

     dateFormatHandler = (date) => {
        let split = date.split(" ");
        let month;
        if(split[1] === 'Jan')
            month = '01';
        else if(split[1] === 'Feb')
            month = '02';
        else if(split[1] === 'Mar')
            month = '03';
        else if(split[1] === 'Apr')
            month = '04';
        else if(split[1] === 'May')
            month = '05';
        else if(split[1] === 'Jun')
            month = '06';
        else if(split[1] === 'Jul')
            month = '07';
        else if(split[1] === 'Aug')
            month = '08';
        else if(split[1] === 'Sep')
            month = '09';
        else if(split[1] === 'Oct')
            month = '10';
        else if(split[1] === 'Nov')
            month = '11';
        else if(split[1] === 'Dec')
            month = '12';
        let split1 = split[5] + "-" + month + "-" + split[2];
        return split1;
    }

    clickView = (text) => { // U zavisnoti od buttona koji je kliknut na donjem navbaru menja se stanje View
        this.setState({view: text});
    }

    inputChangdeHandlerForApi = (event) => {
        console.log(event.target.value);
        this.setState({api_uid: event.target.value})
    }

    checkIdForApiHandler = async(event) => {
        event.preventDefault();
        try {
            if(this.state.api_uid !== '') {
                const response = await axios.post('/checkCertificateOcsp', this.state.api_uid);

                if(response) {
                    if(response.data.uid === null)
                        this.setState({api_text_render: "Certificate is not found", api_style:'white'})
                    else if(response.data.check)
                        this.setState({api_text_render: "Certificate is invalid", api_style:'red'})
                    else if(!response.data.check)
                        this.setState({api_text_render: "Certificate is valid", api_style:'green'})
                    

                    console.log(response.data);
                }
            } else {
                this.setState({api_text_render: "The field is empty", api_style:'white'})
            }
        } catch(err) {
            console.log(err);
        }
    }

    inputChangdeHandler = (event, type) => {
        let auth = updateObject(this.state.auth, {
            [type]: event.target.value
        });
        this.setState({auth});

        //specijalnost sertifikata
        if(type === 'speciality')
            if(event.target.value === 'Root')
                this.setState({rootCertificate: true});
            else
                this.setState({rootCertificate: false});

        //validacija za datume u odnosu na issuer-a
        //validacija da kranji datum mora biti posle pocetnog i da ne smeju biti istog dana
        if(type === 'startDate' || type === 'endDate')
            this.validateDate(event.target.value, type);


        //snimanje datuma od issuer-a
        if (type === 'issuerData') {
            let startDateSplit;
            let endDateSplit;
            for(let i = 0; i < this.state.issuerData.length; i++)
                if(event.target.value === this.state.issuerData[i].split("+")[0]) {
                    startDateSplit = this.dateFormatHandler(this.state.issuerData[i].split("+")[1]);
                    endDateSplit = this.dateFormatHandler(this.state.issuerData[i].split("+")[2]);
                    this.setState({startDate: startDateSplit, 
                    endDate: endDateSplit});
                }
            this.validateDateFromIssuer(this.state.auth.startDate, 'startDate', startDateSplit, endDateSplit);
            this.validateDateFromIssuer(this.state.auth.endDate, 'endDate', startDateSplit, endDateSplit);
        }

        //uid mora biti jedinstven
        if(type === 'uid')
            for(let i = 0; i < this.state.certificatesUID.length; i++)
                if(event.target.value === this.state.certificatesUID[i]) {
                    this.setState({validationUid: false});
                    break;
                }
                else {
                    this.setState({validationUid: true});
                }
        
        //alias mora biti jedinstven
        if(type === 'alias'){
            for (let i=0; i<this.state.aliases.length; i++){
                if (event.target.value === this.state.aliases[i]){
                    this.setState({validationAlias: false});
                    break;
                }
                else {
                    this.setState({validationAlias: true})
                }
            }
        }
    }

    validateDateFromIssuer = (newDate, type, issuerStartDate, issuerEndDate) => {
        if(type === 'startDate')
            if(newDate < issuerStartDate)
                this.setState({validationStartIssuerDate: false});
            else
                this.setState({validationStartIssuerDate: true});
        if(type === 'endDate')
            if(newDate > issuerEndDate)
                this.setState({validationEndIssuerDate: false});
            else
                this.setState({validationEndIssuerDate: true});

        if(type === 'endDate' && this.state.auth.startDate !== '')
            if(newDate < this.state.auth.startDate)
                this.setState({validationEndDate: false});
            else if(newDate === this.state.auth.startDate)
                this.setState({validationEqualDate: false});
            else
                this.setState({validationEndDate: true, validationEqualDate: true});
        
        if(type === 'startDate' && this.state.auth.endDate !== '')
            if(newDate > this.state.auth.endDate)
                this.setState({validationStartDate: false});
            else if(newDate === this.state.auth.endDate)
                this.setState({validationEqualDate: false});
            else
                this.setState({validationStartDate: true, validationEqualDate: true});
    }

    validateDate = (newDate, type) => {
        if(type === 'startDate')
            if(newDate < this.state.startDate)
                this.setState({validationStartIssuerDate: false});
            else
                this.setState({validationStartIssuerDate: true});
        if(type === 'endDate')
            if(newDate > this.state.endDate)
                this.setState({validationEndIssuerDate: false});
            else
                this.setState({validationEndIssuerDate: true});

        if(type === 'endDate' && this.state.auth.startDate !== '')
            if(newDate < this.state.auth.startDate)
                this.setState({validationEndDate: false});
            else if(newDate === this.state.auth.startDate)
                this.setState({validationEqualDate: false});
            else
                this.setState({validationEndDate: true, validationEqualDate: true});
        
        if(type === 'startDate' && this.state.auth.endDate !== '')
            if(newDate > this.state.auth.endDate)
                this.setState({validationStartDate: false});
            else if(newDate === this.state.auth.endDate)
                this.setState({validationEqualDate: false});
            else
                this.setState({validationStartDate: true, validationEqualDate: true});
    }

    addCertificateHandler = (event) => {
        event.preventDefault();

        if (this.state.auth.fullname !== '' && this.state.auth.surname !== ''
        && this.state.auth.givenname !== '' && this.state.auth.e !== '' && this.state.auth.c !== '' && 
        this.state.auth.uid !== '' && this.state.issuerData !== '' && this.state.auth.startDate !== '' 
        && this.state.auth.endDate !== '' && this.state.auth.alias !== '' && this.state.validationUid && this.state.validationAlias
        && this.state.validationStartDate && this.state.validationEndDate && this.state.validationEqualDate
        && this.state.validationStartIssuerDate && this.state.validationEndIssuerDate)
        {
            const {fullname, surname, givenname, e, c, uid, issuerData, startDate, endDate, alias, speciality} = this.state.auth;
            const subjectCertificate = {fullname, surname, givenname, e, c, uid, issuerData, startDate, endDate, alias, speciality};

            try {
                const response = axios.post('/addCertificate', subjectCertificate);
                if (response)
                    window.location.reload();
            } catch(err) {
                console.log(err);
            }
        }
    }

    checkCertificateHandler = async(event, uid) => {
        event.preventDefault();
        try {
            const response = await axios.post('/checkCertificateOcsp', uid);

            if(response) {
                this.setState({checkResponse: response.data.check, checkUid: response.data.uid});
            }
        } catch(err) {
            console.log(err);
        }
    }

    pullCertificateHandler = async(event, uid) => {
        event.preventDefault();
        try {
            const response = await axios.post('/pullCertificateOcsp', uid);
            if(response) {
                window.location.reload();
            }
        } catch(err) {
            console.log(err);
        }
    }

    downloadCertificateHandler = async(event, uid) => {
        event.preventDefault();

        try {
            const response = await axios.post('/downloadCertificate', uid);
            if(response) {                
                const element = document.createElement("a");
                const file = new Blob([response.data],    
                            {type: 'text/plain;charset=utf-8'});
                element.href = URL.createObjectURL(file);
                element.download = "myFile.crt";
                document.body.appendChild(element);
                element.click();
            }
        } catch(err) {
            console.log(err);
        }
    }


    renderView() {
        if (this.state.view === 'NEW')
        {
            return (
                <div>
                    <section id="section">
                        <div>
                            <div className="box">
                                <label>CN</label>
                                <input type="text" placeholder="Fullname" 
                                    onChange={(event) => this.inputChangdeHandler(event, 'fullname')}
                                />
                            </div>
                            <div className="box">
                                <label>SURNAME</label>
                                <input type="text" placeholder="Surname"
                                    onChange={(event) => this.inputChangdeHandler(event, 'surname')}
                                />
                            </div>
                            <div className="box">
                                <label>GIVENNAME</label>
                                <input type="text" placeholder="Givenname" 
                                    onChange={(event) => this.inputChangdeHandler(event, 'givenname')}
                                />
                            </div>
                            <div className="box">
                                <label>E</label>
                                <input type="email" placeholder="Email"
                                    onChange={(event) => this.inputChangdeHandler(event, 'e')}
                                />
                            </div>
                            <div className="box">
                                <label>Speciality</label>
                                <select className="ca-select" defaultValue="CA"
                                    onChange={(event) => this.inputChangdeHandler(event, 'speciality')}>
                                        <option>Root</option>
                                        <option>CA</option>
                                        <option>End-entity</option>
                                </select>
                            </div>
                        </div>
                        <div>
                            <div className="box">
                                <label>UID</label>
                                <input type="text" placeholder="UID"
                                    onChange={(event) => this.inputChangdeHandler(event, 'uid')}
                                />
                                {this.state.validationUid ? null : <p className="invalid">That UID exists already</p>}
                            </div>
                            <div className="box">
                                <label>Start date</label>
                                <input type="date" onChange={(event) => this.inputChangdeHandler(event, 'startDate')} />
                                {this.state.validationStartDate ? null : <p className="invalid">Start date must be before end date.</p>}
                                {this.state.validationStartIssuerDate ? null : <p className="invalid">Start date must be after issuer start date</p>}
                            </div>
                            <div className="box">
                                <label>End date</label>
                                <input type="date" onChange={(event) => this.inputChangdeHandler(event, 'endDate')}/>
                                {this.state.validationEndDate ? null : <p className="invalid">End date must be after start date.</p>}
                                {this.state.validationEqualDate ? null : <p className="invalid">Dates can't be the same.</p>}
                                {this.state.validationEndIssuerDate ? null : <p className="invalid">End date must be before issuer end date</p>}
                            </div>
                            <div className="box">
                                <label>Alias</label>
                                <input type="text" placeholder="Alias" onChange={(event) => this.inputChangdeHandler(event, 'alias')}/>
                                {this.state.validationAlias ? null : <p className="invalid">That ALIAS exists already</p>}
                            </div>
                            {!this.state.rootCertificate ?
                            <div className="box">
                                <label>CA ISSUER</label> 
                                <select className="ca-select" defaultValue="Pera Peric(UID=0001)"
                                 onChange={(event) => this.inputChangdeHandler(event, 'issuerData')}>
                                    <IssuerList issuerData={this.state.filteredCertificates}/>
                                </select>
                            </div> : null}
                        </div>
                    </section>
                    <a href="/" className="btn_1" onClick={(event) => {this.addCertificateHandler(event); } }>Add</a>
                </div>
            );
        }
        if (this.state.view === 'VIEW')
        {
            return (
                <div >
                    {this.state.certificatesForCardList.map((el) => {
                        if(el.split("+")[0].split("=")[1] === this.state.checkUid && this.state.checkResponse) {
                            return (
                                <div className="certificate-box-invalid" key={el.split("+")[0].split("=")[1]}>
                                    <div className="certificate-box-container">
                                        <p>For: {el.split("+")[2].split("=")[1]}</p>
                                        <p>Speciality: {el.split("+")[1].split("=")[1]}</p>
                                        <p>Valid(from): {el.split("+")[3]}</p>
                                        <p>Valid(to): {el.split("+")[4]}</p>
                                        <p>UID: {el.split("+")[0].split("=")[1]}</p>
                                        {this.state.checkResponse === true ? <p className="invalid-cert">Certificate is invalid!</p> : null}
                                        <br/>
                                        <a href="/" onClick={(event) => this.checkCertificateHandler(event, el.split("+")[0].split("=")[1])}>Check</a>
                                        <a href="/" onClick={(event) => this.pullCertificateHandler(event, el.split("+")[0].split("=")[1])}>Pull</a>
                                        <a href="/" onClick={(event) => this.downloadCertificateHandler(event, el.split("+")[0].split("=")[1])}>Download</a>  
                                    </div>
                                </div>
                            );
                        } else if (el.split("+")[0].split("=")[1] === this.state.checkUid && !this.state.checkResponse) {
                            return (
                                <div className="certificate-box-valid" key={el.split("+")[0].split("=")[1]}>
                                    <div className="certificate-box-container">
                                        <p>For: {el.split("+")[2].split("=")[1]}</p>
                                        <p>Speciality: {el.split("+")[1].split("=")[1]}</p>
                                        <p>Valid(from): {el.split("+")[3]}</p>
                                        <p>Valid(to): {el.split("+")[4]}</p>
                                        <p>UID: {el.split("+")[0].split("=")[1]}</p>
                                        {this.state.checkResponse === false ? <p className="valid-cert">Certificate is valid!</p> : null}
                                        <br/>
                                        <a href="/" onClick={(event) => this.checkCertificateHandler(event, el.split("+")[0].split("=")[1])}>Check</a>
                                        <a href="/" onClick={(event) => this.pullCertificateHandler(event, el.split("+")[0].split("=")[1])}>Pull</a>
                                        <a href="/" onClick={(event) => this.downloadCertificateHandler(event, el.split("+")[0].split("=")[1])}>Download</a>  
                                    </div>
                                </div>
                            );
                        } else {
                            return (
                                <div className="certificate-box" key={el.split("+")[0].split("=")[1]}>
                                    <div className="certificate-box-container">
                                        <p>For: {el.split("+")[2].split("=")[1]}</p>
                                        <p>Speciality: {el.split("+")[1].split("=")[1]}</p>
                                        <p>Valid(from): {el.split("+")[3]}</p>
                                        <p>Valid(to): {el.split("+")[4]}</p>
                                        <p>UID: {el.split("+")[0].split("=")[1]}</p>
                                        <br/>
                                        <a href="/" onClick={(event) => this.checkCertificateHandler(event, el.split("+")[0].split("=")[1])}>Check</a>
                                        <a href="/" onClick={(event) => this.pullCertificateHandler(event, el.split("+")[0].split("=")[1])}>Pull</a>
                                        <a href="/" onClick={(event) => this.downloadCertificateHandler(event, el.split("+")[0].split("=")[1])}>Download</a>                                      </div>
                                </div>
                            );
                        }
                    })}
                </div>
            );
        }
        if(this.state.view === "API") {
            return(
                <div className="certificate-box">
                    <div className="certificate-box-container">
                        <br/><br/>
                        <label>Check id</label>
                        <input type="text" style={{width:'220px'}} placeholder='ID'
                            onChange={(event) => this.inputChangdeHandlerForApi(event)} />

                        <br/><br/>
                            <p style={{color:this.state.api_style}}>{this.state.api_text_render}</p>
                        <br/><br/>
                        <a href="/" className="btn_2" onClick={(event) => {this.checkIdForApiHandler(event); } }>Check</a>
                        
                    </div>
                </div>
            );
        }
    }

    render() {
        return (
            <div className="certificate-container">
                <Navbar/>
                <DownBar clickView = {this.clickView}/>
                <div className="centered">
                    {this.renderView()}
                </div>
            </div>
        );
    }
}

export default Certificate;