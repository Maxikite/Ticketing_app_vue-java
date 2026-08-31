<script setup>
import { computed, onMounted, ref } from 'vue'
import AllTicketsPage from './pages/AllTicketsPage.vue'
import EditTicketPage from './pages/EditTicketPage.vue'

const API_URL = 'http://localhost:8080/api/tickets'
const activePage = ref('new')
const tickets = ref([])
const filter = ref('ALL')
const loading = ref(false)
const error = ref('')
const success = ref('')
const form = ref({ title: '', description: '', priority: 'MEDIUM', requester: '', status: 'OPEN' })
const editingTicket = ref(null)
const editForm = ref({ title: '', description: '', priority: 'MEDIUM', requester: '', status: 'OPEN' })

const filteredTickets = computed(() => {
  if (filter.value === 'ALL') return tickets.value
  return tickets.value.filter((ticket) => ticket.status === filter.value)
})

async function loadTickets() {
  loading.value = true
  error.value = ''
  try {
    const response = await fetch(API_URL)
    if (!response.ok) throw new Error('Impossible de récupérer les tickets.')
    tickets.value = await response.json()
  } catch (exception) {
    error.value = `${exception.message} Vérifiez que le backend est lancé.`
  } finally {
    loading.value = false
  }
}

async function createTicket() {
  error.value = ''
  success.value = ''
  if (!form.value.title.trim() || !form.value.description.trim() || !form.value.requester.trim()) {
    error.value = 'Veuillez renseigner le titre, la description et votre nom.'
    return
  }
  try {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value),
    })
    if (!response.ok) {
      const body = await response.json().catch(() => ({}))
      throw new Error(body.message || 'La création du ticket a échoué.')
    }
    const ticket = await response.json()
    tickets.value = [ticket, ...tickets.value]
    form.value = { title: '', description: '', priority: 'MEDIUM', requester: '', status: 'OPEN' }
    success.value = 'Ticket créé et enregistré.'
    activePage.value = 'tickets'
  } catch (exception) {
    error.value = `${exception.message} Vérifiez que le backend est lancé.`
  }
}

function startEditing(ticket) {
  editingTicket.value = ticket
  editForm.value = {
    title: ticket.title,
    description: ticket.description,
    priority: ticket.priority,
    requester: ticket.requester,
    status: ticket.status,
  }
  error.value = ''
  success.value = ''
}

function cancelEditing() {
  editingTicket.value = null
}

async function updateTicket() {
  error.value = ''
  if (!editForm.value.title.trim() || !editForm.value.description.trim() || !editForm.value.requester.trim()) {
    error.value = 'Veuillez renseigner le titre, la description et le nom.'
    return
  }
  try {
    const response = await fetch(`${API_URL}/${editingTicket.value.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(editForm.value),
    })
    if (!response.ok) throw new Error('La modification du ticket a échoué.')
    const updatedTicket = await response.json()
    tickets.value = tickets.value.map((ticket) => ticket.id === updatedTicket.id ? updatedTicket : ticket)
    editingTicket.value = null
    success.value = 'Ticket modifié.'
    activePage.value = 'tickets'
  } catch (exception) {
    error.value = `${exception.message} Vérifiez que le backend est lancé.`
  }
}

async function deleteTicket(ticket) {
  if (!window.confirm(`Supprimer le ticket « ${ticket.title} » ?`)) return
  error.value = ''
  try {
    const response = await fetch(`${API_URL}/${ticket.id}`, { method: 'DELETE' })
    if (!response.ok) throw new Error('La suppression du ticket a échoué.')
    tickets.value = tickets.value.filter((item) => item.id !== ticket.id)
    success.value = 'Ticket supprimé.'
  } catch (exception) {
    error.value = `${exception.message} Vérifiez que le backend est lancé.`
  }
}

function showPage(page) {
  activePage.value = page
  error.value = ''
  success.value = ''
  if (page === 'tickets') loadTickets()
}

onMounted(loadTickets)
</script>

<template>
  <div class="app-shell">
    <aside class="sidebar">
      <div class="brand"><span class="brand-mark">AT</span><span>Atelier<br /><strong>Tickets</strong></span></div>
      <div class="workspace-label">ESPACE DE TRAVAIL</div>
      <nav>
        <button :class="{ active: activePage === 'new' }" @click="showPage('new')"><span class="nav-icon">+</span> Nouveau ticket</button>
        <button :class="{ active: activePage === 'tickets' }" @click="showPage('tickets')"><span class="nav-icon">≡</span> Tous les tickets <span class="nav-count">{{ tickets.length }}</span></button>
      </nav>
      <div class="sidebar-foot"><span class="status-dot"></span><span>API connectée</span><small>Spring Boot · H2</small></div>
    </aside>

    <main class="main-content">
      <header class="topbar">
        <div>
          <p class="eyebrow">CENTRE DE SUPPORT</p>
          <h1>{{ activePage === 'new' ? 'Créer une demande' : 'Vue d’ensemble' }}</h1>
        </div>
        <div class="date-stamp">{{ new Intl.DateTimeFormat('fr-FR', { weekday: 'long', day: 'numeric', month: 'long' }).format(new Date()) }}</div>
      </header>

      <div v-if="error" class="alert alert-error">{{ error }}</div>
      <div v-if="success" class="alert alert-success">{{ success }}</div>

      <section v-if="activePage === 'new'" class="new-layout">
        <div class="form-panel">
          <div class="panel-heading"><div><span class="section-kicker">NOUVELLE DEMANDE</span><h2>Quel est le sujet ?</h2></div><span class="required-note">Tous les champs sont requis</span></div>
          <form @submit.prevent="createTicket">
            <label>Titre de la demande<input v-model="form.title" type="text" placeholder="Ex. Accès impossible au tableau de bord" /></label>
            <label>Décrivez le problème<textarea v-model="form.description" rows="6" placeholder="Ajoutez les détails utiles pour que l'équipe puisse vous aider..."></textarea></label>
            <div class="field-row"><label>Votre nom<input v-model="form.requester" type="text" placeholder="Ex. Camille Martin" /></label><label>Priorité<select v-model="form.priority"><option value="LOW">Basse</option><option value="MEDIUM">Normale</option><option value="HIGH">Haute</option><option value="URGENT">Urgente</option></select></label></div>
            <button class="primary-button" type="submit">Envoyer la demande <span>→</span></button>
          </form>
        </div>
        <div class="aside-note"><h3>Une demande claire, une réponse plus rapide.</h3><p>Décrivez ce que vous essayiez de faire et ce qui s'est passé. Notre équipe reviendra vers vous dès que possible.</p><div class="note-rule"></div><p class="small-copy">Temps de réponse moyen<br /><strong>moins de 24 heures</strong></p></div>
      </section>

      <AllTicketsPage
        v-else-if="activePage === 'tickets'"
        :tickets="tickets"
        :filtered-tickets="filteredTickets"
        :filter="filter"
        :loading="loading"
        @update:filter="filter = $event"
        @edit-ticket="startEditing"
        @delete-ticket="deleteTicket"
      />

    </main>
  </div>

  <EditTicketPage
    v-if="editingTicket"
    :ticket="editingTicket"
    :form="editForm"
    @cancel="cancelEditing"
    @save="updateTicket"
    @update:form="editForm = $event"
  />
</template>

<style scoped></style>
