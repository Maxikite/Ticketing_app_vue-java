<script setup>
import { computed } from 'vue'

const props = defineProps({
  tickets: { type: Array, default: () => [] },
  filteredTickets: { type: Array, default: () => [] },
  filter: { type: String, default: 'ALL' },
  loading: { type: Boolean, default: false },
})

const emit = defineEmits(['update:filter', 'edit-ticket', 'delete-ticket', 'load-tickets'])

const openCount = computed(() => props.tickets.filter((ticket) => ticket.status === 'OPEN').length)
const progressCount = computed(() => props.tickets.filter((ticket) => ticket.status === 'IN_PROGRESS').length)

function formatDate(value) {
  return new Intl.DateTimeFormat('fr-FR', { day: '2-digit', month: 'short', hour: '2-digit', minute: '2-digit' }).format(new Date(value))
}

function notifyFilter(event) {
  emit('update:filter', event.target.value)
}

function handleEdit(ticket) {
  emit('edit-ticket', ticket)
}

function handleDelete(ticket) {
  emit('delete-ticket', ticket)
}
</script>

<template>
  <section class="tickets-page">
    <div class="stats">
      <div><span class="stat-label">TOTAL</span><strong>{{ tickets.length }}</strong><span class="stat-caption">demandes</span></div>
      <div><span class="stat-label">OUVERTS</span><strong>{{ openCount }}</strong><span class="stat-caption">à traiter</span></div>
      <div><span class="stat-label">EN COURS</span><strong>{{ progressCount }}</strong><span class="stat-caption">actuellement</span></div>
    </div>

    <div class="list-toolbar">
      <div>
        <span class="section-kicker"> SUIVI</span>
        <h2>Vos demandes</h2>
      </div>
      <select :value="filter" @change="notifyFilter">
        <option value="ALL">Tous les statuts</option>
        <option value="OPEN">Ouverts</option>
        <option value="IN_PROGRESS">En cours</option>
        <option value="RESOLVED">Résolus</option>
      </select>
    </div>

    <div v-if="loading" class="empty-state">Chargement des demandes...</div>
    <div v-else-if="!filteredTickets.length" class="empty-state">
      <strong>Aucun ticket à afficher</strong>
      <span>Créez votre première demande pour la voir apparaître ici.</span>
    </div>
    <div v-else class="ticket-list">
      <article v-for="ticket in filteredTickets" :key="ticket.id" class="ticket-row">
        <div class="ticket-id">#{{ String(ticket.id).padStart(4, '0') }}</div>
        <div class="ticket-main">
          <h3>{{ ticket.title }}</h3>
          <p>{{ ticket.description }}</p>
          <span>Par {{ ticket.requester }} · {{ formatDate(ticket.createdAt) }}</span>
        </div>
        <div class="ticket-meta">
          <span :class="['priority', ticket.priority.toLowerCase()]">{{ ticket.priority }}</span>
          <span :class="['ticket-status', ticket.status.toLowerCase()]">
            {{ ticket.status === 'OPEN' ? 'Ouvert' : ticket.status === 'IN_PROGRESS' ? 'En cours' : 'Résolu' }}
          </span>
          <div class="ticket-actions">
            <button title="Modifier le ticket" @click="handleEdit(ticket)">Modifier</button>
            <button class="delete-button" title="Supprimer le ticket" @click="handleDelete(ticket)">Supprimer</button>
          </div>
        </div>
      </article>
    </div>
  </section>
</template>
