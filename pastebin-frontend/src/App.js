import React, {useState} from 'react';
import axios from "axios";

import './App.css';
import "primereact/resources/themes/lara-light-indigo/theme.css";
import "primereact/resources/primereact.min.css";
import '/node_modules/primeflex/primeflex.css'
import {Button} from 'primereact/button';
import {Card} from 'primereact/card';

import Keycloak from 'keycloak-js';

let initOptions = {
    url: 'http://localhost:8282/',
    realm: 'pastebin',
    clientId: 'frontend-client',
    onLoad: 'check-sso', // check-sso | login-required
    KeycloakResponseType: 'code',

}

let kc = new Keycloak(initOptions);

kc.init({
    onLoad: initOptions.onLoad,
    KeycloakResponseType: 'code',
    silentCheckSsoRedirectUri: window.location.origin + "/silent-check-sso.html", checkLoginIframe: false,
    pkceMethod: 'S256'
}).then((auth) => {
    if (!auth) {
        window.location.reload();
    } else {
        console.info("Authenticated");
        console.log('auth', auth)
        console.log('Keycloak', kc)
        kc.onTokenExpired = () => {
            console.log('token expired')
        }
    }
}, () => {
    console.error("Authenticated Failed");
});

const initialState = {
    title: "",
    author: "",
    tags: "",
    text: "",
    expiration: "N",
};

function App() {

    const [infoMessage, setInfoMessage] = useState('');

    const [formData, setFormData] = useState(initialState);

    const [hash, setHash] = useState('');

    const handleChange = (e) => {
        const {name, value} = e.target;
        setFormData({...formData, [name]: value});
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!kc.authenticated) {
            setInfoMessage("PLEASE AUTHENTICATE!")
        } else {
            try {
                const response = await axios.post('http://localhost:8081/api/v1/paste', {
                        title: formData.title,
                        author: formData.author,
                        tags: formData.tags.split(",").map((tag) => tag.trim()),
                        text: formData.text,
                        expiration: formData.expiration,
                    },
                    {
                        headers: {
                            Authorization: 'Bearer ' + kc.token, // Добавьте токен в заголовок
                        },
                    });
                setInfoMessage("Hash code of your paste:" + response.data);
                setFormData(initialState);
            } catch (error) {
                console.error("Error sending POST request:", error);
            }
        }
    };

    const handleSubmitHash = async (e) => {
        e.preventDefault();
        if (!kc.authenticated) {
            setInfoMessage("PLEASE AUTHENTICATE!")
        } else {
            try {
                const response = await axios.get('http://localhost:8081/api/v1/paste/' + hash,
                    {
                        headers: {
                            Authorization: 'Bearer ' + kc.token, // Добавьте токен в заголовок
                        },
                    });
              let paste = response.data;
              setInfoMessage("Your paste:\n {\n" +
                  "    \"hash\": \"" + paste.hash + "\",\n" +
                  "    \"title\": \"" + paste.title + "\",\n" +
                  "    \"author\": \"" + paste.author + "\",\n" +
                  "    \"tags\": \"" + JSON.stringify(paste.tags) + "\",\n" +
                  "    \"content\": \"" + paste.content + "\",\n" +
                  "    \"expiration\": " + paste.expiration + "\n" +
                  "}");
              setHash("");
            } catch (error) {
                console.error("Error sending POST request:", error);
            }
        }
    }

    return (
        <div className="App">
            {/* <Auth /> */}
            <div className='grid'>
                <div className='col-12'>
                    <h1>My Awesome Pastebin</h1>
                </div>
                <div className='col-12'>
                    <h1 id='app-header-2'>Secured with Keycloak</h1>
                </div>
            </div>
            <div className="grid">
                <div className="col">
                    <Button onClick={() => {
                        setInfoMessage(kc.authenticated ? 'Authenticated: TRUE' : 'Authenticated: FALSE')
                    }} className="m-1" label='Is Authenticated'/>

                    <Button onClick={() => {
                        kc.login()
                    }} className='m-1' label='Login' severity="success"/>
                    <Button onClick={() => {
                        setInfoMessage(kc.token)
                    }} className="m-1" label='Show Access Token' severity="info"/>
                    <Button onClick={() => {
                        setInfoMessage(JSON.stringify(kc.tokenParsed))
                    }} className="m-1" label='Show Parsed Access token' severity="info"/>
                    <Button onClick={() => {
                        setInfoMessage(kc.isTokenExpired(5).toString())
                    }} className="m-1" label='Check Token expired' severity="warning"/>
                    <Button onClick={() => {
                        kc.updateToken(10).then((refreshed) => {
                            setInfoMessage('Token Refreshed: ' + refreshed.toString())
                        }, (e) => {
                            setInfoMessage('Refresh Error')
                        })
                    }} className="m-1" label='Update Token (if about to expire)'/> {/** 10 seconds */}
                    <Button onClick={() => {
                        kc.logout({redirectUri: 'http://localhost:3000/'})
                    }} className="m-1" label='Logout' severity="danger"/>

                </div>
            </div>


            <div className='grid'>
                <div className='col-2'></div>
                <div className='col-8'>
                    <h3>Info Panel</h3>
                    <Card>
                        <pre style={{wordBreak: 'break-all'}} id='infoPanel'>
                            {infoMessage}
                        </pre>
                    </Card>
                </div>
                <div className='col-2'></div>
            </div>

            <div className='grid'>
                <div className='col-2'></div>
                <div className='col-3'>
                    <h3>Create a New Paste</h3>
                    <form onSubmit={handleSubmit}>
                        <div>
                            <label>Title:</label><br/>
                            <input
                                type="text"
                                name="title"
                                value={formData.title}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div>
                            <label>Author:</label><br/>
                            <input
                                type="text"
                                name="author"
                                value={formData.author}
                                onChange={handleChange}
                            />
                        </div>
                        <div>
                            <label>Tags (comma-separated):</label><br/>
                            <input
                                type="text"
                                name="tags"
                                value={formData.tags}
                                onChange={handleChange}
                            />
                        </div>
                        <div>
                            <label>Text:</label><br/>
                            <textarea
                                name="text"
                                value={formData.text}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div>
                            <label>Expiration:</label><br/>
                            <select
                                name="expiration"
                                value={formData.expiration}
                                onChange={handleChange}
                            >
                                <option value="N">Never</option>
                                <option value="10m">10 Minutes</option>
                                <option value="1M">1 Month</option>
                            </select>
                        </div>
                        <button type="submit">Create Paste</button>
                    </form>
                </div>
                <div className='col-2'></div>
                <div className='col-3'>
                    <h3>Get your Paste</h3>
                    <form onSubmit={handleSubmitHash}>
                        <div>
                            <label>Hash:</label><br/>
                            <input
                                type="text"
                                name="hash"
                                value={hash}
                                onChange={(event) =>
                                    setHash(event.target.value)
                                }
                                required
                            />
                        </div>

                        <button type="submit">Find Paste</button>
                    </form>
                </div>
                <div className='col-2'></div>
            </div>

        </div>
    );
}


export default App;
