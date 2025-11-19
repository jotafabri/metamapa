function mostrarToast(mensaje, tipo = 'success') {
    const contenedor = document.getElementById('toast-container');
    if (!contenedor || !mensaje) return;

    const toast = document.createElement('div');
    toast.classList.add('toast');
    toast.classList.add(tipo === 'error' ? 'toast-error' : 'toast-success');
    toast.textContent = mensaje;

    contenedor.appendChild(toast);

    // Animación de aparición
    setTimeout(() => toast.classList.add('show'), 50);

    // Desaparece después de 3 segundos
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => contenedor.removeChild(toast), 300);
    }, 3000);
}

