function abrirModalEliminacion(button) {
    // Leer datos directamente del botón
    const hechoId = button.dataset.id;
    const titulo = button.dataset.titulo;
    const descripcion = button.dataset.descripcion || 'Sin descripción';
    const categoria = button.dataset.categoria || '';
    const fecha = button.dataset.fecha || '';

    // Rellenar datos del modal
    document.getElementById('modal-titulo').textContent = titulo;
    document.getElementById('modal-descripcion').textContent = descripcion;
    document.getElementById('modal-categoria').textContent = categoria;
    document.getElementById('modal-fecha').textContent = fecha;
    document.getElementById('hecho-id').value = hechoId;

    // Mostrar modal
    document.getElementById('modal-eliminacion').style.display = 'flex';
    document.body.style.overflow = 'hidden';
}

function cerrarModal() {
    document.getElementById('modal-eliminacion').style.display = 'none';
    document.body.style.overflow = '';
    document.getElementById('form-eliminacion').reset();
}

// Cerrar con ESC
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        cerrarModal();
    }
});

// Cerrar al hacer click fuera del modal
document.addEventListener('click', function(e) {
    if (e.target.id === 'modal-eliminacion') {
        cerrarModal();
    }
});
