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
