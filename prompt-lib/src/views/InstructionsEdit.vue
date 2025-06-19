<template>
    <div class="min-h-[calc(100vh-80px)] bg-gradient-to-b from-blue-900 to-blue-700 text-white py-10 px-6">
        <div class="max-w-4xl mx-auto space-y-6">
            <h1 v-if="elem != undefined" class="text-3xl font-bold">Modification de l'instruction : {{ elem.nom }}
            </h1>

            <!-- Éditeur de texte sur fond blanc -->
            <div v-if="textContent !== null" class="bg-white text-black rounded shadow p-4">
                <InstructionsEditor v-model="elem.contenu" @update:promptText="textContent = $event"
                    class="editor-size min-h-[300px]" />
            </div>

            <!-- Boutons -->
            <div class="flex items-center justify-end gap-4">
                <button @click="saveVersion"
                    class="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded shadow">
                    💾 Enregistrer
                </button>
                <router-link :to="`/instruction/${route.params.id}`">
                    <button class="bg-gray-400 hover:bg-gray-500 text-white px-4 py-2 rounded shadow">
                        ❌ Annuler
                    </button>
                </router-link>
            </div>
        </div>
    </div>

    <div v-if="showNotification"
        class="fixed bottom-5 right-5 bg-gray-800 text-white px-4 py-2 rounded shadow-lg transition-opacity duration-300">
        {{ notification }}
    </div>
</template>


<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useBibliothequeStore } from '@/stores/bibliotheque/store'
import { createPrompt } from '@/stores/bibliotheque/api'
import InstructionsEditor from '@/components/InstructionsEditor.vue'
import { storeToRefs } from 'pinia'

const route = useRoute()
const categorie = route.params.categorie
const router = useRouter()
const store = useBibliothequeStore()
const { selectedCategorie } = storeToRefs(store)

const notification = ref('')
const showNotification = ref(false)
const textContent = ref(null)
const elem = ref(null)

// Charger l'élément selon l'id dans la route
const loadElement = async () => {
    const id = route.params.id
    const version = route.params.version
    if (!id) return
    await store.loadElementById(id)
    elem.value = store.selectedElement
    if (version === 'latest') {
        textContent.value = elem.value.contenu
    } else {
        textContent.value = elem.value.historiques[version]
    }
}

onMounted(() => {
    loadElement()
})

const saveVersion = async () => {
    console.log(textContent.value)
    try {
        await createPrompt({
            id: elem.value.id,
            nom: elem.value.nom,
            description: elem.value.description,
            tags: elem.value.tags,
            date: elem.value.date,
            categorie: categorie,
            contenu: textContent.value,
            history: elem.value.historiques
        })
        router.push(`/instruction/${route.params.id}`)
        showNotif(`${selectedCategorie.value.metadatas.label} enregistré !`)
    } catch (e) {
        showNotif('Erreur lors de l’enregistrement.')
    }

}

function showNotif(message) {
    notification.value = message
    showNotification.value = true
    setTimeout(() => (showNotification.value = false), 3000)
}
</script>

<style scoped>
.markdown-preview {
    white-space: pre-wrap;
    padding: 1rem;
    background-color: black;
    border-radius: 0.5rem;
    font-family: monospace;
    font-size: 0.95rem;
}
</style>
