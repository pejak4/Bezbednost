import React from 'react';
import './DownBar.css';

class DownBar extends React.PureComponent {

    render() {
        return (
            <nav className="navbar_1">
                <h1 className="title_1">
                    <span className="logo_1">Info</span>Certificate
                </h1>

                <ul>
                    <li onClick={(e)=>{ this.props.clickView("NEW");}}>Create new certificate</li>
                    <li onClick={(e)=>{ this.props.clickView("VIEW");}}>View all certificate</li>
                    <li onClick={(e)=>{ this.props.clickView("API");}}>Check certificate with ID</li>
                </ul>
            </nav>
        );
    }
}

export default DownBar;