function solicitarEliminacion(hechoId) {
    const hecho = window.hechosData.find(h => h.id === hechoId);

    if (!hecho) return;

    // Rellenar datos del modal
    document.getElementById('modal-titulo').textContent = hecho.titulo;
    document.getElementById('modal-descripcion').textContent = hecho.descripcion || 'Sin descripción';
    document.getElementById('modal-categoria').textContent = hecho.categoria || '';
    document.getElementById('modal-fecha').textContent = hecho.fechaAcontecimiento ?
        new Date(hecho.fechaAcontecimiento).toLocaleDateString('es-AR') : '';
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

// Cerrar al hacer click fuera
document.addEventListener('click', function(e) {
    if (e.target.id === 'modal-eliminacion') {
        cerrarModal();
    }
});
