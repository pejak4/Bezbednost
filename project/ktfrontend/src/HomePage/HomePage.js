import React from 'react';
import Navbar from '../Navbar/Navbar';
import './HomePage.css';

class HomePage extends React.PureComponent {
    render() {
        return (
            <div>
                <Navbar />
                <div className="main-container">
                    <div className="container-content">
                        <h1>Dobrodosli na web servis bezbednosti</h1>
                        <p>Ovde se nalaze primeri odbrane od savremenih sajber napada</p>
                    </div>
                </div>
            </div>
        )
    }
}

export default HomePage;