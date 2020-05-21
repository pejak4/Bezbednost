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
                        <h1>Welcome To CyberSecurity Certification Website</h1>
                        <p>Use or buy our certificates by your wish and let us know if you are satisfied!</p>
                    </div>
                </div>
            </div>
        )
    }
}

export default HomePage;