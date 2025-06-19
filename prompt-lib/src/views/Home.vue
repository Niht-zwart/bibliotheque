<template>
  <div class="min-h-[calc(100vh-80px)] bg-gradient-to-b from-blue-900 to-blue-700 p-6 text-white">
    <div class="max-w-4xl mx-auto">
      <!-- Choix entre Prompts et Instructions -->
      <div class="flex justify-center space-x-4 mb-10">
        <button v-for="(items, key) in store.categories" :key="key" class="px-5 py-2 rounded-xl font-semibold shadow"
          :class="selectedType === key ? 'bg-blue-600 hover:bg-blue-500 text-white' : 'bg-white text-blue-700'"
          @click="selectType(key)">
          {{ toDisplay(key) }}
        </button>
      </div>

      <!-- Liste des tags -->
      <div v-if="currentTags !== undefined && currentTags.length" class="grid grid-cols-2 md:grid-cols-3 gap-6 mb-12">
        <router-link v-for="tag in currentTags" :key="tag.tag" :to="`/${selectedType}/tag/${tag.tag}`"
          class="bg-white/90 text-blue-900 p-4 rounded-xl shadow hover:shadow-lg cursor-pointer transition block">
          <div class="text-lg font-semibold">{{ tag.tag }}</div>
          <div class="text-sm text-blue-600">{{ tag.nombres }} {{ selectedCategorie ?
            selectedCategorie.metadatas['categorie.titre'] : toDisplay(selectedType)
            }}</div>
        </router-link>
      </div>

      <!-- Bouton d'ajout -->
      <div class="flex justify-center">
        <router-link :to="`/create/${selectedType}`"
          class="px-6 py-3 bg-white text-blue-700 font-bold rounded-xl shadow hover:bg-blue-100 transition">
          ➕ Ajouter {{ selectedCategorie ?
            selectedCategorie.metadatas['bouton.ajouter'] : toDisplay(selectedType)
          }}
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { useBibliothequeStore } from '@/stores/bibliotheque/store'

const selectedType = ref('prompts')
const store = useBibliothequeStore()
const { selectedCategorie } = storeToRefs(store)


// Charger les tags au montage
onMounted(async () => {
  await store.loadBibliotheque()
  const firstEntry = Object.entries(store.categories)[0]
  if (firstEntry) {
    const [firstKey, firstValue] = firstEntry
    selectType(firstKey)
  }
})

// Tags dynamiques
const currentTags = computed(() => {
  return store.categories[selectedType.value]
})

const selectType = async (type) => {
  await store.loadCategorie(type)
  selectedType.value = type
}

const toDisplay = (stringToCapitalize) => {
  return stringToCapitalize.charAt(0).toUpperCase() + stringToCapitalize.slice(1).toLowerCase()
}
</script>
