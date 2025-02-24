// Selecting the sidebar and buttons
const sidebar = document.querySelector(".sidebar");
const sidebarOpenBtn = document.querySelector("#sidebar-open");
const sidebarCloseBtn = document.querySelector("#sidebar-close");
const sidebarLockBtn = document.querySelector("#lock-icon");

// Function to toggle the lock state of the sidebar
const toggleLock = () => {
  sidebar.classList.toggle("locked");
  if (!sidebar.classList.contains("locked")) {
    sidebar.classList.add("hoverable");
    sidebarLockBtn.classList.replace("bx-lock-alt", "bx-lock-open-alt");
  } else {
    sidebar.classList.remove("hoverable");
    sidebarLockBtn.classList.replace("bx-lock-open-alt", "bx-lock-alt");
  }
};

// Function to hide the sidebar when the mouse leaves
const hideSidebar = () => {
  if (sidebar.classList.contains("hoverable")) {
    sidebar.classList.add("close");
  }
};

// Function to show the sidebar when the mouse enters
const showSidebar = () => {
  if (sidebar.classList.contains("hoverable")) {
    sidebar.classList.remove("close");
  }
};

// Function to toggle sidebar visibility
const toggleSidebar = () => {
  sidebar.classList.toggle("close");
};

// If the window width is less than 800px, close the sidebar
if (window.innerWidth < 800) {
  sidebar.classList.add("close");
  sidebar.classList.remove("locked");
  sidebar.classList.remove("hoverable");
}

// Event listeners
sidebarLockBtn.addEventListener("click", toggleLock);
sidebar.addEventListener("mouseleave", hideSidebar);
sidebar.addEventListener("mouseenter", showSidebar);
sidebarCloseBtn.addEventListener("click", toggleSidebar);


// search 
document.addEventListener("DOMContentLoaded", function () {
  const searchForm = document.getElementById("searchForm");
  const searchInput = document.getElementById("searchBook");

  if (!searchForm || !searchInput) {
      console.error("❌ Formulaire ou champ de recherche introuvable !");
      return;
  }

  searchForm.addEventListener("submit", function (event) {
      event.preventDefault(); // Empêche l'envoi classique du formulaire

      const bookName = searchInput.value.trim();
      if (!bookName) {
          alert("Veuillez entrer le nom d'un livre !");
          return;
      }

      console.log("🔍 Recherche en cours pour :", bookName);

      fetch(`/search-book?name=${encodeURIComponent(bookName)}`)
          .then(response => response.json())
          .then(data => {
              if (data.found) {
                  console.log("✅ Livre trouvé, redirection vers :", data.url);
                  window.location.href = data.url; // 🔥 Redirige vers la page du livre trouvé
              } else {
                  console.log("❌ Livre non trouvé !");
                  alert("❌ Livre non trouvé !");
                  window.location.href = "/404"; // 🔥 Redirige vers une page 404
              }
          })
          .catch(error => console.error("🚨 Erreur lors de la recherche :", error));
  });
});


