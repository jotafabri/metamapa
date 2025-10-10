/*<![CDATA[*/
function navigatePage(direction) {
    const currentPage = /*[[${currentPage}]]*/ 0;
    const pageSize = /*[[${pageSize}]]*/ 10;
    const newPage = currentPage + direction;
    const url = new URL(window.location.href);
    url.searchParams.set('page', newPage);
    url.searchParams.set('size', pageSize);

    const categoria = /*[[${filtros.categoria}]]*/ null;
    const fechaReporteDesde = /*[[${filtros.fechaReporteDesde}]]*/ null;
    const fechaReporteHasta = /*[[${filtros.fechaReporteHasta}]]*/ null;
    const fechaAcontecimientoDesde = /*[[${filtros.fechaAcontecimientoDesde}]]*/ null;
    const fechaAcontecimientoHasta = /*[[${filtros.fechaAcontecimientoHasta}]]*/ null;

    const curado = document.getElementById('curado')?.checked;
    const soloConMultimedia = document.getElementById('soloConMultimedia')?.checked;
    const soloConContribuyente = document.getElementById('soloConContribuyente')?.checked;

    if (categoria) url.searchParams.set('categoria', categoria);
    if (fechaReporteDesde) url.searchParams.set('fechaReporteDesde', fechaReporteDesde);
    if (fechaReporteHasta) url.searchParams.set('fechaReporteHasta', fechaReporteHasta);
    if (fechaAcontecimientoDesde) url.searchParams.set('fechaAcontecimientoDesde', fechaAcontecimientoDesde);
    if (fechaAcontecimientoHasta) url.searchParams.set('fechaAcontecimientoHasta', fechaAcontecimientoHasta);

    if (curado) url.searchParams.set('curado', true);
    if (soloConMultimedia) url.searchParams.set('soloConMultimedia', true);
    if (soloConContribuyente) url.searchParams.set('soloConContribuyente', true);

    window.location.href = url.toString();
}

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
    const curatedParam = /*[[${filtros.curado}]]*/ null;
    if (curatedParam !== null && curatedParam) {
        checkbox.checked = true;
        toggle.classList.add('active');
        container.classList.add('active');
    }
}

setupCuratedToggle();
setupToggle('toggleMultimedia', 'soloConMultimedia');
setupToggle('toggleContribuyente', 'soloConContribuyente');

document.getElementById('filterForm')?.addEventListener('submit', function (e) {
    e.preventDefault();
    navigatePage(0);
});
/*]]>*/
