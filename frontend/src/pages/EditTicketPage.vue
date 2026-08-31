<script setup>
const props = defineProps({
  ticket: { type: Object, default: null },
  form: { type: Object, default: () => ({ title: '', description: '', priority: 'MEDIUM', requester: '', assignee: 'Alice Martin', status: 'OPEN' }) },
})

const employees = ['Alice Martin', 'Lucas Bernard', 'Sophie Dubois']

const emit = defineEmits(['cancel', 'save', 'update:form'])

function updateField(field, value) {
  emit('update:form', { ...props.form, [field]: value })
}
</script>

<template>
  <div class="modal-backdrop" @click.self="emit('cancel')">
    <form class="edit-modal" @submit.prevent="emit('save')">
      <div class="panel-heading">
        <div>
          <span class="section-kicker">MODIFICATION</span>
          <h2>Modifier le ticket</h2>
        </div>
        <button class="close-button" type="button" aria-label="Fermer" @click="emit('cancel')">×</button>
      </div>

      <label>
        Titre de la demande
        <input :value="form.title" type="text" @input="updateField('title', $event.target.value)" />
      </label>

      <label>
        Décrivez le problème
        <textarea :value="form.description" rows="5" @input="updateField('description', $event.target.value)"></textarea>
      </label>

      <div class="field-row">
        <label>
          Votre nom
          <input :value="form.requester" type="text" @input="updateField('requester', $event.target.value)" />
        </label>

        <label>
          Assigné à
          <select :value="form.assignee || 'Alice Martin'" @change="updateField('assignee', $event.target.value)">
            <option v-for="employee in employees" :key="employee" :value="employee">{{ employee }}</option>
          </select>
        </label>
      </div>

      <div class="field-row">
        <label>
          Priorité
          <select :value="form.priority" @change="updateField('priority', $event.target.value)">
            <option value="LOW">Basse</option>
            <option value="MEDIUM">Normale</option>
            <option value="HIGH">Haute</option>
            <option value="URGENT">Urgente</option>
          </select>
        </label>

        <label>
          Statut
          <select :value="form.status" @change="updateField('status', $event.target.value)">
            <option value="OPEN">Ouvert</option>
            <option value="IN_PROGRESS">En cours</option>
            <option value="RESOLVED">Résolu</option>
          </select>
        </label>
      </div>

      <div class="modal-actions">
        <button class="secondary-button" type="button" @click="emit('cancel')">Annuler</button>
        <button class="primary-button" type="submit">Enregistrer <span>→</span></button>
      </div>
    </form>
  </div>
</template>
