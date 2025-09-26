async function loadCollections() {
  try {
    const res = await fetch("collections.json");
    const data = await res.json();

    const grid = document.getElementById("collections-grid");

    data.forEach(col => {
      const card = document.createElement("article");
      card.classList.add("card");

      card.innerHTML = `
        <div class="card-header">
          <span class="tag">${col.tag}</span>
          <span class="country">
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
    <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 
             0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/>
  </svg>
  ${col.country}
</span>

        </div>
        <h3 class="card-title">${col.title}</h3>
        <p class="card-desc">${col.desc}</p>
        <div class="card-meta">
          <span>${col.hechos} hechos</span>
          <span>${col.curados} curados</span>
        </div>
        <button class="btn btn-primary btn-hero">👁 Ver Colección</button>
      `;

      grid.appendChild(card);
    });

  } catch (error) {
    console.error("Error al cargar las colecciones:", error);
  }
}

loadCollections();

