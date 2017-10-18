import React from 'react';
import ReactDOM from 'react-dom';

require("../css/styles.css");

import App from './components/app';

var container = document.createElement("div");
document.body.appendChild(container);
ReactDOM.render(<App/>, container);
