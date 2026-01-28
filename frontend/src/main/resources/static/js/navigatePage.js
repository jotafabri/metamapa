function navigatePage(direction) {
    const url = new URL(window.location.href);

    // Página actual
    const currentPage = parseInt(url.searchParams.get('page') || 0);
    const pageSize = parseInt(url.searchParams.get('size') || 10);

    const newPage = currentPage + direction;
    url.searchParams.set('page', newPage);
    url.searchParams.set('size', pageSize);

    // Tomar los filtros directamente del DOM
    const categoria = document.getElementById('categoria')?.value;
    const fechaReporteDesde = document.getElementById('fechaReporteDesde')?.value;
    const fechaReporteHasta = document.getElementById('fechaReporteHasta')?.value;
    const fechaAcontecimientoDesde = document.getElementById('fechaAcontecimientoDesde')?.value;
    const fechaAcontecimientoHasta = document.getElementById('fechaAcontecimientoHasta')?.value;

    const tipoUbicacion = document.getElementById('tipo-ubicacion')?.value;
    let pais = null, provincia = null, localidad = null;
    if (tipoUbicacion === 'pais') {
        pais = document.getElementById('pais')?.value;
    } else if (tipoUbicacion === 'provincia') {
        provincia = document.getElementById('provincia')?.value;
    } else if (tipoUbicacion === 'localidad') {
        localidad = document.getElementById('localidad')?.value;
    }

    const curado = document.getElementById('curado')?.checked;
    const soloConMultimedia = document.getElementById('soloConMultimedia')?.checked;
    const soloConContribuyente = document.getElementById('soloConContribuyente')?.checked;

    // Setear solo los filtros que existen
    if (categoria) url.searchParams.set('categoria', categoria);
    if (fechaReporteDesde) url.searchParams.set('fechaReporteDesde', fechaReporteDesde);
    if (fechaReporteHasta) url.searchParams.set('fechaReporteHasta', fechaReporteHasta);
    if (fechaAcontecimientoDesde) url.searchParams.set('fechaAcontecimientoDesde', fechaAcontecimientoDesde);
    if (fechaAcontecimientoHasta) url.searchParams.set('fechaAcontecimientoHasta', fechaAcontecimientoHasta);

    if (tipoUbicacion) url.searchParams.set('tipoUbicacion', tipoUbicacion);
    if (pais) url.searchParams.set('pais', pais);
    if (provincia) url.searchParams.set('provincia', provincia);
    if (localidad) url.searchParams.set('localidad', localidad);


    url.searchParams.set('curado', curado);
    url.searchParams.set('soloConMultimedia', soloConMultimedia);
    url.searchParams.set('soloConContribuyente', soloConContribuyente);


    // Navegar
    window.location.href = url.toString();
}

// Manejo de toggles (curado, multimedia, contribuyente)
function setupToggle(toggleId, checkboxId) {
    const toggle = document.getElementById(toggleId);
    const checkbox = document.getElementById(checkboxId);

    toggle.addEventListener('click', function () {
        checkbox.checked = !checkbox.checked;
        toggle.classList.toggle('active', checkbox.checked);
    });
}

function setupCuratedToggle() {
    const container = document.getElementById('curatedContainer');
    const toggle = document.getElementById('toggleCurado');
    const checkbox = document.getElementById('curado');
    toggle.addEventListener('click', function () {
        checkbox.checked = !checkbox.checked;
        toggle.classList.toggle('active', checkbox.checked);
        container.classList.toggle('active', checkbox.checked);
    });

    // Si ya venía marcado en la URL
    const curatedParam = checkbox.checked;
    if (curatedParam) {
        toggle.classList.add('active');
        container.classList.add('active');
    }
}

// Inicializar toggles
setupCuratedToggle();
setupToggle('toggleMultimedia', 'soloConMultimedia');
setupToggle('toggleContribuyente', 'soloConContribuyente');

// Enviar formulario con GET
document.getElementById('filterForm')?.addEventListener('submit', function (e) {
    // no es necesario preventDefault porque el form ya hace GET
});
