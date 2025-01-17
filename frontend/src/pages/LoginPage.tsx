import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/LoginPage.css';
import Cookies from "js-cookie";
import NavBar from "../components/NavBar.tsx";
import Footer from "../components/Footer.tsx";

interface AssignedUser {
    id: string;
    email: string;
    role: string;
    enabled: boolean;
}

interface LoginResponse {
    token: string;
    assignedUser: AssignedUser;
}

const LoginPage: React.FC = () => {
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [confirmPassword, setConfirmPassword] = useState<string>('');
    const [error, setError] = useState<string>('');
    const [isLogin, setIsLogin] = useState<boolean>(true);
    const navigate = useNavigate();

    // Funkcja logowania
    const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError('');

        try {
            const response = await axios.post<LoginResponse>('http://localhost:8080/api/v1/auth/login', {
                email,
                password,
            });
            const userId = response.data.assignedUser.id
            const { token } = response.data;
            Cookies.set('userId', userId);
            Cookies.set('token', token);
            navigate('/profile/' + userId);
        } catch (err) {
            setError('Nieprawidłowy e-mail lub hasło. error: ' + err);
        }
    };

    // Funkcja rejestracji
    const handleRegister = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError('');

        if (password !== confirmPassword) {
            setError('Hasła nie pasują do siebie.');
            return;
        }

        try {
            await axios.post('http://localhost:8080/api/v1/users/', {
                email,
                password,
            });
            setIsLogin(true); // Po zarejestrowaniu przełączenie na formularz logowania
            setError('');
        } catch (err) {
            setError('Błąd rejestracji. Spróbuj ponownie. error: ' + err);
        }
    };

    return (
        <>
        <NavBar />
        <div className="login-page">
            <div className="login-container">
                <h1 className="login-title">{isLogin ? 'Logowanie' : 'Rejestracja'}</h1>
                <form
                    onSubmit={isLogin ? handleLogin : handleRegister}
                    className="login-form"
                >
                    <div className="form-group">
                        <label htmlFor="email">E-mail:</label>
                        <input
                            type="email"
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                            className="form-input"
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Hasło:</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            className="form-input"
                        />
                    </div>
                    {!isLogin && (
                        <div className="form-group">
                            <label htmlFor="confirmPassword">Potwierdź hasło:</label>
                            <input
                                type="password"
                                id="confirmPassword"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                required
                                className="form-input"
                            />
                        </div>
                    )}
                    {error && <p className="error-message">{error}</p>}
                    <button type="submit" className="login-button">
                        {isLogin ? 'Zaloguj się' : 'Zarejestruj się'}
                    </button>
                </form>
                <div className="switch-form">
                    <p>
                        {isLogin ? 'Nie masz konta?' : 'Masz już konto?'}
                        <button onClick={() => setIsLogin(!isLogin)} className="switch-button">
                            {isLogin ? 'Zarejestruj się' : 'Zaloguj się'}
                        </button>
                    </p>
                </div>
            </div>
        </div>
            <Footer />
        </>
    );
};

export default LoginPage;
