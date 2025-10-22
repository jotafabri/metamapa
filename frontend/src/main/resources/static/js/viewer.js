function toggleMenu(menuId) {
    const menu = document.getElementById(menuId);
    const allMenus = document.querySelectorAll('.menu-dropdown');

    allMenus.forEach(m => {
        if (m.id !== menuId) {
            m.classList.remove('active');
        }
    });

    menu.classList.toggle('active');
}

document.addEventListener('click', function(e) {
    if (!e.target.closest('.hecho-menu')) {
        document.querySelectorAll('.menu-dropdown').forEach(m => m.classList.remove('active'));
    }
});

function editarHecho(id) {
    console.log('Editar hecho:', id);
}

function eliminarHecho(id) {
    if (confirm('¿Está seguro de que desea eliminar este hecho?')) {
        console.log('Eliminar hecho:', id);
    }
}

// viewer.js
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

document.getElementById('tipo-ubicacion').addEventListener('change', function() {
    const tipo = this.value;

    // Ocultar todos
    document.getElementById('container-pais').style.display = 'none';
    document.getElementById('container-provincia').style.display = 'none';
    document.getElementById('container-localidad').style.display = 'none';

    // Limpiar solo los que NO son el tipo seleccionado
    if (tipo !== 'pais') document.getElementById('pais').value = '';
    if (tipo !== 'provincia') document.getElementById('provincia').value = '';
    if (tipo !== 'localidad') document.getElementById('localidad').value = '';

    // Mostrar el seleccionado
    document.getElementById('container-' + tipo).style.display = 'block';
});
