const criteriosUsados = new Set();

const tiposCriterio = {
    'categoria': { label: 'Categoría', tipo: 'string' },
    'conContribuyente': { label: 'Con contribuyente', tipo: 'boolean' },
    'fechaAcontecimiento': { label: 'Fecha acontecimiento', tipo: 'rango' },
    'fechaCarga': { label: 'Fecha carga', tipo: 'rango' },
    'conMultimedia': { label: 'Con multimedia', tipo: 'boolean' },
    'titulo': { label: 'Título', tipo: 'string' },
    'ubicacion': { label: 'Ubicación', tipo: 'ubicacion' }
};

document.getElementById('btnAgregarCriterio').addEventListener('click', function() {
    const container = document.getElementById('criteriosContainer');
    const criterioDiv = document.createElement('div');
    criterioDiv.classList.add('criterio-item');
    criterioDiv.style.cssText = 'padding: 1rem; background-color: #1A1B1E; border: 1px solid #373738; border-radius: 0.5rem;';

    criterioDiv.innerHTML = `
        <div style="display: flex; gap: 1rem; align-items: flex-start;">
            <div style="flex: 1;">
                <label style="display: block; color: #ffffff; font-weight: 500; margin-bottom: 0.5rem; font-size: 0.875rem;">
                    Tipo de criterio:
                </label>
                <select class="criterio-selector" style="width: 100%; padding: 0.75rem; background-color: #1E2023; border: 1px solid #373738; border-radius: 0.5rem; color: #ffffff; font-size: 0.875rem; cursor: pointer;">
                    <option value="">Seleccionar criterio...</option>
                    ${Object.entries(tiposCriterio)
                        .filter(([key]) => !criteriosUsados.has(key))
                        .map(([key, value]) => `<option value="${key}">${value.label}</option>`)
                        .join('')}
                </select>
            </div>
            <div class="valor-container" style="flex: 2;"></div>
            <button type="button" class="btn-eliminar" style="background-color: #dc3545; color: white; border: none; border-radius: 0.5rem; padding: 0.75rem 1rem; cursor: pointer; font-size: 0.875rem; margin-top: 1.75rem;">
                <svg style="width: 16px; height: 16px;" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M6 18L18 6M6 6l12 12"/>
                </svg>
            </button>
        </div>
    `;

    container.appendChild(criterioDiv);
    actualizarEstadoBoton();

    const selector = criterioDiv.querySelector('.criterio-selector');
    const valorContainer = criterioDiv.querySelector('.valor-container');
    const btnEliminar = criterioDiv.querySelector('.btn-eliminar');

    let criterioAnterior = null;

    selector.addEventListener('change', function() {
        const tipoCriterio = this.value;
        if (!tipoCriterio) return;

        if (criterioAnterior && criterioAnterior !== tipoCriterio) {
            criteriosUsados.delete(criterioAnterior);
        }

        criteriosUsados.add(tipoCriterio);
        criterioAnterior = tipoCriterio;
        actualizarSelectoresCriterios();

        const config = tiposCriterio[tipoCriterio];
        valorContainer.innerHTML = generarCampoValor(tipoCriterio, config);
    });

    btnEliminar.addEventListener('click', function() {
        const tipoCriterio = selector.value;
        if (tipoCriterio) {
            criteriosUsados.delete(tipoCriterio);
        }
        criterioDiv.remove();
        actualizarSelectoresCriterios();
    });
});

function generarCampoValor(tipoCriterio, config) {
    const labelStyle = 'display: block; color: #ffffff; font-weight: 500; margin-bottom: 0.5rem; font-size: 0.875rem;';
    const inputStyle = 'width: 100%; padding: 0.75rem; background-color: #1E2023; border: 1px solid #373738; border-radius: 0.5rem; color: #ffffff; font-size: 0.875rem;';

    const mapeoNombres = {
        'categoria': 'criterios.categoria',
        'conContribuyente': 'criterios.soloConContribuyente',
        'fechaAcontecimiento': 'criterios.fechaAcontecimiento',
        'fechaCarga': 'criterios.fechaReporte',
        'conMultimedia': 'criterios.soloConMultimedia',
        'ubicacion': 'criterios.ubicacion'
    };

    const nombreBase = mapeoNombres[tipoCriterio] || `criterios.${tipoCriterio}`;

    switch(config.tipo) {
        case 'string':
            return `
                <label style="${labelStyle}">Valor:</label>
                <input type="text" name="${nombreBase}" style="${inputStyle}" placeholder="Ingrese valor...">
            `;

        case 'boolean':
            return `
                <label style="${labelStyle}">
                    <input type="checkbox" name="${nombreBase}" value="true" style="margin-right: 0.5rem;">
                    Activar filtro
                </label>
            `;

        case 'rango':
            return `
                <div style="display: flex; gap: 1rem;">
                    <div style="flex: 1;">
                        <label style="${labelStyle}">Desde:</label>
                        <input type="date" name="${nombreBase}Desde" style="${inputStyle}">
                    </div>
                    <div style="flex: 1;">
                        <label style="${labelStyle}">Hasta:</label>
                        <input type="date" name="${nombreBase}Hasta" style="${inputStyle}">
                    </div>
                </div>
            `;

        case 'ubicacion':
            return `
                <div style="display: flex; flex-direction: column; gap: 0.75rem;">
                    <div>
                        <label style="${labelStyle}">País:</label>
                        <select name="criterios.pais" class="ubicacion-pais" style="${inputStyle}">
                            <option value="">Seleccionar país...</option>
                        </select>
                    </div>
                    <div>
                        <label style="${labelStyle}">Provincia:</label>
                        <select name="criterios.provincia" class="ubicacion-provincia" style="${inputStyle}" disabled>
                            <option value="">Seleccionar provincia...</option>
                        </select>
                    </div>
                    <div>
                        <label style="${labelStyle}">Localidad:</label>
                        <select name="criterios.localidad" class="ubicacion-localidad" style="${inputStyle}" disabled>
                            <option value="">Seleccionar localidad...</option>
                        </select>
                    </div>
                </div>
            `;

        default:
            return '';
    }
}

function actualizarSelectoresCriterios() {
    document.querySelectorAll('.criterio-selector').forEach(selector => {
        const valorActual = selector.value;
        selector.innerHTML = `
            <option value="">Seleccionar criterio...</option>
            ${Object.entries(tiposCriterio)
                .filter(([key]) => !criteriosUsados.has(key) || key === valorActual)
                .map(([key, value]) => `<option value="${key}" ${key === valorActual ? 'selected' : ''}>${value.label}</option>`)
                .join('')}
        `;
    });
    actualizarEstadoBoton();
}

function actualizarEstadoBoton() {
    const btn = document.getElementById('btnAgregarCriterio');
    const criteriosActuales = document.querySelectorAll('.criterio-item').length;
    const hayOpcionesDisponibles = criteriosUsados.size < Object.keys(tiposCriterio).length && criteriosActuales < 7;
    btn.disabled = !hayOpcionesDisponibles;
    btn.style.opacity = hayOpcionesDisponibles ? '1' : '0.5';
    btn.style.cursor = hayOpcionesDisponibles ? 'pointer' : 'not-allowed';
}

function cargarCriteriosExistentes() {
    const hiddenInput = document.getElementById('criteriosExistentes');
    if (!hiddenInput) return;

    const criteriosData = {
        'categoria': hiddenInput.dataset.categoria,
        'titulo': hiddenInput.dataset.titulo,
        'fechaAcontecimiento': {
            desde: hiddenInput.dataset.fechaAcontecimientoDesde,
            hasta: hiddenInput.dataset.fechaAcontecimientoHasta
        },
        'fechaCarga': {
            desde: hiddenInput.dataset.fechaReporteDesde,
            hasta: hiddenInput.dataset.fechaReporteHasta
        },
        'ubicacion': {
            pais: hiddenInput.dataset.pais,
            provincia: hiddenInput.dataset.provincia,
            localidad: hiddenInput.dataset.localidad
        },
        'conMultimedia': hiddenInput.dataset.soloMultimedia === 'true',
        'conContribuyente': hiddenInput.dataset.soloContribuyente === 'true'
    };

    if (criteriosData.categoria && criteriosData.categoria !== 'null') {
        agregarCriterioExistente('categoria', criteriosData.categoria);
    }

    if (criteriosData.titulo && criteriosData.titulo !== 'null') {
        agregarCriterioExistente('titulo', criteriosData.titulo);
    }

    if ((criteriosData.fechaAcontecimiento.desde && criteriosData.fechaAcontecimiento.desde !== 'null') ||
        (criteriosData.fechaAcontecimiento.hasta && criteriosData.fechaAcontecimiento.hasta !== 'null')) {
        agregarCriterioRangoExistente('fechaAcontecimiento',
            criteriosData.fechaAcontecimiento.desde !== 'null' ? criteriosData.fechaAcontecimiento.desde : null,
            criteriosData.fechaAcontecimiento.hasta !== 'null' ? criteriosData.fechaAcontecimiento.hasta : null);
    }

    if ((criteriosData.fechaCarga.desde && criteriosData.fechaCarga.desde !== 'null') ||
        (criteriosData.fechaCarga.hasta && criteriosData.fechaCarga.hasta !== 'null')) {
        agregarCriterioRangoExistente('fechaCarga',
            criteriosData.fechaCarga.desde !== 'null' ? criteriosData.fechaCarga.desde : null,
            criteriosData.fechaCarga.hasta !== 'null' ? criteriosData.fechaCarga.hasta : null);
    }

    if ((criteriosData.ubicacion.pais && criteriosData.ubicacion.pais !== 'null') ||
        (criteriosData.ubicacion.provincia && criteriosData.ubicacion.provincia !== 'null') ||
        (criteriosData.ubicacion.localidad && criteriosData.ubicacion.localidad !== 'null')) {
        agregarCriterioUbicacionExistente(
            criteriosData.ubicacion.pais !== 'null' ? criteriosData.ubicacion.pais : null,
            criteriosData.ubicacion.provincia !== 'null' ? criteriosData.ubicacion.provincia : null,
            criteriosData.ubicacion.localidad !== 'null' ? criteriosData.ubicacion.localidad : null);
    }

    if (criteriosData.conMultimedia) {
        agregarCriterioBooleanExistente('conMultimedia');
    }

    if (criteriosData.conContribuyente) {
        agregarCriterioBooleanExistente('conContribuyente');
    }

    hiddenInput.remove();
}

function agregarCriterioExistente(tipo, valor) {
    document.getElementById('btnAgregarCriterio').click();
    const ultimoCriterio = document.querySelector('.criterio-item:last-child');
    const selector = ultimoCriterio.querySelector('.criterio-selector');
    selector.value = tipo;
    selector.dispatchEvent(new Event('change'));

    setTimeout(() => {
        const input = ultimoCriterio.querySelector('input[type="text"]');
        if (input) input.value = valor;
    }, 50);
}

function agregarCriterioRangoExistente(tipo, desde, hasta) {
    document.getElementById('btnAgregarCriterio').click();
    const ultimoCriterio = document.querySelector('.criterio-item:last-child');
    const selector = ultimoCriterio.querySelector('.criterio-selector');
    selector.value = tipo;
    selector.dispatchEvent(new Event('change'));

    setTimeout(() => {
        const inputs = ultimoCriterio.querySelectorAll('input[type="date"]');
        if (desde && inputs[0]) inputs[0].value = desde;
        if (hasta && inputs[1]) inputs[1].value = hasta;
    }, 50);
}

function agregarCriterioUbicacionExistente(pais, provincia, localidad) {
    document.getElementById('btnAgregarCriterio').click();
    const ultimoCriterio = document.querySelector('.criterio-item:last-child');
    const selector = ultimoCriterio.querySelector('.criterio-selector');
    selector.value = 'ubicacion';
    selector.dispatchEvent(new Event('change'));

    setTimeout(() => {
        const selects = ultimoCriterio.querySelectorAll('select');
        if (pais && selects[0]) selects[0].value = pais;
        if (provincia && selects[1]) selects[1].value = provincia;
        if (localidad && selects[2]) selects[2].value = localidad;
    }, 50);
}

function agregarCriterioBooleanExistente(tipo) {
    document.getElementById('btnAgregarCriterio').click();
    const ultimoCriterio = document.querySelector('.criterio-item:last-child');
    const selector = ultimoCriterio.querySelector('.criterio-selector');
    selector.value = tipo;
    selector.dispatchEvent(new Event('change'));

    setTimeout(() => {
        const checkbox = ultimoCriterio.querySelector('input[type="checkbox"]');
        if (checkbox) checkbox.checked = true;
    }, 50);
}

document.addEventListener('DOMContentLoaded', function() {
    cargarCriteriosExistentes();
});

