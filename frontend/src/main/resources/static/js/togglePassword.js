// Funcinn para mostrar contraseña
function togglePassword(input) {
    const passwordInput = document.getElementById(input);

    const eyeOpenElement = (input === 'password') ? 'eye-open' : 'eye-open-confirm'
    const eyeClosedElement = (input === 'password') ? 'eye-closed' : 'eye-closed-confirm'
    const eyeOpen = document.getElementById(eyeOpenElement);
    const eyeClosed = document.getElementById(eyeClosedElement);

    const toggleBtn = document.getElementById('toggle-btn');

    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        eyeOpen.style.display = 'none';
        eyeClosed.style.display = 'block';
        toggleBtn.setAttribute('aria-label', 'Ocultar contraseña');
    } else {
        passwordInput.type = 'password';
        eyeOpen.style.display = 'block';
        eyeClosed.style.display = 'none';
        toggleBtn.setAttribute('aria-label', 'Mostrar contraseña');
    }
}