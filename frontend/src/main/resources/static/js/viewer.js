function prevSlide(carouselId) {
    const track = document.getElementById(carouselId);
    const items = track.querySelectorAll('.carousel-item');
    const indicatorsContainer = document.getElementById('indicators-' + carouselId.replace('carousel-', ''));
    if (!indicatorsContainer) return;

    const indicators = indicatorsContainer.querySelectorAll('.indicator');
    let current = Array.from(items).findIndex(item => item.classList.contains('active'));

    // Pausar video actual si existe
    const currentVideo = items[current].querySelector('video');
    if (currentVideo) currentVideo.pause();

    items[current].classList.remove('active');
    indicators[current].classList.remove('active');

    current = (current - 1 + items.length) % items.length;

    items[current].classList.add('active');
    indicators[current].classList.add('active');
}

function nextSlide(carouselId) {
    const track = document.getElementById(carouselId);
    const items = track.querySelectorAll('.carousel-item');
    const indicatorsContainer = document.getElementById('indicators-' + carouselId.replace('carousel-', ''));
    if (!indicatorsContainer) return;

    const indicators = indicatorsContainer.querySelectorAll('.indicator');
    let current = Array.from(items).findIndex(item => item.classList.contains('active'));

    // Pausar video actual si existe
    const currentVideo = items[current].querySelector('video');
    if (currentVideo) currentVideo.pause();

    items[current].classList.remove('active');
    indicators[current].classList.remove('active');

    current = (current + 1) % items.length;

    items[current].classList.add('active');
    indicators[current].classList.add('active');
}

function goToSlide(carouselId, index) {
    const track = document.getElementById(carouselId);
    const items = track.querySelectorAll('.carousel-item');
    const indicatorsContainer = document.getElementById('indicators-' + carouselId.replace('carousel-', ''));
    if (!indicatorsContainer) return;

    const indicators = indicatorsContainer.querySelectorAll('.indicator');

    // Pausar todos los videos
    items.forEach(item => {
        const video = item.querySelector('video');
        if (video) video.pause();
        item.classList.remove('active');
    });

    indicators.forEach(ind => ind.classList.remove('active'));

    items[index].classList.add('active');
    indicators[index].classList.add('active');
}

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

function solicitarEliminacion(id) {
    console.log('Solicitar eliminación:', id);
}

function eliminarHecho(id) {
    if (confirm('¿Está seguro de que desea eliminar este hecho?')) {
        console.log('Eliminar hecho:', id);
    }
}
