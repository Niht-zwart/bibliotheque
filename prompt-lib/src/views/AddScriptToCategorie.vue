<template>
    <AddPrompt v-if="selectedCategorie && selectedCategorie.parametres" />
    <AddInstructions v-else />
</template>

<script setup>
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useBibliothequeStore } from '@/stores/bibliotheque/store'
import { storeToRefs } from 'pinia'
import AddInstructions from '@/components/AddInstructions.vue'
import AddPrompt from '@/components/AddPrompt.vue'

const route = useRoute()
const nomCategorie = route.params.categorie

const store = useBibliothequeStore()
const { selectedCategorie } = storeToRefs(store)

// Charger les tags au montage
onMounted(async () => {
    await store.loadCategorie(nomCategorie)
})

</script>