<template>
    <div class="min-h-[calc(100vh-80px)] bg-gradient-to-b from-blue-900 to-blue-700 text-white py-10 px-6">
        <div class="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-8">
            <!-- Colonne gauche : éditeur -->
            <div class="md:col-span-2 bg-white rounded-lg shadow p-6 text-blue-900 flex flex-col">
                <label class="font-semibold mb-2">Texte {{ selectedCategorie ? selectedCategorie.metadatas.texte : ''
                    }}</label>
                <InstructionsEditor @update:promptText="promptText = $event" />
            </div>

            <!-- Colonne droite : infos -->
            <div class="space-y-6 bg-white text-blue-900 rounded-lg p-6 shadow">
                <!-- Nom -->
                <div>
                    <label class="block mb-2 font-semibold text-blue-900">Nom {{ selectedCategorie ?
                        selectedCategorie.metadatas.texte : ''
                        }}</label>
                    <input v-model="promptName" type="text" placeholder="Ex : Créations de tests unitaires"
                        class="w-full p-2 rounded border border-blue-400 text-blue-900 focus:outline-none focus:ring-2 focus:ring-blue-500" />
                </div>

                <!-- Description -->
                <div>
                    <label class="block mb-2 font-semibold text-blue-900">Description</label>
                    <textarea v-model="promptDescription" rows="3" placeholder="Description brève"
                        class="w-full p-2 rounded border border-blue-400 text-blue-900 focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"></textarea>
                </div>

                <!-- Tags -->
                <div>
                    <label class="block mb-2 font-semibold text-blue-900">Catégories (1 à 3 tags)</label>

                    <!-- Tags sélectionnés -->
                    <div class="flex flex-wrap gap-2 mb-2">
                        <span v-for="tag in selectedTags" :key="tag"
                            class="bg-blue-200 text-blue-900 px-3 py-1 rounded-full flex items-center cursor-pointer hover:bg-blue-300"
                            @click="removeTag(tag)" title="Cliquer pour supprimer">
                            {{ tag }} <span class="ml-2 font-bold">×</span>
                        </span>
                    </div>

                    <!-- Champ unique pour sélection ou création de tags -->
                    <div class="relative max-w-xs">
                        <input v-model="search" @input="onInput" @keydown.down.prevent="onArrowDown"
                            @keydown.up.prevent="onArrowUp" @keydown.enter.prevent="onEnterOrCreate" type="text"
                            placeholder="Chercher ou ajouter un tag"
                            class="w-full p-2 border border-blue-400 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                            :disabled="selectedTags.length >= 3" />

                        <!-- Suggestions -->
                        <ul v-if="filteredTags.length > 0"
                            class="absolute z-10 bg-white border border-blue-300 mt-1 w-full rounded shadow-md max-h-40 overflow-y-auto">
                            <li v-for="(tag, index) in filteredTags" :key="tag.nom" @click="selectTag(tag)" :class="[
                                'px-3 py-1 cursor-pointer hover:bg-blue-100',
                                index === activeIndex ? 'bg-blue-100' : ''
                            ]">
                                {{ tag.nom }}
                            </li>
                        </ul>
                    </div>
                    <p class="mt-1 text-xs text-blue-700 italic">Vous pouvez choisir jusqu’à 3 tags.</p>
                </div>

                <!-- Bouton sauvegarder -->
                <button @click="savePrompt" :disabled="!canSave"
                    class="w-full bg-blue-600 disabled:bg-blue-300 text-white font-semibold py-2 rounded hover:bg-blue-700 transition">
                    Enregistrer {{ selectedCategorie ? selectedCategorie.metadatas.enregistrer : ''
                    }}
                </button>
            </div>
        </div>

        <div v-if="showNotification"
            class="fixed bottom-5 right-5 bg-gray-800 text-white px-4 py-2 rounded shadow-lg transition-opacity duration-300">
            {{ notification }}
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useBibliothequeStore } from '@/stores/bibliotheque/store' // store Pinia
import { createPrompt } from '@/stores/bibliotheque/api'
import InstructionsEditor from '@/components/InstructionsEditor.vue'
import { storeToRefs } from 'pinia'

const store = useBibliothequeStore()
const { selectedCategorie } = storeToRefs(store)

const promptName = ref('')
const promptDescription = ref('')
const promptText = ref('')

const selectedTags = ref([])
const filteredTags = ref([])
const search = ref('')
const activeIndex = ref(-1)

const notification = ref('')
const showNotification = ref(false)

function removeTag(tag) {
    selectedTags.value = selectedTags.value.filter(t => t !== tag)
}

const canSave = computed(() => {
    return (
        promptName.value.trim().length > 0 &&
        promptDescription.value.trim().length > 0 &&
        selectedTags.value.length >= 1 &&
        selectedTags.value.length <= 3
    )
})

async function savePrompt() {
    if (!canSave.value) return

    const newPrompt = {
        nom: promptName.value.trim(),
        description: promptDescription.value.trim(),
        categorie: selectedCategorie.value.nom,
        contenu: promptText.value.trim(),
        tags: selectedTags.value.map(tag => ({ nom: tag, type: selectedCategorie.value.nom })),
        historiques: [],
        parametres: [], // Pas de paramètres ici
        date: new Date().toISOString().split('T')[0],
    }

    try {
        await createPrompt(newPrompt)
        showNotif(`${selectedCategorie.value.metadatas.label} enregistré !`)
    } catch (error) {
        console.error('Erreur lors de l\'enregistrement : ', error)
        showNotif(`Erreur lors de l\'enregistrement du ${selectedCategorie.value.metadatas.label}.`)
    }
}

async function onInput() {
    if (search.value.trim()) {
        await store.loadTagsByNomAndCategorie(search.value, selectedCategorie.value.nom) // 'PROMPT' ou autre type selon logique métier
        filteredTags.value = store.tags.filter(tag => !selectedTags.value.includes(tag))
        activeIndex.value = -1
    } else {
        filteredTags.value = []
    }
}

function selectTag(tag) {
    let tagValue = tag.nom
    if (!selectedTags.value.includes(tagValue) && selectedTags.value.length < 3) {
        selectedTags.value.push(tagValue)
    }
    search.value = ''
    filteredTags.value = []
}

function onArrowDown() {
    if (filteredTags.value.length > 0) {
        activeIndex.value = (activeIndex.value + 1) % filteredTags.value.length
    }
}

function onArrowUp() {
    if (filteredTags.value.length > 0) {
        activeIndex.value =
            (activeIndex.value - 1 + filteredTags.value.length) % filteredTags.value.length
    }
}

function onEnterOrCreate() {
    if (filteredTags.value.length == 1) {
        selectTag(filteredTags.value[0]);
    } else if (activeIndex.value >= 0 && filteredTags.value[activeIndex.value]) {
        selectTag(filteredTags.value[activeIndex.value])
    } else if (search.value.trim() && !selectedTags.value.includes(search.value.trim()) && selectedTags.value.length < 3) {
        selectedTags.value.push(search.value.trim())
    }
    search.value = ''
    filteredTags.value = []
}

function showNotif(message) {
    notification.value = message
    showNotification.value = true
    setTimeout(() => (showNotification.value = false), 3000)
}
</script>
