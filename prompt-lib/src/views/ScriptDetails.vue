<template>
    <PrompDetails v-if="selectedCategorie && selectedCategorie.parametres" />
    <InstructionsDetails v-else />
</template>

<script setup>
import { onMounted } from 'vue';
import { useRoute } from 'vue-router'
import { useBibliothequeStore } from '@/stores/bibliotheque/store'
import { storeToRefs } from 'pinia'
import InstructionsDetails from '../components/InstructionsDetails.vue';
import PrompDetails from '../components/PrompDetails.vue';

const route = useRoute()
const nomCategorie = route.params.categorie

const store = useBibliothequeStore()
const { selectedCategorie } = storeToRefs(store)

// Charger les tags au montage
onMounted(async () => {
    await store.loadCategorie(nomCategorie)
})


</script>